import java.util.Scanner;
import java. util. Arrays;
import java.io.*;
public class ReverseSumMod {
    final static int MOD = 1_000_000_007;

    public static void main(String[] args) throws Exception {
        int i = 0;
        int j = 0;
        Scanner sc = new Scanner(System.in);
        int[][] numbers = new int[1][];
        int[] cur = new int[1];
        while (sc.hasNextLine()) {
            Scanner s = new Scanner(sc.nextLine());
            j = 0;
            while (s.hasNextInt()) {
                if (cur.length == j) {
                    cur = Arrays.copyOf(cur, cur.length * 2);
                }
                cur[j] = s.nextInt();
                //System.err.println(cur[j] + "H");
                j++;
            }
            if (i == numbers.length) {
                numbers = Arrays.copyOf(numbers, numbers.length * 2);
            }
            numbers[i] = Arrays.copyOf(cur, j);
            i++;
            //System.err.println();
        }
        numbers = Arrays.copyOf(numbers, i);
        // rows
        long[] strings = new long[numbers.length];
        int mx = 0;
        for (i = 0; i < numbers.length; i++) {
            mx = Math.max(mx, numbers[i].length);
        }
        // cols
        long[] stolbs = new long[mx];
        for (i = 0; i < numbers.length; i++) {
            for (j = 0; j < numbers[i].length; j++) {
                strings[i] += numbers[i][j];
                stolbs[j] += numbers[i][j];
            }
        }
        for (i = 0; i < numbers.length; i++) {
            for (j = 0; j < numbers[i].length; j++) {
                if (strings[i] + stolbs[j] - numbers[i][j] > 0) {
                    System.out.print((strings[i] + stolbs[j] - numbers[i][j]) % MOD + " ");
                } else {
                    System.out.print((MOD - Math.abs(strings[i] + stolbs[j] - numbers[i][j]) % MOD) % MOD + " ");
                }
                /*if (numbers.length == 2) {
                    System.err.print(numbers[i][j] + " ");
                }*/
            }
            System.out.println();
            //System.err.println();
        }

    }
}