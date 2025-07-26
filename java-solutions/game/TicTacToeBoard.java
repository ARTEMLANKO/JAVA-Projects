package game;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

//note: abstract class?
public class TicTacToeBoard implements Board {
    private static final Map<Cell, Character> SYMBOLS = Map.of(
            Cell.X, 'X',
            Cell.O, 'O',
            Cell.E, '.'
    );

    private final Cell[][] cells;
    private Cell turn;
    private int empty;
    public static int n, m, k;
    Scanner in;
    boolean draw = false;

    public TicTacToeBoard(int n1, int m1, int k1) {
        this.cells = new Cell[n1][m1];
        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.E);
        }
        turn = Cell.X;
        n = n1;
        m = m1;
        k = k1;
        empty = n * m;
    }

    public TicTacToeBoard() {
        this.cells = new Cell[n][m];
        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.E);
        }
        turn = Cell.X;
        empty = n * m;
        in = new Scanner(System.in);
    }

    public int getN() {
        return this.n;
    }

    public int getM() {
        return this.m;
    }

    private int check(Cell[][] cell, int y, int x, int ans, Move move, int side) {
        //System.out.println(ans);
        if (side == 0 && 0 <= y && y < n && 0 <= x && x < m && cell[y][x] == move.getCell()) {
            return check(cell, y - 1, x - 1, ans + 1, move, 0);
        } else if (side == 1 && 0 <= y && y < n && 0 <= x && x < m && cell[y][x] == move.getCell()) {
            return check(cell, y - 1, x, ans + 1, move, 1);
        } else if (side == 2 && 0 <= y && y < n && 0 <= x && x < m && cell[y][x] == move.getCell()) {
            return check(cell, y - 1, x + 1, ans + 1, move, 2);
        } else if (side == 3 && 0 <= y && y < n && 0 <= x && x < m && cell[y][x] == move.getCell()) {
            return check(cell, y, x + 1, ans + 1, move, 3);
        } else if (side == 4 && 0 <= y && y < n && 0 <= x && x < m && cell[y][x] == move.getCell()) {
            return check(cell, y + 1, x + 1, ans + 1, move, 4);
        } else if (side == 5 && 0 <= y && y < n && 0 <= x && x < m && cell[y][x] == move.getCell()) {
            return check(cell, y + 1, x, ans + 1, move, 5);
        } else if (side == 6 && 0 <= y && y < n && 0 <= x && x < m && cell[y][x] == move.getCell()) {
            return check(cell, y + 1, x - 1, ans + 1, move, 6);
        } else if (side == 7 && 0 <= y && y < n && 0 <= x && x < m && cell[y][x] == move.getCell()) {
            return check(cell, y, x - 1, ans + 1, move, 7);
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
        int ans1 = check(cells, move.getRow() - 1, move.getColumn() - 1, 1, move, 0) +
                check(cells, move.getRow() + 1, move.getColumn() + 1, 1, move, 4) - 1;
        //System.out.println(ans1 + " ggf ");
        int ans2 = check(cells, move.getRow() - 1, move.getColumn(), 1, move, 1) +
                check(cells, move.getRow() + 1, move.getColumn(), 1, move, 5) - 1;
        int ans3 = check(cells, move.getRow() - 1, move.getColumn() + 1, 1, move, 2) +
                check(cells, move.getRow() + 1, move.getColumn() - 1, 1, move, 6) - 1;
        int ans4 = check(cells, move.getRow(), move.getColumn() + 1, 1, move, 3) +
                check(cells, move.getRow(), move.getColumn() - 1, 1, move, 7) - 1;
        //System.out.println(ans1 + " ggf " + ans2 + " " + ans3 + " fff " + ans4);
        ans1 = Math.max(ans1, ans2);
        ans1 = Math.max(ans1, ans3);
        ans1 = Math.max(ans1, ans4);
        //System.err.println(ans1);
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
        System.err.println(move.getCell() + " " + move.getRow() + " " + move.getColumn());
        return 0 <= move.getRow() && move.getRow() < n
                && 0 <= move.getColumn() && move.getColumn() < m
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
        for (int r = 3; r < m; r++) {
            sb.append(r);
        }
        for (int r = 0; r < n; r++) {
            sb.append("\n");
            sb.append(r);
            for (int c = 0; c < m; c++) {
                sb.append(SYMBOLS.get(cells[r][c]));
            }
        }
        return sb.toString();
    }
}
