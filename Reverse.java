

import java.util.Scanner;
import java. util. Arrays;
import java.util.function.Predicate;

public class Reverse {
    public static void main(String[] args) throws Exception {
        int i = 0;
        int j = 0;
        Predicate<Character> predict = s -> ((Character.isDigit(s)) || s == '-');
        MyReader sc = new MyReader(predict);
        int[][] numbers = new int[1][];
        int[] cur = new int[1];
        while (sc.hasNextLine()) {
            //System.err.println("kl");
            j = 0;
            while (sc.hasNext()) {
                if (cur.length == j) {
                    cur = Arrays.copyOf(cur, cur.length * 2);
                }
                cur[j] = Integer.parseInt(sc.next());
                j++;
            }
            if (i == numbers.length) {
                numbers = Arrays.copyOf(numbers, numbers.length * 2);
            }
            numbers[i] = Arrays.copyOf(cur, j);
            i++;
        }
        numbers = Arrays.copyOf(numbers, i);
        for (i = numbers.length - 1; i >= 0; i--) {
            for (j = numbers[i].length - 1; j >= 0; j--) {
                System.out.print(numbers[i][j] + " ");
            }
            System.out.println();
        }
    }
}