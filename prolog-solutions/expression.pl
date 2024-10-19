nonvar(V, _) :- var(V).
nonvar(V, T) :- nonvar(V), call(T).

lookup(K, [(K, V) | _], V).
lookup(K, [_ | T], V) :- lookup(K, T, V).


:- load_library('alice.tuprolog.lib.DCGLibrary').

ws  --> [' '].
dot --> ['.'].
lp  --> ['('].
rp  --> [')'].

skip_ws --> [].
skip_ws --> ws, skip_ws.
expect_ws --> ws, skip_ws.

expr_p(variable(Name)) -->
  { nonvar(Name, atom_chars(Name, Chars)) },
  letters_p(Chars),
  { Chars = [_ | _], atom_chars(Name, Chars) }.

letters_p([]) --> [].
letters_p([H | T]) -->
  { member(H, [x, y, z, 'X', 'Y', 'Z']) },
  [H],
  letters_p(T).

expr_p(const(Value)) -->
  { nonvar(Value, number_chars(Value, Chars)) },
  signed_p(Chars),
  { Chars = [_ | _], number_chars(Value, Chars) }.

digit_p(H)        --> { member(H, ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9']) }, [H].
digits_p([])      --> [].
digits_p([H | T]) --> digit_p(H), digits_p(T).

signed_p(['-' | T]) --> ['-'], number_p(T).
signed_p(T)         --> number_p(T).
number_p([H | T])   --> digit_p(H), number_p(T).
number_p(['.' | T]) --> dot, digits_p(T).

op_p(op_negate)   --> ['n', 'e', 'g', 'a', 't', 'e'].
op_p(op_add)      --> ['+'].
op_p(op_subtract) --> ['-'].
op_p(op_multiply) --> ['*'].
op_p(op_divide)   --> ['/'].
op_p(op_bitnot)   --> ['~'].
op_p(op_bitand)   --> ['&'].
op_p(op_bitor)    --> ['|'].
op_p(op_bitxor)   --> ['^'].

expr_p(operation(Op, A))    -->                                     op_p(Op), expect_ws, expr_p(A).
expr_p(operation(Op, A, B)) --> lp, skip_ws, expr_p(A),  expect_ws, op_p(Op), expect_ws, expr_p(B), skip_ws, rp.

ws_expr_ws_p(E) --> skip_ws, expr_p(E), skip_ws.

infix_str(E, A) :- ground(E), phrase(ws_expr_ws_p(E), C), atom_chars(A, C), !.
infix_str(E, A) :-   atom(A), atom_chars(A, C), phrase(ws_expr_ws_p(E), C), !.


eval(op_negate, A, R)      :- R is  -A.
eval(op_bitnot, A, R)      :- R is \ A.
eval(op_add, A, B, R)      :- R is A + B.
eval(op_subtract, A, B, R) :- R is A - B.
eval(op_multiply, A, B, R) :- R is A * B.
eval(op_divide, A, B, R)   :- R is A / B.
eval(op_bitand, A, B, R)   :- R is A /\ B.
eval(op_bitor, A, B, R)    :- R is A \/ B.
eval(op_bitxor, A, B, R)   :- R is ((\ A) /\ B) \/ (A /\ (\ B)).


evaluate(const(Value), _, Value).
evaluate(variable(Name), Vars, R) :- atom_chars(Name, [N | _]), lookup(N, Vars, R).
evaluate(operation(Op, A), Vars, R) :- evaluate(A, Vars, AV), eval(Op, AV, R).
evaluate(operation(Op, A, B), Vars, R) :- evaluate(A, Vars, AV), evaluate(B, Vars, BV), eval(Op, AV, BV, R).
