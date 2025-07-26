package game;
import java.io.PrintStream;
import java.util.Scanner;

public class HumanPlayer implements Player{
    private final PrintStream out;
    private final Scanner in;

    public HumanPlayer(final PrintStream out, final Scanner in) {
        this.out = out;
        this.in = in;
    }

    public HumanPlayer() {
        this(System.out, new Scanner(System.in));
    }

    public Move move(final ShowBoard position, final Cell cell) {
        boolean draw = false;
        while (true) {
            out.println(cell + "'s move");
            out.println("Введите строку и столбец (0-индексация). Если хотите предложить ничью, введите Draw. Если хотите сдаться, введите Lose");
            String next_step = "";
            Move move;
            try {
                move = new Move(in.nextInt(), in.nextInt(), cell);
                if (position.isValid(move)) {
                    return move;
                }
            } catch (NumberFormatException exp) {
                //note: nextstep isn't set
                if (next_step.equals("Draw")) {
                    if (draw) {
                        return new Move(-1, -1, Cell.L);
                    }
                    draw = true;
                    System.out.println("Согласны ли вы на ничью? (Введите Да)");
                    String ans = in.next();
                    if (ans.equals("Да")) {
                        return new Move(-1, -1, Cell.D);
                    } else {
                        continue;
                    }
                } else if (next_step.equals("Lose")) {
                    move = new Move(0, 0, Cell.L);
                    return move;
                }
            }
            out.println("Ход некорректный. Введите ход заново");
        }
    }
}
