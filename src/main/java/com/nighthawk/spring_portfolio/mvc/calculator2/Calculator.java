package com.nighthawk.spring_portfolio.mvc.calculator2;

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
public class Calculator {
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
    public Calculator(String expression) {
        // original input
        this.expression = expression;

        if (this.isBalanced(expression)) {
            // parse expression into terms
            this.termTokenizer();

            this.tokensToReversePolishNotation();

            // calculate reverse polish notation
            this.rpnToResult();
        } else {
            try {
                throw new BadParenthesisException();
            } catch (BadParenthesisException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
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

    private boolean isBalanced(String s) {
        Stack<Character> stack = new Stack<Character>();

        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(c == '[' || c == '(' || c == '{' ) {     
                stack.push(c);
            } else if(c == ']') {
                if(stack.isEmpty() || stack.pop() != '[') {
                    return false;
                }
            } else if(c == ')') {
                if(stack.isEmpty() || stack.pop() != '(') {
                    return false;
                }           
            } else if(c == '}') {
                if(stack.isEmpty() || stack.pop() != '{') {
                    return false;
                }
            }

        }
        return stack.isEmpty();
    }

    // Term Tokenizer takes original expression and converts it to ArrayList of tokens
    private void termTokenizer() {
        
        // contains final list of tokens
        this.tokens = new ArrayList<>();

        int start = 0;  // term split starting index
        StringBuilder multiCharTerm = new StringBuilder();    // term holder
        for (int i = 0; i < this.expression.length(); i++) {
            Character c = this.expression.charAt(i);
            if ( isOperator(c.toString() ) || isSeparator(c.toString())  ) {
                // 1st check for working term and add if it exists
                if (multiCharTerm.length() > 0) {
                    tokens.add(this.expression.substring(start, i));
                }
                // Add operator or parenthesis term to list
                if (c != ' ') {
                    tokens.add(c.toString());
                }
                // Get ready for next term
                start = i + 1;
                multiCharTerm = new StringBuilder();
            } else {
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
                // Pop the two top entries
                double op2 = calcStack.pop();
                double op1 = calcStack.isEmpty() ? 0 : calcStack.pop();
                

                switch (token) {
                    case "+":
                        result = op2 + op1;
                        break;
                    case "-":
                        result = op2 - op1;
                        break;
                    case "*":
                        result = op2 * op1;
                        break;
                    case "/":
                        result = op2 / op1;
                        break;
                    case "%":
                        result = op2 % op1;
                        break;
                    case "^":
                        result = Math.pow(op2, op1);
                        break;
                    case "SQRT":
                        result = Math.sqrt(op2);
                        break;

                    
                }

                // Calculate intermediate results

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

    // Tester method
    public static void main(String[] args) {
        // Random set of test cases
        Calculator simpleMath = new Calculator("100 + 200  * 3");
        System.out.println("Simple Math\n" + simpleMath);

        System.out.println();

        Calculator parenthesisMath = new Calculator("((100 + 200)  * 3");
        System.out.println("Parenthesis Math\n" + parenthesisMath);

        System.out.println();

        Calculator decimalMath = new Calculator("100.2 - 99.3");
        System.out.println("Decimal Math\n" + decimalMath);

        System.out.println();

        Calculator moduloMath = new Calculator("300 % 200");
        System.out.println("Modulo Math\n" + moduloMath);

        System.out.println();

        Calculator divisionMath = new Calculator("300/200");
        System.out.println("Division Math\n" + divisionMath);

        System.out.println();

        Calculator sqrtMath = new Calculator("SQRT (9 * 9)");
        System.out.println("SQRT Math\n" + sqrtMath);


    }   
}