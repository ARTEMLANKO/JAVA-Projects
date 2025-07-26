package game;
import java.util.Random;
class Game {
    private static final Random random = new Random();
    private final boolean log;
    private final Player player1, player2;
    private final int order;
    public Game(final boolean log, final Player player1, final Player player2) {
        this.log = log;
        this.player1 = player1;
        this.player2 = player2;
        int r = random.nextInt(2);
        if (r == 0) {
            this.order = 0;
        } else {
            this.order = 1;
        }
    }

    public int play(Board board) {
        if (order == 0) {
            System.out.println("Первый игрок ходит первым");
        } else {
            System.out.println("Второй игрок ходит первым");
        }
        while (true) {
            final int result1 = move(board, player1, 1);
            if (result1 != -1) {
                return result1;
            }
            final int result2 = move(board, player2, 2);
            if (result2 != -1) {
                return result2;
            }
        }
    }

    private int move(final Board board, final Player player, int no) {
        Move move;
        Result result;
        try {
            move = player.move(board.getPosition(), board.getCell());
            result = board.makeMove(move);
        } catch (Exception exp) {
            result = Result.LOSE;
            move = new Move(-1, -1, Cell.L);
        }
        if (order == 1) {
            no = 3 - no;
        }
        log("Player " + no + " move: " + move);
        log("Position:\n" + board);
        if (result == Result.WIN) {
            log("Player " + no + " won");
            return no;
        } else if (result == Result.LOSE) {
            log("Player " + no + " lose");
            return 3 - no;
        } else if (result == Result.DRAW) {
            log("Draw");
            return 0;
        } else {
            return -1;
        }
    }

    private void log(final String message) {
        if (log) {
            System.out.println(message);
        }
    }
}
