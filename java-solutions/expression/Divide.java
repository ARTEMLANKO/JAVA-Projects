package expression;

public class Divide extends Action {
    public Divide(Operand operand1, Operand operand2) {
        super(operand1, operand2);
    }

    public String getSign() {
        return "/";
    }

    public int calc(int left, int right) {
        return left / right;
    }

    public double calc(double left, double right) {
        return left / right;
    }

    public int getpriority() {
        return 1;
    }

    public boolean isassociative() {
        return false;
    }

    public boolean iscommutative() {
        return false;
    }
}
