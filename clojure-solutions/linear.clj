; checks
(defn vectors-same-dimension? [vectors]
  (let [first-count (count (first vectors))]
   (every? #(= first-count(count %)) (rest vectors))))
(defn matrices-same-dimension? [matrices]
  (and (vectors-same-dimension? matrices) (->> matrices (apply map #(vectors-same-dimension? %&)) (every? true?))))
(defn d3? [v] (== 3 (count v)))
(defn valid-vector? [v] (and (vector? v) (every? number? v)))
(defn valid-matrix? [m] (and (vector? m) (every? valid-vector? m) (vectors-same-dimension? m)))
(defn ladder-matrix? [matrix]
  (or (empty? matrix) (and (== (count matrix) (count (first matrix))) (ladder-matrix? (rest matrix)))))
(defn valid-simplex? [s]
  (or (valid-vector? s) (and (ladder-matrix? s) (every? valid-simplex? s))))

; utilities
(defn make-by-component-operator [do-by-component component-operator]
  (fn [& args] (do-by-component component-operator args)))
(defn add-hoare-logic [pre post f]
  (fn [& args] {:pre [(apply pre args)] :post [(post %)]} (apply f args)))

; logic
(defn vector-pre-post [f]
  (letfn [(pre [& args] (and (every? valid-vector? args) (vectors-same-dimension? args)))
          (post [result] (valid-vector? result))]
    (add-hoare-logic pre post f)))
(defn matrix-pre-post [f]
  (letfn [(pre [& args] (and (every? valid-matrix? args) (matrices-same-dimension? args)))
          (post [result] (valid-matrix? result))]
    (add-hoare-logic pre post f)))
(defn simplex-pre-post [f]
  (letfn [(pre [& args] (and (every? valid-simplex? args) (vectors-same-dimension? args)))
          (post [result] (valid-simplex? result))]
    (add-hoare-logic pre post f)))

; by component functions
(defn do-vector-by-component [do args] (apply (partial mapv do) args))
(defn do-tensor-by-component [do args]
  (letfn [(rec [& args] (if (every? number? args) (apply do args) (apply mapv rec args)))] (apply rec args)))

(def make-vector-by-component-operator (partial make-by-component-operator do-vector-by-component))
(def make-tensor-by-component-operator (partial make-by-component-operator do-tensor-by-component))

; HW
(def easy-v+ (make-vector-by-component-operator +))
(def easy-v- (make-vector-by-component-operator -))
(def easy-v* (make-vector-by-component-operator *))
(def easy-vd (make-vector-by-component-operator /))

(def v+ (vector-pre-post easy-v+))
(def v- (vector-pre-post easy-v-))
(def v* (vector-pre-post easy-v*))
(def vd (vector-pre-post easy-vd))

(def m+ (-> easy-v+ make-vector-by-component-operator matrix-pre-post))
(def m- (-> easy-v- make-vector-by-component-operator matrix-pre-post))
(def m* (-> easy-v* make-vector-by-component-operator matrix-pre-post))
(def md (-> easy-vd make-vector-by-component-operator matrix-pre-post))
(def x+ (-> + make-tensor-by-component-operator simplex-pre-post))
(def x- (-> - make-tensor-by-component-operator simplex-pre-post))
(def x* (-> * make-tensor-by-component-operator simplex-pre-post))
(def xd (-> / make-tensor-by-component-operator simplex-pre-post))

(defn scalar [& vectors]
  {:pre [(every? valid-vector? vectors) (vectors-same-dimension? vectors)]
   :post [(number? %)]}
  (reduce + (reduce v* vectors)))
(defn v*s [v & s]
  {:pre [(valid-vector? v) (every? number? s)]
   :post [(valid-vector? v)]}
  (mapv (partial * (reduce * s)) v))

(defn vect [& vectors]
  {:pre [(every? valid-vector? vectors) (vectors-same-dimension? vectors) (d3? (first vectors))]
   :post [(valid-vector? %)]}
  (reduce #(let [a0 (first %1) a1 (second %1) a2 (last %1)
                 b0 (first %2) b1 (second %2) b2 (last %2)]
             (vector (- (* a1 b2) (* b1 a2))
                     (- (* a2 b0) (* b2 a0))
                     (- (* a0 b1) (* b0 a1)))) vectors))
(defn transpose [m] (apply mapv vector m))
(defn m*s [m & s]
  {:pre [(every? vector? m)]
   :post [every? vector? %]}
  (mapv #(apply v*s % s) m))
(defn m*v [m v]
  {:pre [(every? vector? m) (valid-vector? v)]
   :post [every? vector? %]}
  (mapv (partial scalar v) m))
(defn m*m
  ([m]
   {:pre [(valid-matrix? m)]
    :post [(valid-matrix? %)]}
   (identity m))
  ([m1 m2]
   {:pre [(valid-matrix? m1) (valid-matrix? m2) (vectors-same-dimension? [(first m1) m2])]
    :post [(valid-matrix? %)]}
   (transpose (mapv (partial m*v m1) (transpose m2))))
  ([m1 m2 & matrices]
   (reduce m*m (m*m m1 m2) matrices)))