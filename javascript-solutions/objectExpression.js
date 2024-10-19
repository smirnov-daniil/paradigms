function makeExpression(construct, evaluate, diff, toString, prefix, postfix) {
    construct.prototype.evaluate = evaluate;
    construct.prototype.diff     = diff;
    construct.prototype.toString = toString;
    construct.prototype.prefix   = prefix ?? toString;
    construct.prototype.postfix  = postfix ?? toString;
    return construct;
}

const Operator = makeExpression(
    function(...operands) { this.operands = operands; },
    function(...values) { return this.func(...this.operands.map(operand => operand.evaluate(...values))) },
    function(variable) {
        return this.derivative(...this.operands, ...this.operands.map(operand => operand.diff(variable)));
    },
    function() { return this.operands.join(' ') + ' ' + this.literal; },
    function () {
        return '(' + this.literal + ' ' + this.operands.map(e => e.prefix()).join(' ') + ')';
    },
    function () {
        return '(' + this.operands.map(e => e.postfix()).join(' ') + ' ' + this.literal + ')';
    }
);

const operators = {};

function makeOperator(func, literal, derivative) {
    const Op = function(...operands) {
        Operator.call(this, ...operands);
    }
    Op.prototype = Object.create(Operator.prototype);
    Op.prototype.constructor = Op;
    Op.prototype.func = func;
    Op.prototype.literal = literal;
    Op.prototype.derivative = derivative;
    operators[literal] = Op;
    return Op;
}

const Const = makeExpression(
    function(value) { this.value = value; },
    function() { return this.value; },
    function() { return Const.ZERO; },
    function() { return this.value.toString(); }
);

Const.ZERO = new Const(0);
Const.ONE = new Const(1);
Const.TWO = new Const(2);

const variablesNames = ["x", "y", "z"];

const Variable = makeExpression(
    function(name) {
        this.name = name;
        this.index = variablesNames.indexOf(name);
    },
    function(...values) { return values[this.index]; },
    function(variable) { return variable === this.name ? Const.ONE : Const.ZERO; },
    function() { return this.name; }
);

const variables =
    Object.fromEntries(variablesNames.map(name => [name, new Variable(name)]));

const square = f => new Multiply(f, f);
const squareDerivative = (f, df) => new Multiply(Const.TWO, new Multiply(f, df));

const Add = makeOperator((f, g) => f + g, "+",
    (f, g, df, dg) => new Add(df, dg)
);
const Subtract = makeOperator((f, g) => f - g, "-",
    (f, g, df, dg) => new Subtract(df, dg)
);
const Negate = makeOperator(f => -f, "negate",
    (f, df) => new Negate(df)
);
const multiplyDerivative = (f, g, df, dg) => new Add(new Multiply(df, g), new Multiply(f, dg));
const Multiply = makeOperator((f, g) => f * g, "*",
    multiplyDerivative
);
const Divide = makeOperator((f, g) => f / g, "/",
    (f, g, df, dg) => new Divide(new Subtract(new Multiply(df, g), new Multiply(f, dg)), square(g))
);
const ArcTan = makeOperator(Math.atan, "atan",
    (f, df) => new Divide(df, new Add(Const.ONE, square(f)))
);
const ArcTan2 = makeOperator(Math.atan2, "atan2",
    (f, g, df, dg) => new Divide(
        new Subtract(new Multiply(df, g), new Multiply(f, dg)),
        new Add(square(f), square(g))
    )
);

const sum = (...operands) => operands.reduce((a, b) => a + b);
const mean = (...operands) => sum(...operands) / operands.length;
const variation = (...operands) => mean(...operands.map(a => Math.pow(a, 2))) - Math.pow(mean(...operands), 2)

const meanDerivative = (...args) => new Divide(args.splice(-args.length / 2).reduce((a, b) => new Add(a, b)), new Const(args.length))
const Mean = makeOperator(mean, "mean",
    meanDerivative
);
const Var = makeOperator(variation, "var",
    function(...args) {
        const derivatives = args.splice(-args.length / 2);
        const first = meanDerivative(...args, ...args.map((f, i) => squareDerivative(f, derivatives[i])));
        const m = new Mean(...args);
        const second = squareDerivative(m, m.derivative(...args, ...derivatives));
        return new Subtract(first, second);
    }
);

