package com.nighthawk.spring_portfolio.mvc.frqs.calculator1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/* In mathematics,
    an expression or mathematical expression is a finite combination of symbols that is well-formed
    according to rules that depend on the context.
   In computers,
    expression can be hard to calculate with precedence rules and user input errors
    to handle computer math we often convert strings into reverse polish notation
    to handle errors we perform try / catch or set default conditions to trap errors
     */
public class Calculator1 {
    // Key instance variables
    private final String expression;
    private ArrayList<String> tokens;
    private ArrayList<String> reverse_polish;
    private Double result = 0.0;

    // Helper definition for supported operators
    private final Map<String, Integer> OPERATORS = new HashMap<>();
    {
        // Map<"token", precedence>
        OPERATORS.put("SQRT", 1);
        OPERATORS.put("NORM", 1);
        OPERATORS.put("^", 2);
        OPERATORS.put("*", 3);
        OPERATORS.put("/", 3);
        OPERATORS.put("%", 3);
        OPERATORS.put("+", 4);
        OPERATORS.put("-", 4);
    }

    // Helper definition for supported operators
    private final Map<String, Integer> SEPARATORS = new HashMap<>();
    {
        // Map<"separator", not_used>
        SEPARATORS.put(" ", 0);
        SEPARATORS.put("(", 0);
        SEPARATORS.put(")", 0);
    }

    // Create a 1 argument constructor expecting a mathematical expression
    public Calculator1(String expression) throws BadParenthesisException {
        // original input
        this.expression = expression;

        // parse expression into terms
        this.termTokenizer();

        // place terms into reverse polish notation
        this.tokensToReversePolishNotation();

        // calculate reverse polish notation
        this.rpnToResult();
    }

    // Test if token is an operator
    private boolean isOperator(String token) {
        // find the token in the hash map
        return OPERATORS.containsKey(token);
    }

    // Test if token is an separator
    private boolean isSeparator(String token) {
        // find the token in the hash map
        return SEPARATORS.containsKey(token);
    }

    // Compare precedence of operators.
    private Boolean isPrecedent(String token1, String token2) {
        // token 1 is precedent if it is greater than token 2
        return (OPERATORS.get(token1) - OPERATORS.get(token2) >= 0) ;
    }

    // Term Tokenizer takes original expression and converts it to ArrayList of tokens
    private void termTokenizer() throws BadParenthesisException {
        DelimeterFRQ1 frq = new DelimeterFRQ1('(', ')');
        if (frq.isBalanced(frq.getDelimitersList(this.expression))) {
        }
        else {
            throw new BadParenthesisException();
        }
        
        // contains final list of tokens
        this.tokens = new ArrayList<>();

        int start = 0;  // term split starting index
        StringBuilder multiCharTerm = new StringBuilder();    // term holder
        for (int i = 0; i < this.expression.length(); i++) {
            Character c = this.expression.charAt(i);
            if ( isOperator(c.toString() ) || isSeparator(c.toString())  ) {
                // 1st check for working term and add if it exists
                if (multiCharTerm.length() > 0) {
                    // SPECIAL CHARACTER "n" used as a negative sign
                    tokens.add(this.expression.substring(start, i).replace("n", "-"));
                }
                // Add operator or parenthesis term to list
                if (c != ' ') {
                    tokens.add(c.toString());
                }
                // Get ready for next term
                start = i + 1;
                multiCharTerm = new StringBuilder();
            } 
            // special case for 4 character functions
            else if (i + 3 < this.expression.length() && isOperator(this.expression.substring(i, i + 4))) {
                // 1st check for working term and add if it exists
                if (multiCharTerm.length() > 0) {
                    tokens.add(this.expression.substring(start, i));
                }
                // Add SQRT operator to list
                tokens.add(this.expression.substring(i, i + 4));
                // Get ready for next term
                start = i + 4;
                multiCharTerm = new StringBuilder();
                i += 3;
            }
            else {
                // multi character terms: numbers, functions, perhaps non-supported elements
                // Add next character to working term
                multiCharTerm.append(c);
            }

        }
        // Add last term
        if (multiCharTerm.length() > 0) {
            tokens.add(this.expression.substring(start));
        }
    }

