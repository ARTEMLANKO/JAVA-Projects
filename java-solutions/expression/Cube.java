package expression;

public class Cube extends UnaryAction {
    public Cube (Operand oper1) {
        super(oper1);
    }

    public String getSign() {
        return "³";
    }

    public int getpriority() {
        return -20;
    }

    public boolean isassociative() {
        return true;
    }

    public String toString() {
        return "(" + operand1.toString() + ")³";
    }

    public boolean iscommutative() {
        return false;
    }

    public int calc(int left) {
        return left * left * left;
    }

    public double calc(double left) {
        return left * left * left;
    }

    public String toMiniString() {
        String o1;
        if ((operand1.getpriority() > getpriority() || operand1.getClass() == UnaryMinus.class)) {
            o1 = "(" + operand1.toMiniString() + ")³";
        } else {
            o1 = operand1.toMiniString() + "³";
        }
        return o1;
    }
}
