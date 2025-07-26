package expression;

import java.util.Map;

public abstract class Action extends Operand {
    protected Operand operand1;
    protected Operand operand2;

    public Action(Operand operand1, Operand operand2) {
        this.operand1 = operand1;
        this.operand2 = operand2;
    }

    public abstract String getSign();
    public abstract int getpriority();

    public String toString() {
        return "(" + operand1.toString() + " " + getSign() + " " + operand2.toString() + ")";
    }

    public boolean equals(Object oper1) {
        if (oper1 != null && getClass() == oper1.getClass()) {
            Action oper3 = (Action) oper1;
            return this.operand1.equals(oper3.operand1) && this.operand2.equals(oper3.operand2);
        }
        return false;
    }

    public abstract int calc(int left, int right);

    public abstract double calc(double left, double right);

    public int evaluate(int x) {
        return calc(operand1.evaluate(x),  operand2.evaluate(x));
    }

    public double evaluateD(Map<String, Double> dict) {
        return calc(operand1.evaluateD(dict), operand2.evaluateD(dict));
    }

    public int evaluate(int x, int y, int z) {
        return calc(operand1.evaluate(x, y, z), operand2.evaluate(x, y, z));
    }

    public abstract boolean isassociative();
    public abstract boolean iscommutative();

    public String toMiniString() {
        String o1;
        if ((operand1.getpriority() > getpriority())) {
            o1 = "(" + operand1.toMiniString() + ")";
        } else {
            o1 = operand1.toMiniString();
        }
        String o2;
        if ((getpriority() < operand2.getpriority() || (getpriority() == operand2.getpriority() && (!operand2.isassociative() || !iscommutative())))) {
            o2 = "(" + operand2.toMiniString() + ")";
        } else {
            o2 = operand2.toMiniString();
        }
        return o1 + " " + getSign() + " " + o2;
    }
}
