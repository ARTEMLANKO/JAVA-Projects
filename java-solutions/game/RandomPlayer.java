package game;
import java.util.Random;

public class RandomPlayer implements Player {
    private final Random random;

    public RandomPlayer(final Random random) {
        this.random = random;
    }

    public RandomPlayer() {
        this(new Random());
    }

    public Move move(final ShowBoard position, final Cell cell) {
        while (true) {
            int r = random.nextInt(position.getN());
            int c = random.nextInt(position.getM());
            final Move move = new Move(r, c, cell);
            if (position.isValid(move)) {
                return move;
            }
        }
    }
}
