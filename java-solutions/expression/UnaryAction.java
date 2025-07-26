package expression;

import java.util.Map;

public abstract class UnaryAction extends Operand {
    protected Operand operand1;

    public UnaryAction(Operand operand1) {
        this.operand1 = operand1;
    }

    public abstract String getSign();
    public abstract int getpriority();

    public abstract String toString();

    public boolean equals(Object oper1) {
        if (oper1 != null && getClass() == oper1.getClass()) {
            Action oper3 = (Action) oper1;
            return this.operand1.equals(oper3.operand1);
        }
        return false;
    }

    public abstract int calc(int left);

    public abstract double calc(double left);

    public int evaluate(int x) {
        return calc(operand1.evaluate(x));
    }

    public double evaluateD(Map<String, Double> dict) {
        return calc(operand1.evaluateD(dict));
    }

    public int evaluate(int x, int y, int z) {
        return calc(operand1.evaluate(x, y, z));
    }


    public abstract String toMiniString();
}
