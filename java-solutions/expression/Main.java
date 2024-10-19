package expression;

import expression.exceptions.ExpressionParser;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        ExpressionParser parser = new ExpressionParser();
        String expression = "(\u000B\t$0A\u000B\t\u2029\n" +
                "\u2029+ 0)";
        System.out.println(parser.parse(expression, List.of("$1", "$2", "$3", "$4", "$5", "$6")).evaluate(List.of(3, 4, 5)));
    }
    private static void test2() {
        //System.out.println((new ExpressionParser()).parse("10 10").toMiniString());
    }
    private static void test1() {
        Expression
                eq1 = new Multiply(new Const(2), new Variable("x")),
                eq2 = new Multiply(new Const(2), new Variable("x")),
                eq3 = new Multiply(new Variable("x"), new Const(2));
        System.out.println(
                eq1.equals(eq2)
        );
        System.out.println(
                eq1.equals(eq3)
        );
        Expression
                eval = new Subtract(
                        new Multiply(
                                new Const(2),
                                new Variable("x")
                        ),
                        new Const(3)
                );
        System.out.println(
                eval.evaluate(5)
        );
        Expression
                toStr1 = new Subtract(
                        new Multiply(
                                new Const(2),
                                new Variable("x")
                        ),
                        new Const(3)
                ),
                toStr2 = new Divide(
                        new Multiply(
                                new Negate(new Add(
                                        new Const(2),
                                        new Variable("x")
                                )),
                                new Negate(new Variable("x"))
                        ),
                        new Divide(
                                new Multiply(
                                        new Variable("x"),
                                        new Divide(
                                                new Const(3),
                                                new Const(2)
                                        )
                                ),
                                new Add(
                                        new Const(2),
                                        new Variable("x")
                                )
                        )
                ),
                toStr3 = new Divide(
                        new Multiply(
                                new Add(
                                        new Const(2),
                                        new Variable("x")
                                ),
                                new Variable("x")
                        ),
                        new Const(4)
                );
        System.out.println(
                toStr1
        );
        System.out.println(
                toStr1.toMiniString()
        );
        System.out.println(
                toStr2.toMiniString()
        );
        System.out.println(
                toStr2
        );
        System.out.println(
                toStr3.toMiniString()
        );
        System.out.println(
                toStr3
        );
    }
}
