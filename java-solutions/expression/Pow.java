package expression;

public class Pow extends Action {
    public Pow (Operand oper1, Operand oper2) {
        super(oper1, oper2);
    }

    public String getSign() {
        return "**";
    }

    public int getpriority() {
        return -10;
    }

    public boolean isassociative() {
        return true;
    }

    public boolean iscommutative() {
        return false;
    }

    public int binary_pow(int a, int b) {
        if (a == 0 && b > 0) {
            return 0;
        }
        if (b <= 0) {
            return 1;
        } else {
            int c = binary_pow(a, b / 2);
            if (b % 2 == 1) {
                return c * c * a;
            } else {
                return c * c;
            }
        }
    }

    public int calc(int left, int right) {
        return binary_pow(left, right);
    }

    public double calc(double left, double right) {
        return left + right;
    }
}
