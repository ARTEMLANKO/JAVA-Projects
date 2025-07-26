package game;

import java.util.Map;
import java.util.Scanner;

public class TriangleBoard implements Board {
    private static final Map<Cell, Character> SYMBOLS = Map.of(
            Cell.X, 'X',
            Cell.O, 'O',
            Cell.E, '.',
            Cell.NON, ' '
    );

    private final Cell[][] cells;
    private Cell turn;
    private int empty;
    public static int n, m, k;
    boolean draw = false;
    Scanner in = new Scanner(System.in);
    public TriangleBoard(int n1, int m1, int k1) {
        int N = n1*2 - 1;
        this.cells = new Cell[n1*2 - 1][m1 * 2 - 1];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i + j < n1 && N- j + i < n1 && N - i + j < n1 && N - i + N - j < n1) {
                    cells[i][j] = Cell.E;
                } else {
                    cells[i][j] = Cell.NON;
                }
            }
        }
        turn = Cell.X;
        n = n1;
        m = m1;
        k = k1;
        empty = (2 * n -1) * (2 * m - 1) - 4 * (1 + n - 1) * (n-1) / 2;
    }

    public TriangleBoard() {
        int N = n*2 - 1;
        this.cells = new Cell[n*2 - 1][m * 2 - 1];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i + j < n-1 || N- j - 1+ i < n-1 || N - i - 1 + j < n-1 || N - i + N - j - 2 < n-1) {
                    cells[i][j] = Cell.NON;
                } else {
                    cells[i][j] = Cell.E;
                }
            }
        }
        turn = Cell.X;
        empty = (2 * n -1) * (2 * m - 1) - 4 * (1 + n - 1) * (n-1) / 2;
        //System.err.println("aaaaa " + empty);
    }
    public int getN() {
        return 2 * n -1;
    }

    public int getM() {
        return 2 * m - 1;
    }

    private int check(Cell[][] cell, int y, int x, int ans, Move move, int side) {
        //System.out.println(ans);
        if (side == 0 && 0 <= y && y < n && 0 <= x && x < m && cell[y][x] == move.getCell()) {
            return check(cell, y - 1, x - 1, ans+1, move, 0);
        } else if (side == 1 && 0 <= y && y < n && 0 <= x && x < m && cell[y][x] == move.getCell()) {
            return check(cell, y - 1, x, ans+1, move, 1);
        } else if (side == 2 && 0 <= y && y < n && 0 <= x && x < m && cell[y][x] == move.getCell()) {
            //System.err.println(ans);
            return check(cell, y - 1, x + 1, ans+1, move, 2);
        } else if (side == 3 && 0 <= y && y < n && 0 <= x && x < m && cell[y][x] == move.getCell()) {
            return check(cell, y, x + 1, ans+1, move, 3);
        } else if (side == 4 && 0 <= y && y < n && 0 <= x && x < m && cell[y][x] == move.getCell()) {
            return check(cell, y + 1, x + 1, ans+1, move, 4);
        } else if (side == 5 && 0 <= y && y < n && 0 <= x && x < m && cell[y][x] == move.getCell()) {
            return check(cell, y + 1, x, ans+1, move, 5);
        } else if (side == 6 && 0 <= y && y < n && 0 <= x && x < m && cell[y][x] == move.getCell()) {
            //System.err.println(ans);
            return check(cell, y + 1, x - 1, ans+1, move, 6);
        } else if (side == 7 && 0 <= y && y < n && 0 <= x && x < m && cell[y][x] == move.getCell()) {
            return check(cell, y, x - 1, ans+1, move, 7);
        }
        return ans;
    }

    @Override
    public ShowBoard getPosition() {
        return new ShowBoard(this);
    }

    @Override
    public Cell getCell() {
        return turn;
    }

    @Override
    public Result makeMove(final Move move) {
        System.out.println(move);
        if (move.getCell() == Cell.D) {
            /*if (draw) {
                return Result.LOSE;
            }
            draw = true;*/
            return Result.DRAW;
        } else if (move.getCell() == Cell.L) {
            draw = false;
            return Result.LOSE;
        }
        draw = false;
        //System.out.println(move.getRow() + " " + move.getColumn() + " aaaaa");
        empty--;
        cells[move.getRow()][move.getColumn()] = move.getCell();
        /*for (Cell[] i : cells) {
            for (Cell j : i) {
                System.out.print(j);
            }
            System.out.println();
        }*/
        int ans1 = check(cells, move.getRow()-1, move.getColumn()-1, 1, move, 0) +
                check(cells, move.getRow()+1, move.getColumn()+1, 1, move, 4) - 1;
        //System.out.println(ans1 + " ggf ");
        int ans2 = check(cells, move.getRow()-1, move.getColumn(), 1, move, 1) +
                check(cells, move.getRow()+1, move.getColumn(), 1, move, 5) - 1;
        int ans3 = check(cells, move.getRow()-1, move.getColumn() + 1, 1, move, 2) +
                check(cells, move.getRow()+1, move.getColumn() - 1, 1, move, 6) - 1;
        int ans4 = check(cells, move.getRow(), move.getColumn() + 1, 1, move, 3) +
                check(cells, move.getRow(), move.getColumn() - 1, 1, move, 7) - 1;
        System.err.println(ans3 + " " +  k);
        //System.out.println(ans1 + " ggf " + ans2 + " " + ans3 + " fff " + ans4);
        ans1 = Math.max(ans1, ans2);
        ans1 = Math.max(ans1, ans3);
        ans1 = Math.max(ans1, ans4);
        //System.err.println(empty);
        if (ans1 >= k) {
            return Result.WIN;
        }
        if (empty == 0) {
            return Result.DRAW;
        }
        turn = turn == Cell.X ? Cell.O : Cell.X;
        return Result.UNKNOWN;
    }

    //@Override
    public boolean isValid(final Move move) {
        //System.err.println("THTHTH" + move.getRow() + move.getColumn() + move.getCell());
        return 0 <= move.getRow() && move.getRow() < 2 * n -1
                && 0 <= move.getColumn() && move.getColumn() < 2 * n -1
                && (cells[move.getRow()][move.getColumn()] == Cell.E
                || move.getCell() == Cell.L
                || move.getCell() == Cell.D)
                && (move.getCell() == Cell.D || move.getCell() == Cell.L || move.getCell() == getCell())
                ;
    }
    @Override
    public Cell getCell(final int r, final int c) {
        return cells[r][c];
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(" 012");
        for (int r = 3; r < 2 * n - 1; r++) {
            sb.append(r);
        }
        for (int r = 0; r < 2 * n - 1; r++) {
            sb.append("\n");
            sb.append(r);
            for (int c = 0; c < 2 * m - 1; c++) {
                sb.append(SYMBOLS.get(cells[r][c]));
            }
        }
        return sb.toString();
    }
}