function parse(expression) {
    const stack = [];
    expression.trim().split(/\s+/u).forEach(tok =>
        stack.push(tok in operators ?
            new operators[tok](...stack.splice(-operators[tok].prototype.func.length)) :
                tok in variables ?
                    variables[tok] :
                        new Const(parseFloat(tok))));
    return stack.pop();
}

function ParserError(message, expression) {
    this.message = `${message} while parsing expression \"${expression}\"`;
}

ParserError.prototype = Object.create(Error.prototype);
ParserError.prototype.name = "ParserError"
ParserError.prototype.constructor = ParserError;

function UnexpectedTokenParserError(token, expression) {
    ParserError.call(this, `Got unexpected token \"${token}\"`, expression);
}
UnexpectedTokenParserError.prototype = Object.create(ParserError.prototype);
UnexpectedTokenParserError.prototype.constructor = UnexpectedTokenParserError;

function UnexpectedArityParserError(arity, operator, numOfOperands, expression) {
    ParserError.call(this, `Expected ${arity} arguments for ${operator} but found ${numOfOperands}`, expression);
}
UnexpectedArityParserError.prototype = Object.create(ParserError.prototype);
UnexpectedArityParserError.prototype.constructor = UnexpectedArityParserError;


function Source(expression) {
    const source = expression.trim().split(/(?=[()])|\s+|(?<=[()])/u);
    if (source[0] === "") {
        source.pop();
    }
    source.push("EOE");
    let index = 0;
    const next = () => source[index++];

    let token = next();
    this.peek = () => token;
    this.isEoE = () => token === "EOE";
    this.next = () => {
        const tok = this.peek()
        if (this.isEoE()) {
            throw new UnexpectedTokenParserError(`end-of-expression`, expression);
        }
        token = next();
        return tok;
    }
}

function parseMod(expression, isPostfix) {
    const source = new Source(expression);
    if (source.isEoE()) {
        throw new UnexpectedTokenParserError("EoE", expression);
    }
    const values = {
        "(": () => parseScoped(source),
        ...Object.fromEntries(variablesNames.map(name => [name, () => variables[name]]))
    }
    const isNumber = (string) => /^(-?\d*)$/.test(string);
    const parseExpression = () => {
        const token = source.next()
        if (isNumber(token)) {
            return new Const(parseFloat(token));
        } else if (token in values) {
            return values[token]();
        }
        throw new UnexpectedTokenParserError(`${token}`, expression);
    }
    const parseScoped = (source) => {
        const parseOperator = () => {
            const token = source.next();
            if (!(token in operators)) {
                throw new UnexpectedTokenParserError(`${token}`, expression);
            }
            return operators[token];
        }

        const parseOperands = () => {
            const operands = [];
            while (!(source.peek() in operators) && source.peek() !== ")" && !source.isEoE()) {
                operands.push(parseExpression());
            }
            return operands;
        }

        const assertArity = (operator, operands) => {
            const arity = operator.prototype.func.length;
            if (arity !== 0 && operands.length !== arity) {
                throw new UnexpectedArityParserError(arity, operator.prototype.literal, operands.length, expression);
            }
            if (source.next() !== ")") {
                throw new UnexpectedTokenParserError(source.peek(), expression);
            }
        }

        if (isPostfix) {
            const parsedOperands = parseOperands();
            const parsedOperator = parseOperator();
            assertArity(parsedOperator, parsedOperands);
            return new parsedOperator(...parsedOperands);
        }
        else {
            const parsedOperator = parseOperator();
            const parsedOperands = parseOperands();
            assertArity(parsedOperator, parsedOperands);
            return new parsedOperator(...parsedOperands);
        }
    }

    const result = parseExpression();
    if (!source.isEoE()) {
        throw new UnexpectedTokenParserError(`${source.next()}`, expression);
    }
    return result
}


const parsePrefix = (expression) => parseMod(expression, false);
const parsePostfix = (expression) => parseMod(expression, true);
