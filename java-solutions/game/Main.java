package game;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final Game game = new Game(true, new HumanPlayer(), new HumanPlayer());
        int result;
        int n, m, k;
        Scanner in = new Scanner(System.in);
        System.out.println("Введите n, m, k (Если планируете играть на ромбе, введите равные n и m)");
        n = in.nextInt();
        m = in.nextInt();
        k = in.nextInt();
        TicTacToeBoard.n = n;
        TicTacToeBoard.m = m;
        TicTacToeBoard.k = k;
        TriangleBoard.n = n;
        TriangleBoard.m = m;
        TriangleBoard.k = k;
        System.out.println("Выберите поле: ромб - r, прямоугольник - p");
        String typeboard = in.next();
        while (!typeboard.equals("r") && !typeboard.equals("p")) {
            typeboard = in.next();
        }
        UpperGrid uppergrid;
        System.out.println("Турнир (t) или одиночная игра (o)?");
        String is_tourntament = in.next();
        if (is_tourntament.equals("t")) {
            System.out.println("Введите количество игроков");
            int kolvo;
            while (true) {
                try {
                    kolvo = in.nextInt();
                    break;
                } catch (Exception e) {
                    System.out.println("Введите число!");
                }
            }
            if (typeboard.equals("r")) {
                uppergrid = new UpperGrid(1);
            } else {
                uppergrid = new UpperGrid(0);
            }
            for (int i = 0; i < kolvo; i++) {
                uppergrid.add(new HumanPlayer(), i);
            }
            uppergrid.playTourntament();
        } else {
            do {
                if (typeboard.equals("r")) {
                    result = game.play(new TriangleBoard());
                } else {
                    result = game.play(new TicTacToeBoard());
                }
                System.out.println("Game result: " + result);
            } while (result == 0);
        }
    }
}
