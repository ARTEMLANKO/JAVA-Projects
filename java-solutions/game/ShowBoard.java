package game;
public class ShowBoard {
    private Board board;
    public ShowBoard(Board board) {
        this.board = board;
    }

    public int getN() {
        return board.getN();
    }

    public int getM() {
        return board.getM();
    }

    public boolean isValid(Move move) {
        //System.err.println("THTHTH" + move.getRow() + move.getColumn() + move.getCell());
        return 0 <= move.getRow() && move.getRow() < board.getN()
                && 0 <= move.getColumn() && move.getColumn() < board.getM()
                && (board.getCell(move.getRow(), move.getColumn()) == Cell.E
                || move.getCell() == Cell.L
                || move.getCell() == Cell.D)
                && (move.getCell() == Cell.D || move.getCell() == Cell.L || move.getCell() == board.getCell())
                ;
    }

    public Cell getCell(int n, int m) {
        return board.getCell(n, m);
    }
}
