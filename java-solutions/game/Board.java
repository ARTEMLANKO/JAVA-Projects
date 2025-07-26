package game;
public interface Board {
    ShowBoard getPosition();
    Cell getCell();
    Result makeMove(Move move);
    int getN();
    int getM();
    Cell getCell(int i, int j);
}
