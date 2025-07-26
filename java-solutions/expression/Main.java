package expression;

import expression.parser.ExpressionParser;

public class Main {
    public static void main(String[] args) {
        ExpressionParser parserr = new ExpressionParser();
        System.out.print(parserr.parse("(-(-992389902))²").toMiniString());
    }
}