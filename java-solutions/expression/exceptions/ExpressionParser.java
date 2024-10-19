package expression.exceptions;

import expression.ListExpression;
import expression.TripleExpression;
import expression.parser.RecursiveParser;
import expression.parser.exceptions.*;

import java.util.List;

/**
 * @author DS2
 */
public class ExpressionParser implements TripleParser, ListParser {
    @Override
    public TripleExpression parse(String expression)
            throws ParserInvalidOperatorException, ParserInvalidConstantException,
            ParserTokenException, ParserEndOfFileException {
        return new RecursiveParser(expression, new CheckedOperators()).parse();
    }

    @Override
    public ListExpression parse(String expression, List<String> variables)
            throws ParserInvalidOperatorException, ParserInvalidConstantException,
            ParserTokenException, ParserEndOfFileException {
        return new RecursiveParser(expression, new CheckedOperators(), variables).parse();
    }
}
