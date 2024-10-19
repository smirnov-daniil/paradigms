(defn parser [make-const make-variable make-operator expression]
  (letfn [(parse [token]
            (let [operators (deref make-operator)]
              (cond (list? token) (apply (-> token first str operators) (->> token rest (map parse)))
                    (number? token) (make-const token) :else (-> token name make-variable))))]
    (-> expression read-string parse)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;                        Functional                         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn variable [name] (fn [variable-list] (variable-list name)))
(def constant constantly)
(defn operator [op]
  (fn [& operands]
    (fn [variable-list] (if (empty? operands) (op) (apply op ((apply juxt operands) variable-list))))))

(defn divider [& args] (if (-> args rest empty?) (->> args first (/ 1.0)) (->> args rest (reduce *) double (/ (first args)))))

(def *oper-table-functional (volatile! {}))
(defn vupdate! [map key operation] (vswap! map assoc key operation))
(defmacro defoperfunction
  ([literal operation key]
   `(do
      (def ~literal (operator ~operation))
      (vupdate! *oper-table-functional (name ~key) ~literal)))
  ([literal operation] `(defoperfunction ~literal ~operation ~(name literal))))

(defn _mean [& args] (/ (apply + args) (count args)))
(defn _varn [& args] (letfn [(square [a] (* a a))]
                       (- (->> args (map square) (apply _mean)) (->> args (apply _mean) square))))
(defoperfunction add + '+)
(defoperfunction subtract - '-)
(defoperfunction multiply * '*)
(defoperfunction divide divider '/)
(defoperfunction negate -)
(defoperfunction mean _mean)
(defoperfunction varn _varn)

(def parseFunction (partial parser constant variable *oper-table-functional))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;                          Object                           ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defmacro java-wrapper
  [arity method] (let [args (repeatedly arity gensym)] `(fn [this# ~@args] (~method this# ~@args))))
(defn curry2 [f] (fn [a] (partial f a)))

(definterface IExpression
  (^Number evaluate [variable-list])
  (diff [variable])
  (^String toStringPostfix [])
  (^String toStringInfix []))

(deftype JConstant [value]
  IExpression
  (evaluate [_ _] value)
  (diff [_ _] (JConstant. 0))
  (toStringPostfix [_] (str value))
  (toStringInfix [_] (str value))
  Object
  (toString [_] (str value)))
(defn Constant [value] (JConstant. value))

(def ZERO (Constant 0))
(def ONE (Constant 1))
(def TWO (Constant 2))

(deftype JVariable [name]
  IExpression
  (evaluate [_ variable-list] (variable-list (clojure.string/lower-case (str (get name 0)))))
  (diff [_ variable] (if (= (clojure.string/lower-case (str (get name 0))) variable) ONE ZERO))
  (toStringPostfix [_] name)
  (toStringInfix [_] name)
  Object
  (toString [_] name))
(defn Variable [name] (JVariable. name))

(def *oper-table-object (volatile! {}))

(defmacro defoperobject
  ([literal operation key derivative]
   (let [class-name (str (gensym))
         constructor (symbol (str (name class-name) "."))]
     `(do
        (deftype ~class-name [operands#]
          IExpression
          (evaluate [_ variable-list#]
            (if (empty? operands#)
              (~operation)
              (apply ~operation ((->> operands# (map (->> .evaluate (java-wrapper 1) curry2)) (apply juxt)) variable-list#))))
          (diff [_ variable#]
            (~derivative (fn [& args#] (~constructor args#)) operands#
              ((->> operands# (map (->> .diff (java-wrapper 1) curry2)) (apply juxt)) variable#)))
          (toStringPostfix [_] (str "(" (clojure.string/join " " (map #(.toStringPostfix %) operands#)) " " ~key ")"))
          (toStringInfix [_] (if (-> operands# rest empty?)
                               (str ~key " " (.toStringInfix (first operands#)))
                               (str "(" (.toStringInfix (first operands#)) " " ~key " " (.toStringInfix (second operands#)) ")")))
          Object
          (toString [_] (str "(" ~key " " (clojure.string/join " " operands#) ")"))
          )
        (defn ~literal [& args#] (~constructor args#))
        (vupdate! *oper-table-object (name ~key) ~literal))))
  ([literal operation derivative] `(defoperobject ~literal ~operation ~(-> literal name clojure.string/lower-case) ~derivative)))


(defoperobject Add + '+ (fn [op fs dfs] (apply op dfs)))
(defoperobject Subtract - '- (fn [op fs dfs] (apply op dfs)))
(defoperobject Negate - (fn [op fs dfs] (apply op dfs)))
(defn _multiply-derivative [op fs dfs] (reduce (fn [[f df] [g dg]] (vector (op f g) (Add (op df g) (op f dg)))) (map vector fs dfs)))
(defoperobject Multiply * '* (comp last _multiply-derivative))
(def multiply-derivative (partial _multiply-derivative Multiply))
(defoperobject Divide divider '/
  (fn [op fs dfs]
    (let [f (first fs) df (first dfs)]
      (if (-> fs rest empty?)
        (Negate (op df (Multiply f f)))
        (let [[g dg] (multiply-derivative (rest fs) (rest dfs))]
          (op (Subtract (Multiply df g) (Multiply f dg)) (Multiply g g)))))))

(defn _mean-derivative [op fs dfs] (Divide (reduce Add dfs) (Constant (count dfs))))
(defoperobject Mean _mean _mean-derivative)
(def mean-derivative (partial _mean-derivative Mean))
(defn square-derivative [[f df]] (Multiply TWO f df))
(defn varn-derivative [op fs dfs] (Subtract (mean-derivative [] (map square-derivative (map vector fs dfs))) (square-derivative [(mean-derivative [] fs) (mean-derivative [] dfs)])))
(defoperobject Varn _varn varn-derivative)

(defoperobject AbsC (fn [im re] (Math/sqrt (+ (* im im) (* re re)))) #(ONE))
(defoperobject PhiC (fn [im re] (Math/atan2 im re)) #(ONE))
(def parseObject (partial parser Constant Variable *oper-table-object))
(defmacro defjw [fn-name arity] `(def ~(symbol (subs (name fn-name) 1)) (java-wrapper ~arity ~fn-name)))
(defjw .evaluate 1)
(defjw .toString 0)
(defjw .diff 1)
(defjw .toStringPostfix 0)
(defjw .toStringInfix 0)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;                        Combinator                         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(load-file "parser.clj")
(def *space (+char " \r\t\n"))
(def *ws (-> *space +star +ignore))
(def *digit (->> (range 0 10) (reduce str) +char))
(def *number (-> *digit +plus +str))
(def *sign (+char "+-"))
(def *left_paren (+ignore (+char "(")))
(def *right_paren (+ignore (+char ")")))
(def *constant (+seqf (comp Constant read-string str) (+opt *sign) *number (+char ".") *number))
(def *variable (->> "XxYyZz" +char +plus +str (+map Variable)))
(def *base (+or *constant *variable))
(defn *operator-literal [literal] (->> literal seq (map (comp +char str)) (apply +seq) +str))
(defn *operators [ops] (let [oper-table (deref *oper-table-object)]
                         (->> ops (map *operator-literal) (apply +or) (+map oper-table))))
(def *operator (let [oper-table (deref *oper-table-object)] (-> oper-table keys *operators)))
(defparser parseObjectPostfix
           *expression (+seqf (fn [operands operator] (apply operator operands))
                              *left_paren *ws (+plus (+seqn 0 (+or *base (delay *expression)) *ws)) *operator *ws *right_paren)
           *parser (+seqn 0 *ws (+or *base *expression) *ws))

(defn infix [left rst] (if (empty? rst) left (recur (#((first %) left (second %)) (first rst)) (rest rst))))

(defn *left-assoc [ops base] (+seqf infix base (+star (+seq *ws ops *ws base))))
(defn *binary-operator [ops base] (+seqf (fn eval [l, op, r] (op l r)) base *ws ops *ws (+or (delay (*binary-operator ops base)) base)))
(defn *right-assoc [ops base] (+or (*binary-operator ops base) base))
(defmacro *reduce-binary [priorities base]
  (if (empty? priorities)
    base `(*left-assoc (*operators ~(first priorities)) (*reduce-binary ~(rest priorities) ~base))))

(defparser parseObjectInfix
           *scoped (+seqn 0 *left_paren *ws (delay *expression) *ws *right_paren)
           *primary (+or *scoped *base)
           *unary-operator (+or (+seqf (fn [op e] (op e)) (*operators ["negate"]) *ws (delay *unary-operator)) *primary)
           *complex (*right-assoc (*operators ["phic" "absc"]) *unary-operator)
           *expression (*reduce-binary [["+" "-"] ["*" "/"]] *complex)
           *parser (+seqn 0 *ws *expression *ws))

(println (toString (diff (Variable "x") "y")))