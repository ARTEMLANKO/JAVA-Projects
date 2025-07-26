package expression;

import java.util.Map;

public class Add extends Action {
    public Add(Operand operand1, Operand operand2) {
        super(operand1, operand2);
    }

    public String getSign() {
        return "+";
    }

    public int getpriority() {
        return 2;
    }

    public boolean isassociative() {
        return true;
    }

    public boolean iscommutative() {
        return true;
    }

    public int calc(int left, int right) {
        return left + right;
    }

    public double calc(double left, double right) {
        return left + right;
    }
}
