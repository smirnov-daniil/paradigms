foldLeft([], Z, _, Z).
foldLeft([H | T], Z, F, R) :- G =.. [F, Z, H, RH], call(G), foldLeft(T, RH, F, R).
mul(A, B, R) :- R is A * B.
check([]).
check([X]) :- prime(X).
check([X , Y | Rest]) :- prime(X), X =< Y, check([Y|Rest]).

init(MAX_N) :- sieve(2, sqrt(MAX_N), MAX_N), !.

sieve(PrimeFrom, PrimeTo, _) :- PrimeFrom > PrimeTo, !.
sieve(I, SqrtMax, Max) :- (composite(I); From is I * I, mark(I, From, Max)), NextI is I + 1, sieve(NextI, SqrtMax, Max).

mark(_, From, To) :- From > To, !.
mark(Prime, From, To) :- assert(composite(From)), NextFrom is From + Prime, mark(Prime, NextFrom, To).

prime(N) :- \+ composite(N).

find_next_prime(PrevPrime, _, NextPrime) :- next_prime(PrevPrime, NextPrime), !.
find_next_prime(PrevPrime, NextPrime, NextPrime) :- prime(NextPrime), assertz(next_prime(PrevPrime, NextPrime)).
find_next_prime(PrevPrime, N, NextPrime) :- NextN is N + 1, find_next_prime(PrevPrime, NextN, NextPrime).

prime_divisors(N, []) :- N = 1, !.
prime_divisors(N, D) :- var(N), check(D), foldLeft(D, 1, mul, N), !.
prime_divisors(N, D) :- number(N), prime_divisors(N, D, 2), !.
prime_divisors(N, [N], _) :- prime(N), !.
prime_divisors(N, [Divisor | Tail], Divisor) :-
    prime(Divisor), 0 is mod(N, Divisor), Divided is N / Divisor, prime_divisors(Divided, Tail, Divisor).
prime_divisors(N, Divisors, Divisor) :-
    ND is Divisor + 1, find_next_prime(Divisor, ND, NextDivisor), prime_divisors(N, Divisors, NextDivisor).

intersection([], _, []).
intersection(_, [], []).
intersection([H|T1], [H|T2], [H|Intersection]) :-
    intersection(T1, T2, Intersection).
intersection([H1|T1], [H2|T2], Intersection) :-
    H1 < H2,
    intersection(T1, [H2|T2], Intersection).
intersection([H1|T1], [H2|T2], Intersection) :-
    H1 > H2,
    intersection([H1|T1], T2, Intersection).

gcd(A, B, C) :- prime_divisors(A, AD), prime_divisors(B, BD), intersection(AD, BD, D), prime_divisors(C, D), !.
