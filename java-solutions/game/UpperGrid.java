package game;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Collections;

public class UpperGrid {
    //note: make a class instead ob object[2]
    private List<Object[]> grid = new ArrayList<>();
    private List<Object[]> lowgrid = new ArrayList<>();
    boolean t = false;
    public UpperGrid(final Random random, int board) {
        if (board == 1) {
            t = true;
        }
    }

    public UpperGrid(int board) {
        this(new Random(), board);
    }

    public void add(Player player1, int number) {
        Object[] object = new Object[2];
        object[0] = player1;
        object[1] = number;
        grid.add(object);
    }

    public void playTourntament() {
        List<List<Integer>> order = new ArrayList<List<Integer>>();
        int r = 0;
        while (grid.size() > 1 || lowgrid.size() > 1) {
            if (r >= 1) {
                order.add(new ArrayList<Integer>());
            }
            r += 1;
            List<Integer> list1 = new ArrayList<Integer>();
            for (int ii = 0; ii < lowgrid.size(); ii++) {
                list1.add(ii);
            }
            Collections.shuffle(list1);
            List<Object[]> newlist = new ArrayList<Object[]>();
            System.out.println(r + " round");
            for (int ii = 0; ii < lowgrid.size() / 2; ii++) {
                System.out.println(lowgrid.get(list1.get(ii * 2))[1] + " player vs " + lowgrid.get(list1.get(ii * 2 + 1))[1] + " player");
                //note: copypaste
                final Game game = new Game(true, (Player) (lowgrid.get(list1.get(ii * 2)))[0], (Player) (lowgrid.get(list1.get(ii * 2 + 1)))[0]);
                int result;
                do {
                    if (!t) {
                        result = game.play(new TicTacToeBoard());
                    } else {
                        result = game.play(new TriangleBoard());
                    }
                    //System.out.println("Game result: " + result);
                } while (result == 0);
                //System.out.println(result);
                if (result == 1) {
                    newlist.add(lowgrid.get(list1.get(ii * 2)));
                    System.out.println(lowgrid.get(list1.get(ii * 2))[1] + "player win");
                    order.get(order.size()-1).add((Integer) lowgrid.get(list1.get(ii * 2 + 1))[1]);
                } else {
                    newlist.add(lowgrid.get(list1.get(ii * 2 + 1)));
                    System.out.println(lowgrid.get(list1.get(ii * 2 + 1))[1] + " player win");
                    order.get(order.size()-1).add((Integer) lowgrid.get(list1.get(ii * 2))[1]);
                }
            }
            if (lowgrid.size() % 2 == 1) {
                newlist.add(lowgrid.get(list1.get(lowgrid.size() - 1)));
            }
            lowgrid = new ArrayList<Object[]>();
            for (Object[] i : newlist) {
                lowgrid.add(i);
            }
            newlist = new ArrayList<Object[]>();
            List<Integer> list = new ArrayList<Integer>();
            for (int ii = 0; ii < grid.size(); ii++) {
                list.add(ii);
            }
            Collections.shuffle(list);
            for (int ii = 0; ii < grid.size() / 2; ii++) {
                System.out.println(grid.get(list.get(ii * 2))[1] + " player vs  " + grid.get(list.get(ii * 2 + 1))[1] + " player");
                final Game game = new Game(true, (Player) (grid.get(list.get(ii * 2)))[0], (Player) (grid.get(list.get(ii * 2 + 1)))[0]);
                int result;
                do {
                    if (!t) {
                        result = game.play(new TicTacToeBoard());
                        //System.out.println("Game result: " + result);
                    } else {
                        result = game.play(new TriangleBoard());
                        //System.out.println("Game result: " + result);
                }
                } while (result == 0);
                //System.out.println(result);
                if (result == 1) {
                    newlist.add(grid.get(list.get(ii * 2)));
                    lowgrid.add(grid.get(list.get(ii * 2 + 1)));
                    System.out.println(grid.get(list.get(ii * 2))[1] + " player win");
                } else {
                    newlist.add(grid.get(list.get(ii * 2 + 1)));
                    lowgrid.add(grid.get(list.get(ii * 2)));
                    System.out.println(grid.get(list.get(ii * 2 + 1))[1] + " player win");
                }
            }
            if (grid.size() % 2 == 1) {
                newlist.add(grid.get(list.get(grid.size() - 1)));
            }
            grid = new ArrayList<Object[]>();
            for (Object[] i : newlist) {
                grid.add(i);
            }
            System.out.println("high grid:");
            for (Object[] i : grid) {
                System.out.print(i[1] + " ");
            }
            System.out.println();
            System.out.println("low grid:");
            for (Object[] i : lowgrid) {
                System.out.print(i[1] + " ");
            }
            System.out.println();
        }
        //******
        final Game game = new Game(true, (Player) (grid.get(0)[0]), (Player) (lowgrid.get(0)[0]));
        int result;
        do {
            if (!t) {
                System.out.println(grid.get(0)[1] + " player versus " + lowgrid.get(0)[1]);
                result = game.play(new TicTacToeBoard());
                System.out.println("Game result: " + result);
            } else {
                result = game.play(new TriangleBoard());
                System.out.println("Game result: " + result);
            }
        } while (result == 0);
        //System.out.println(result);
        //note: copypaste
        if (result == 1) {
            order.add(new ArrayList<Integer>());
            order.get(order.size()-1).add((Integer) lowgrid.get(0)[1]);
            order.add(new ArrayList<Integer>());
            order.get(order.size()-1).add((Integer) grid.get(0)[1]);
        } else {
            order.add(new ArrayList<Integer>());
            order.get(order.size()-1).add((Integer) grid.get(0)[1]);
            order.add(new ArrayList<Integer>());
            order.get(order.size()-1).add((Integer) lowgrid.get(0)[1]);
        }
        Collections.reverse(order);
        for (int i = 0; i < order.size(); i++) {
            System.out.print((i + 1) + " place: ");
            for (int j : order.get(i)) {
                System.out.print(j + " ");
            }
            System.out.println();
        }
    }
}
