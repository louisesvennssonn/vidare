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
        Deque<String> stack = new ArrayDeque<>();
        double d = 0;
        int countDigits=0;
        int countOperators =0;
        for (String s: postfix){
            if (!(OPERATORS.contains(s)) ){
                stack.push(s);
                d = Double.valueOf(s);
                countDigits++;
            }
            else{
                try{
                    double a = Double.valueOf(stack.pop());
                    double b = Double.valueOf(stack.pop());
                    d = applyOperator(s,a, b);
                    stack.push(Double.toString(d));
                    countOperators++;
                }catch (NoSuchElementException e){
                    throw new IllegalArgumentException(MISSING_OPERAND);
                }
            }
        }missingOperator(countDigits,countOperators);
        return d;
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
        List<String> list = new ArrayList<>();

        for(String s: tokens){
            if(s.equals("(") || s.equals(")") || (OPERATORS.contains(s))){
                if (stack.isEmpty()){
                    stack.push(s);
                } else if(higherpriority(s, stack)){
                    doHigherPriority(s, stack, list);
                }
                else if(samepriority(s,stack)){
                    if (!(tokens.contains("("))) {
                        doSamePriority(list,stack, s);
                    } else {
                        stack.push(s);
                    }
                } else {
                    if (stack.peek().equals(")")){
                        doStackPeek(list,stack);

                    } else{ stack.push(s);
                        if (stack.peek().equals(")")){
                            doStackPeek(list,stack);
                        }
                    }
                }
            } else{
                list.add(s);
            }
        }return   emptystack(stack,list); }


    void missingOperator(int countDigits, int countOperators){

        if((countDigits-countOperators)>1){
            throw new IllegalArgumentException(MISSING_OPERATOR);
        }
    }

    void doSamePriority (List<String> list, Deque<String> stack, String s){

        if(stack.peek().equals("(")){
            String temp= stack.pop();
            list.add((stack.peek()));
            stack.pop();
            stack.push(temp);
            stack.push(s);
        }
        else if ((stack.peek().equals("^")) && (s.equals("^"))){
            stack.push(s);
        }
        else {
            list.add(stack.peek());
            stack.pop();
            stack.push(s);
        }
    }

    void doHigherPriority(String s, Deque<String>stack, List<String>list){

        if( stack.peek().equals("^")){
            for(String k :stack){
                if(stack.peek().equals("^")){
                    list.add(stack.peek());
                    stack.pop();
                }
            }
            stack.push(s);
        }
        else {
            list.add(stack.peek());
            stack.pop();
            if((!samepriority(s,stack)) || (!higherpriority(s,stack))){
                stack.push(s);
            }
        }
    }

    void doStackPeek(List<String> list, Deque<String> stack){
        stack.pop();
        for(int i = stack.size(); i>=0; i--){

            assert stack.peek() != null;
            if ( !(stack.peek().equals("(") )){
                list.add(stack.peek());
                stack.pop();
            }
            else {
                stack.pop();
                break;
            }
        }
    }

    boolean samepriority (String s, Deque<String> stack){
        boolean x = false;

        if (("*/".contains(s))){
            if (stack.contains("*") || stack.contains("/")){
                x = true;
            }
        }
        if (("+-".contains(s))){
            if (stack.contains("+") || stack.contains("-")){
                x = true;
            }
        }        return x;
    }



    boolean higherpriority (String s, Deque<String> stack) {
        boolean x = false;

        if ("+-*/".contains(s)) {
            if (stack.peek().equals("^")){
                x = true;
            }
        }
        if ("+-".contains(s)) {
            if ("*/".contains(stack.peek())){
                x = true;
            }
        }
        return x;
    }

    List<String> emptystack (Deque<String> stack, List<String> list){

        for (int i = 0; i< stack.size(); i++){
            list.add(stack.peek());
            stack.pop();
        }
        if (!stack.isEmpty()){
            list.add(stack.peek());
        }
        return list;
    }


    void missingP(List<String> list){
        int count1=0;
        int count2=0;
        for(String s: list){
            if(s.equals(")")){
                count1++;
            }
            else if ( s.equals("(")){
                count2++;
            }
        }
        if(count1-count2!=0){
            throw new IllegalArgumentException(MISSING_OPERATOR);
        }
    }


    // ---------- Tokenize -----------------------

    public List<String> tokenize(String expr) {
        List<String> list = new ArrayList<>();

        for(int i = 0; i < expr.length(); i++){

            if(((Character.isDigit(expr.toCharArray()[i])) && (i != expr.length()))){
                String sbnumber = checkNext(expr, i);
                i =  (checkI(expr, i));
                list.add(sbnumber);
            }
            else if (!(Character.isDigit(expr.toCharArray()[i])) && (!Character.isWhitespace(expr.toCharArray()[i]))){
                list.add(Character.toString(expr.toCharArray()[i]));
            }
            else if ((Character.isDigit(expr.toCharArray()[i]))){
                list.add(Character.toString(expr.toCharArray()[i]));
            }
        }
        missingP(list);
        return list;
    }

    String checkNext (String expr, int i){
        StringBuilder sb= new StringBuilder();

        for (int b = i; b < expr.length(); b++){
            if ((Character.isDigit(expr.toCharArray()[b]))){
                sb.append(expr.toCharArray()[b]);
            }
            else{
                break;
            }
        }
        String sbnumber = sb.toString();
        return sbnumber;}



    int checkI (String expr, int i){
        int b;
        for ( b = i; b < expr.length(); ){
            if ((Character.isDigit(expr.toCharArray()[b]))){
                b++;
            }
            else{
                b--;
                break;
            }
        }
        return  b;}
}
