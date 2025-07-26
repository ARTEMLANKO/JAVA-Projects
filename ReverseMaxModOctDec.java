import java.io.IOException;
import java.util.Arrays;
import java.util.function.Predicate;

public class ReverseMaxModOctDec {
    final static int MOD = 1_000_000_007;

    public static int mod(final int a) {
        if (a < 0) {
            return (MOD - Math.abs(a) % MOD) % MOD;
        }
        return a % MOD;
    }
    public static void main(final String[] args) {
        int i = 0;
        int j = 0;
        // :NOTE: naming
        // :NOTE: IntPredicate
        final Predicate<Character> predict = s -> Character.isDigit(s) || s == '-' || s == 'O' || s == 'o';
        try {
            final MyReader sc = new MyReader(predict);
            int[][] numbers = new int[1][];
            int[] cur = new int[1];
            while (sc.hasNextLine()) {
                j = 0;
                while (sc.hasNext()) {
                    //System.err.println('s');
                    if (cur.length == j) {
                        cur = Arrays.copyOf(cur, cur.length * 2);
                    }
                    cur[j++] = sc.nextInt();
                }
                if (i == numbers.length) {
                    numbers = Arrays.copyOf(numbers, numbers.length * 2);
                }
                numbers[i++] = Arrays.copyOf(cur, j);
            }
            numbers = Arrays.copyOf(numbers, i);
            final int[] strings = new int[numbers.length];
            int mx = 0;
            for (i = 0; i < numbers.length; i++) {
                mx = Math.max(mx, numbers[i].length);
            }

            final boolean[] rowsX = new boolean[numbers.length];
            final boolean[] colsX = new boolean[mx];
            final int[] stolbs = new int[mx];
            for (i = 0; i < numbers.length; i++) {
                for (j = 0; j < numbers[i].length; j++) {
                    if (!rowsX[i]) {
                        strings[i] = numbers[i][j];
                        rowsX[i] = true;
                    } else {
                        final int c = mod(numbers[i][j]);
                        final int c2 = mod(strings[i]);
                        if (c > c2) {
                            strings[i] = numbers[i][j];
                        }
                    }
                    if (!colsX[j]) {
                        stolbs[j] = numbers[i][j];
                        colsX[j] = true;
                    } else {
                        final int c = mod(numbers[i][j]);
                        final int c2 = mod(stolbs[j]);
                        if (c > c2) {
                            stolbs[j] = numbers[i][j];
                        }
                    }
                }
            }
            for (i = 0; i < numbers.length; i++) {
                for (j = 0; j < numbers[i].length; j++) {
                    final int c = mod(strings[i]);
                    final int c2 = mod(stolbs[j]);
                    if (c > c2) {
                        System.out.print(Integer.toOctalString(strings[i]) + "o ");
                    } else {
                        System.out.print(Integer.toOctalString(stolbs[j]) + "o ");
                    }
                }
                System.out.println();
            }
        } catch (final IOException exp) {
            System.err.println("IOException while working with file, exception type is: " + exp.getClass().getCanonicalName());
        }
    }
}