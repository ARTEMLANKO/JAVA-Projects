package expression.parser;

import expression.*;

public class ExpressionParser implements TripleParser {
    String s;
    int i;

    public void skipwhitespace() {
        while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
            i++;
        }
    }

    public Operand parse(String s) {
        this.s = s;
        i = 0;
        Operand ans = parseAddSub();
        return ans;
    }

    public Operand parseAddSub() {
        skipwhitespace();
        Operand oper = parseMulDiv();
        while (i < s.length()) {
            skipwhitespace();
            if (i >= s.length()) {
                break;
            }
            if (s.charAt(i) == '+') {
                i++;
                oper = new Add(oper, parseMulDiv());
            } else if (s.charAt(i) == '-') {
                i++;
                oper = new Subtract(oper, parseMulDiv());
            } else {
                return oper;
            }
        }
        skipwhitespace();
        return oper;
    }

    public Operand parseMulDiv() {
        skipwhitespace();
        Operand oper = parsePowLog();
        while (i < s.length()) {
            skipwhitespace();
            if (i >= s.length()) {
                break;
            }
            if (s.charAt(i) == '*') {
                i++;
                oper = new Multiply(oper, parsePowLog());
            } else if (s.charAt(i) == '/') {
                i++;
                oper = new Divide(oper, parsePowLog());
            } else {
                return oper;
            }
        }
        return oper;
    }

    public Operand parsePowLog() {
        skipwhitespace();
        Operand oper = parseUnary();
        while (i < s.length()) {
            skipwhitespace();
            if (i >= s.length()) {
                break;
            }
            if (s.charAt(i) == '*' && i + 1 < s.length() && s.charAt(i + 1) == '*') {
                i += 2;
                oper = new Pow(oper, parseUnary());
            } else if (s.charAt(i) == '/' && i + 1 < s.length() && s.charAt(i + 1) == '/') {
                i += 2;
                oper = new Log(oper, parseUnary());
            } else {
                return oper;
            }
        }
        return oper;
    }

    public Operand parseUnary() {
        skipwhitespace();
        if (String.valueOf(s.charAt(i)).equals("(")) {
            i++;
            Operand ans = parseAddSub();
            i++;
            skipwhitespace();
            while (i < s.length()) {
                skipwhitespace();
                if (i >= s.length()) {
                    break;
                }
                if (s.charAt(i) == '²') {
                    i++;
                    ans = new Square(ans);
                } else if (s.charAt(i) == '³') {
                    i++;
                    ans = new Cube(ans);
                } else {
                    break;
                }
            }
            return ans;
        } else if (s.charAt(i) >= '0' && s.charAt(i) <= '9') {
            return parseConst(1);
        } else if ((s.charAt(i) >= 'a' && s.charAt(i) <= 'z') || (s.charAt(i) >= 'A' && s.charAt(i) <= 'Z')) {
            return parseVariable();
        }
        i++;
        if (!Character.isWhitespace(s.charAt(i))) {
            if (s.charAt(i) >= '0' && s.charAt(i) <= '9') {
                return parseConst(-1);
            }
        }
        skipwhitespace();
        Operand neww = new UnaryMinus(parseUnary());
        return neww;
    }

    public Operand parseConst(int k) {
        StringBuilder left = new StringBuilder();
        skipwhitespace();
        while (i < s.length() && s.charAt(i) >= '0' && s.charAt(i) <= '9') {
            left.append(s.charAt(i));
            i++;
        }
        Operand ans;
        if (left.toString().equals("2147483648")) {
            ans = new Const(-2147483648);
        } else {
            ans = new Const(k * Integer.parseInt(left.toString()));
        }
        skipwhitespace();
        while (i < s.length()) {
            skipwhitespace();
            if (i >= s.length()) {
                break;
            }
            if (s.charAt(i) == '²') {
                i++;
                ans = new Square(ans);
            } else if (s.charAt(i) == '³') {
                i++;
                ans = new Cube(ans);
            } else {
                break;
            }
        }
        return ans;
    }

    public Operand parseVariable() {
        StringBuilder variable = new StringBuilder();
        while (i < s.length() && ((s.charAt(i) >= 'a' && s.charAt(i) <= 'z') || (s.charAt(i) >= 'A' && s.charAt(i) <= 'Z'))) {
            variable.append(s.charAt(i));
            i++;
        }
        skipwhitespace();
        Operand ans = new Variable(variable.toString());
        while (i < s.length()) {
            skipwhitespace();
            if (i >= s.length()) {
                break;
            }
            if (s.charAt(i) == '²') {
                i++;
                ans = new Square(ans);
            } else if (s.charAt(i) == '³') {
                i++;
                ans = new Cube(ans);
            } else {
                break;
            }
        }
        return ans;
    }
}
