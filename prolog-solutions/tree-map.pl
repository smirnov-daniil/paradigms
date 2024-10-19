less((X, _), (Y, _)) :- X < Y, !.

foldLeft([], Z, _, Z).
foldLeft([H | T], Z, F, R) :- G =.. [F, Z, H, RH], call(G), foldLeft(T, RH, F, R).

split(List, L, M, R) :-
    length(List, Len), Median is Len // 2,
    length(L, Median), append(L, [M | R], List), !.

sorted([]) :- !.
sorted([X]) :- !.
sorted([X, Y | T]) :- less(X, Y), sorted([Y | T]), !.

max(A, A, A) :- !.
max(A, B, A) :- A > B, !.
max(A, B, B) :- A < B, !.


ins(nil, X, node(X, nil, nil, 1)) :- !.
ins(node((Key, _), L, R, H), (Key, Value), node((Key, Value), L, R, H)) :- !.
ins(node(X, L, R, H), Y, node(X, L2, R, H2)) :- less(Y, X), H2 is H + 1, ins(L, Y, L2), !.
ins(node(X, L, R, H), Y, node(X, L, R2, H2)) :- less(X, Y), H2 is H + 1, ins(R, Y, R2), !.

build_sorted([], nil, 0) :- !.
build_sorted(List, node(Root, L, R, H), H) :-
    split(List, P, Root, S), build_sorted(P, L, LH), build_sorted(S, R, RH), fix_height(LH, RH, H), !.

take(node(X, _, _, _), X) :- !.
take(node(X, _, R, _), Y) :- less(X, Y), take(R, Y), !.
take(node(X, L, _, _), Y) :- less(Y, X), take(L, Y), !.

take_ceiling(node(X, _, _, _), X, X) :- !.
take_ceiling(node(X, _, R, _), Y, E) :- less(X, Y), take_ceiling(R, Y, E), !.
take_ceiling(node(X, L, _, _), Y, E) :- less(Y, X), take_ceiling(L, Y, E), !.
take_ceiling(node(X, _, _, _), Y, X) :- less(Y, X), !.


del(nil, _, nil) :- !.
del(node((Key, _), L, R, _), Key, Result)      :- merge(L, R, Result), !.
del(node(X, L, R, H), Key, node(X, L2, R, H2)) :- less((Key, _), X), H2 is H - 1, del(L, Key, L2), !.
del(node(X, L, R, H), Key, node(X, L, R2, H2)) :- less(X, (Key, _)), H2 is H - 1, del(R, Key, R2), !.

merge(nil, T, T) :- !.
merge(T, nil, T) :- !.
merge(node(X, L, R, H), T, node(X, L, R2, H2)) :- H2 is H - 1, merge(R, T, R2), !.

height(nil, 0) :- !.
height(node(_, _, _, H), H) :- !.

fix_height(AH, BH, Height) :- max(AH, BH, H1), Height is H1 + 1, !.

%          A            B          %
%         / \          / \         %
%        B   R   ->   P   A        %
%       / \              / \       %
%      P   Q            Q   R      %
rotate_right(node(A, node(B, P, Q, _), R, _), node(B, P, node(A, Q, R, AH), BH)) :-
    height(P, PH), height(Q, QH), QH < PH, height(R, RH),
    fix_height(QH, RH, AH), fix_height(PH, AH, BH), !.

%       A                 C        %
%      / \              /   \      %
%     B   S            B     A     %
%    / \       ->     / \   / \    %
%   P   C            P   Q R   S   %
%      / \                         %
%     Q   R                        %
rotate_left_right(node(A, node(B, P, node(C, Q, R, Ch), _), S, _), node(C, node(B, P, Q, BH), node(A, R, S, AH), CH)) :-
    height(P, PH), Ch > PH, height(Q, QH), height(R, RH), height(S, SH),
    fix_height(PH, QH, BH), fix_height(RH, SH, AH), fix_height(BH, AH, CH), !.

%        A                B        %
%       / \              / \       %
%      P   B     ->     A   R      %
%         / \          / \         %
%        Q   R        P   Q        %
rotate_left(node(A, P, node(B, Q, R, _), _), node(B, node(A, P, Q, AH), R, BH)) :-
    height(Q, QH), height(R, RH), QH < RH, height(P, PH),
    fix_height(PH, QH, AH), fix_height(RH, AH, BH), !.

%      A                 C         %
%     / \              /   \       %
%    P   B            A     B      %
%       / \    ->    / \   / \     %
%      C   S        P   Q R   S    %
%     / \                          %
%    Q   R                         %
rotate_right_left(node(A, P, node(B, node(C, Q, R, Ch), S , _), _), node(C, node(A, P, Q, AH), node(B, R, S, BH), CH)) :-
    height(S, SH), Ch > SH, height(P, PH), height(Q, QH), height(R, RH),
    fix_height(PH, QH, AH), fix_height(RH, SH, BH), fix_height(AH, BH, CH), !.

balance(nil, nil) :- !.
balance(node(X, L, R, _), Result) :-
    balance(L, LL), balance(R, RR),
    height(LL, LH), height(RR, RH),
    Diff is LH - RH,
    fix_height(LH, RH, HH),
    Node = node(X, LL, RR, HH),
    (
        Diff = 2, (rotate_right(Node, Result), ! ; rotate_left_right(Node, Result), !)
    ;
        Diff = -2, (rotate_left(Node, Result), ! ; rotate_right_left(Node, Result), !)
    ;
        Result = Node, !
    ).

map_put(Root, Key, Value, Result) :- ins(Root, (Key, Value), T), balance(T, Result), !.
map_put(Root, X, Result)          :- ins(Root, X, T),            balance(T, Result), !.

map_build(List, Tree) :- sorted(List), !, build_sorted(List, Tree, _), !.
map_build(List, Tree) :- foldLeft(List, nil, map_put, Tree), !.

map_get(Root, Key, Value) :- take(Root, (Key, Value)), !.

map_remove(Tree, Key, Result) :- del(Tree, Key, T), balance(T, Result), !.

map_ceilingEntry(Map, Key, Entry) :- take_ceiling(Map, (Key, _), Entry), !.
map_removeCeiling(Map, Key, Result) :- take_ceiling(Map, (Key, _), (DelKey, Y)), map_remove(Map, DelKey, Result), !.
map_removeCeiling(Map, _, Map) :- !.
