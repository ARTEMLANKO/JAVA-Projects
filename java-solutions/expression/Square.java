package expression;

public class Square extends UnaryAction {
    public Square (Operand oper1) {
        super(oper1);
    }

    public String getSign() {
        return "²";
    }

    public int getpriority() {
        return -20;
    }

    public boolean isassociative() {
        return true;
    }

    public String toString() {
        return "(" + operand1.toString() + ")²";
    }

    public boolean iscommutative() {
        return false;
    }

    public int calc(int left) {
        return left * left;
    }

    public double calc(double left) {
        return left * left;
    }

    public String toMiniString() {
        String o1;
        if ((operand1.getpriority() > getpriority() || operand1.getClass() == UnaryMinus.class)) {
            o1 = "(" + operand1.toMiniString() + ")²";
        } else {
            o1 = operand1.toMiniString() + "²";
        }
        return o1;
    }
}
