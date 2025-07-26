package expression;

import java.util.Map;

public class Const extends Operand {
    Number x;
    public Const(int x) {
        this.x = x;
    }

    public Const(double x) {
        this.x = x;
    }

    public int evaluate(int y) {
        return x.intValue();
    }

    public int evaluate(int xx, int yy, int zz) {
        return x.intValue();
    }

    public double evaluateD(Map<String, Double> dict) {
        return x.doubleValue();
    }

    public String toString() {
        return String.valueOf(x);
    }

    public boolean equals(Object oper1) {
        if (oper1 != null && getClass() == oper1.getClass()) {
            Const oper3 = (Const) oper1;
            return x.equals(oper3.x);
        }
        return false;
    }

    public int getpriority() {
        return -100;
    }

    public boolean isassociative() {
        return true;
    }

    public boolean iscommutative() {
        return true;
    }
}
