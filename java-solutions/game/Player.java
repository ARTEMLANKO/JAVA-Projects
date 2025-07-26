package game;
public interface Player {
    Move move(ShowBoard position, Cell cell) throws Exception;
}
