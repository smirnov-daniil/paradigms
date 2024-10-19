const cnst = value => () => value;
const variableAny = variables => name => (...args) =>
    Object.fromEntries(
        variables.map((fieldName, index) => [fieldName, args[index]])
    )[name];
const variable = variableAny(["x", "y", "z"])

const operator = op => (...operands) => (...args) => op(...operands.map(operand => operand(...args)))

const add = operator((...args) => args.reduce((a, b) => a + b, 0))
const subtract = operator((a, b) => a - b)
const multiply = operator((...args) => args.reduce((a, b) => a * b, 1))
const divide = operator((a, b) => a / b)
const negate = operator(a => -a)
const max = operator(Math.max)
const min = operator(Math.min)

const min5 = min;
const max3 = max;

const operators = {
    "+"      : { op: add,      ary: 2},
    "-"      : { op: subtract, ary: 2},
    "*"      : { op: multiply, ary: 2},
    "/"      : { op: divide,   ary: 2},
    "negate" : { op: negate,   ary: 1},
    "min5"   : { op: min,      ary: 5},
    "max3"   : { op: max,      ary: 3},
}

const pi = cnst(Math.PI);
const e = cnst(Math.E);

const abstractParse = consts => variables => expression => {
    const variable = variableAny(variables);

    const stack = [];
    expression.split(" ").filter(e => e !== "").forEach(tok =>
        stack.push(tok in operators ?
            operators[tok].op(...stack.splice(stack.length - operators[tok].ary, operators[tok].ary)) :
                tok in consts ?
                    consts[tok] :
                        variables.includes(tok) ?
                            variable(tok) :
                                cnst(parseInt(tok))));

    return stack.shift();
}
const parse = abstractParse({"pi" : pi, "e"  : e})(["x", "y", "z"]);
