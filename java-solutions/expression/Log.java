package expression;

public class Log extends Action {
    public Log (Operand oper1, Operand oper2) {
        super(oper1, oper2);
    }

    public String getSign() {
        return "//";
    }

    public int getpriority() {
        return -10;
    }

    public boolean isassociative() {
        return false;
    }

    public boolean iscommutative() {
        return false;
    }

    public int count_log(int a, int b) {
        if (a == 1 && b == 1) {
            return 0;
        }
        if (a == 0 && b > 0) {
            return -2147483648;
        }
        if (a < 0 || b <= 0) {
            return 0;
        }
        if (a > 1 && b == 1) {
            return 2147483647;
        }
        if (a == 1 || a < b) {
            return 0;
        }
        int ans = 0;
        int tek = 1;
        while (tek * b <= a && 2147483647 / tek >= b) {
            tek *= b;
            ans++;
        }
        return ans;
    }

    public int calc(int left, int right) {
        return count_log(left, right);
    }

    public double calc(double left, double right) {
        return left + right;
    }
}
