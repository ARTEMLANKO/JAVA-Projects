package expression;

import java.util.Map;

public class UnaryMinus extends UnaryAction {
    public UnaryMinus(Operand operand1) {
        super(operand1);
    }

    public String getSign() {
        return "-";
    }

    public int getpriority() {
        return -100;
    }

    public String toString() {
        return "-(" + operand1.toString() + ")";
    }


    public int calc(int left) {
        return -1 * left;
    }

    public double calc(double left) {
        return -1 * left;
    }

    public int evaluate(int x) {
        return calc(operand1.evaluate(x));
    }

    public double evaluateD(Map<String, Double> dict) {
        return calc(operand1.evaluateD(dict));
    }

    public int evaluate(int x, int y, int z) {
        return calc(operand1.evaluate(x, y, z));
    }

    public boolean isassociative() {
        return true;
    }

    public boolean iscommutative() {
        return true;
    }

    public String toMiniString() {
        //System.err.println("luck " + operand1.getpriority() + " " + getpriority());
        String o1;
        if ((operand1.getpriority() > getpriority() && operand1.getpriority() != -20)) {
            o1 = "-(" + operand1.toMiniString() + ")";
        } else {
            o1 = "- " + operand1.toMiniString();
            //System.err.println(8787878 + " " + o1 + "l " + operand1.toMiniString());
        }
        return o1;
    }
}
