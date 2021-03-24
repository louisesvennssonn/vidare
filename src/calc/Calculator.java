package calc;

import java.util.*;

import static java.lang.Double.NaN;
import static java.lang.Math.pow;


/*
 *   A calculator for rather simple arithmetic expressions
 *
 *   This is not the program, it's a class declaration (with methods) in it's
 *   own file (which must be named Calculator.java)
 *
 *   NOTE:
 *   - No negative numbers implemented
 */
class Calculator {

    // Here are the only allowed instance variables!
    // Error messages (more on static later)
    final static String MISSING_OPERAND = "Missing or bad operand";
    final static String DIV_BY_ZERO = "Division with 0";
    final static String MISSING_OPERATOR = "Missing operator or parenthesis";
    final static String OP_NOT_FOUND = "Operator not found";

    // Definition of operators
    final static String OPERATORS = "+-*/^";

    // Method used in REPL
    double eval(String expr) {
        if (expr.length() == 0) {
            return NaN;
        }
        List<String> tokens = tokenize(expr);
        List<String> postfix = infix2Postfix(tokens);
        return evalPostfix(postfix);
    }

    // ------  Evaluate RPN expression -------------------

    public double evalPostfix(List<String> postfix) {
        return 0;  // TODO
    }


    double applyOperator(String op, double d1, double d2) {
        switch (op) {
            case "+":
                return d1 + d2;
            case "-":
                return d2 - d1;
            case "*":
                return d1 * d2;
            case "/":
                if (d1 == 0) {
                    throw new IllegalArgumentException(DIV_BY_ZERO);
                }
                return d2 / d1;
            case "^":
                return pow(d2, d1);
        }
        throw new RuntimeException(OP_NOT_FOUND);
    }

    // ------- Infix 2 Postfix ------------------------

    public List<String> infix2Postfix(List<String> tokens) {
        Deque<String> stack = new ArrayDeque<>();
        List<String> postfix = new ArrayList<>();

        for (String str : tokens) {
            if (!(OPERATORS.contains(str))) {
                postfix.add(str);
            }
            else if(stack.isEmpty()|| str.equals("(")|| stack.peek().equals("(")){
                stack.push(str);
            }
            else if (str.equals(")")&& stack.contains("(")) {
                postfix.add(closing(str, stack));
            }
            else if ((OPERATORS.contains(str))) {

                if (getPrecedence(stack.peek()) > getPrecedence(str)) {
                    postfix.add(sortOperator(str, stack));
                }
                else if (getPrecedence(stack.peek()) < getPrecedence(str)) {
                    stack.push(str);
                }
                else if ((getPrecedence(stack.peek()) == getPrecedence(str)) && (getAssociativity(stack.peek()) == Assoc.LEFT)) {
                    postfix.add(stack.pop());
                    stack.push(str);
                }
            }
        }
        if(!stack.isEmpty())
            postfix.add(stack.pop());
        return postfix;

    }

    void missingOperator(int countDigits, int countOperators){

        if((countDigits-countOperators)>1){
            throw new IllegalArgumentException(MISSING_OPERATOR);
        }
    }
    String closing(String str, Deque<String> stack) {
        String result = new String();
        while(!stack.peek().equals("(")){
            result += stack.peek();
            stack.pop();
            if(stack.isEmpty()) {
                break;
            }
        }
        stack.pop();
        return result;
    }

    String sortOperator(String str, Deque<String> stack) {
        String result = new String();
        while (!(getPrecedence(stack.peek()) == (getPrecedence(str)))) {
            result += stack.pop();
            if(stack.isEmpty()) {
                break;
            }
        }

        return result;
    }

    int getPrecedence(String op) {
        if ("+-".contains(op)) {
            return 2;
        } else if ("*/".contains(op)) {
            return 3;
        } else if ("^".contains(op)) {
            return 4;
        } else {
            throw new RuntimeException(OP_NOT_FOUND);
        }
    }

    Assoc getAssociativity(String op) {
        if ("+-*/".contains(op)) {
            return Assoc.LEFT;
        } else if ("^".contains(op)) {
            return Assoc.RIGHT;
        } else {
            throw new RuntimeException(OP_NOT_FOUND);
        }
    }

    enum Assoc {
        LEFT,
        RIGHT
    }

    // ---------- Tokenize -----------------------


    public List<String> tokenize(String expr) {

        List<String> tokens = new ArrayList<>();
        expr = expr.replaceAll("\\s", "");

        for (int i = 0; i < expr.length(); i++) {
            char index = expr.charAt(i);
            boolean isOperator = OPERATORS.contains(Character.toString(index));
            if (isOperator) {
                tokens.add(String.valueOf(index));
            } else if (index == '(' || index == ')') { // ev fel fungerar med "å" hehee.
                tokens.add(String.valueOf(index));
            } else if (Character.isDigit(index)) {
                i = extractNumbers(expr, i, tokens) - 1;
            }
        }
        System.out.println(tokens);
        return tokens;

    }

    int extractNumbers(String expr, int i, List<String> tokens) {
        StringBuilder sb = new StringBuilder();
        // char index = expr.charAt(i);
        while (i < expr.length() && Character.isDigit(expr.charAt(i))) {
            char index = expr.charAt(i);
            sb.append(index);
            i++;
        }
        tokens.add(sb.toString());
        return i;
    }
