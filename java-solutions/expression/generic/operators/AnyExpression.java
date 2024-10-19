package expression.generic.operators;

import expression.Operator;
import expression.ToMiniString;
import expression.generic.evaluators.Evaluator;

public interface AnyExpression<T> extends Operator, ToMiniString {
    T evaluate(T x, T y, T z, final Evaluator<T> evaluator);
}