    // Takes tokens and converts to Reverse Polish Notation (RPN), this is one where the operator follows its operands.
    private void tokensToReversePolishNotation () {
        // contains final list of tokens in RPN
        this.reverse_polish = new ArrayList<>();

        // stack is used to reorder for appropriate grouping and precedence
        Stack<String> tokenStack = new Stack<String>();
        for (String token : tokens) {
            switch (token) {
                // If left bracket push token on to stack
                case "(":
                    tokenStack.push(token);
                    break;
                case ")":
                    while (tokenStack.peek() != null && !tokenStack.peek().equals("("))
                    {
                        reverse_polish.add( tokenStack.pop() );
                    }
                    tokenStack.pop();
                    break;
                case "+":
                case "-":
                case "*":
                case "/":
                case "%":
                case "^":
                case "SQRT":
                case "NORM":
                    // While stack
                    // not empty AND stack top element
                    // and is an operator
                    while (tokenStack.size() > 0 && isOperator(tokenStack.peek()))
                    {
                        if ( isPrecedent(token, tokenStack.peek() )) {
                            reverse_polish.add(tokenStack.pop());
                            continue;
                        }
                        break;
                    }
                    // Push the new operator on the stack
                    tokenStack.push(token);
                    break;
                default:    // Default should be a number, there could be test here
                    this.reverse_polish.add(token);
            }
        }
        // Empty remaining tokens
        while (tokenStack.size() > 0) {
            reverse_polish.add(tokenStack.pop());
        }

    }

    double normConst = 1/Math.sqrt(2*Math.PI);
    double[] taylorSeries = {normConst/1, -normConst/6, normConst/40, -normConst/336, normConst/3456, -normConst/42240, normConst/599040};
    double getAreaUnderNormalCurve(double z) {
        double ans = 0.5;
        for (int i = 0; i<taylorSeries.length; i++) {
            ans += Math.pow(z, 2*i+1) * taylorSeries[i];
        }
        return ans;
    }
    
    // Calculates result from two numbers and an operator
    private double resolve (Stack<Double> stack, String operator) {
        switch (operator) {
            case "+":
                return stack.pop() + stack.pop();
            case "-":
                return -stack.pop() + stack.pop();
            case "*":
                return stack.pop() * stack.pop();
            case "/":
                return 1/stack.pop() * stack.pop();
            case "%":
                double divisor = stack.pop();
                return stack.pop() % divisor;
            case "^":
                double exp = stack.pop();
                return Math.pow(stack.pop(), exp);
            case "SQRT":
                return Math.sqrt(stack.pop());
            case "NORM":
                return getAreaUnderNormalCurve(stack.pop());
            default:
                return 0;
        }   
    }

    // Takes RPN and produces a final result
    private void rpnToResult()
    {
        // stack is used to hold operands and each calculation
        Stack<Double> calcStack = new Stack<Double>();

        // RPN is processed, ultimately calcStack has final result
        for (String token : this.reverse_polish)
        {
            // If the token is an operator, calculate
            if (isOperator(token))
            {
                // Calculate intermediate results
                result = resolve(calcStack, token);

                // Push intermediate result back onto the stack
                calcStack.push( result );
            }
            // else the token is a number push it onto the stack
            else
            {
                calcStack.push(Double.valueOf(token));
            }
        }
        // Pop final result and set as final result for expression
        this.result = calcStack.pop();
    }

    // Print the expression, terms, and result
    public String toString() {
        return ("Original expression: " + this.expression + "\n" +
                "Tokenized expression: " + this.tokens.toString() + "\n" +
                "Reverse Polish Notation: " +this.reverse_polish.toString() + "\n" +
                "Final result: " + String.format("%.2f", this.result));
    }

    // result getter
    public double getResult() {
      return this.result;
    }

    // Tester method
    public static void main(String[] args) throws BadParenthesisException {
        // Random set of test cases
        Calculator1 simpleMath = new Calculator1("100 + 200  * 3");
        System.out.println("Simple Math\n" + simpleMath);

        System.out.println();

        Calculator1 parenthesisMath = new Calculator1("(100 + 200)  * 3");
        System.out.println("Parenthesis Math\n" + parenthesisMath);

        System.out.println();

        Calculator1 decimalMath = new Calculator1("100.2 - 99.3");
        System.out.println("Decimal Math\n" + decimalMath);

        System.out.println();

        Calculator1 moduloMath = new Calculator1("300 % 200");
        System.out.println("Modulo Math\n" + moduloMath);

        System.out.println();

        Calculator1 divisionMath = new Calculator1("300/200");
        System.out.println("Division Math\n" + divisionMath);

        System.out.println();

        Calculator1 powerMath = new Calculator1("2 ^ 3 + 1");
        System.out.println("Power Math\n" + powerMath);

        System.out.println();

        Calculator1 squareRootMath = new Calculator1("3 + 5 * SQRT(81)");
        System.out.println("Square Root Math\n" + squareRootMath);

        System.out.println();

        Calculator1 normalMath = new Calculator1("NORM(1) - NORM(n1)");
        System.out.println("Normal Math\n" + normalMath);
    }
}
