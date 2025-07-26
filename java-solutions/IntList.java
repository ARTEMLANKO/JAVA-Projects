import java.util.*;

public class IntList {
    // :NOTE: private
    int[] list;
    int i;
    int n;

    public IntList() {
        list = new int[1];
        i = 0;
        n = 0;
    }

    public void append(int c) {
        if (i == list.length) {
            list = Arrays.copyOf(list, list.length * 2);
        }
        list[i] = c;
        i++;
    }

    public int[] get() {
        return list;
    }
    // :NOTE: должен быть метод get по индексу

    public int getSize() {
        list = Arrays.copyOf(list, i); // :NOTE: странно
        return list.length;
    }

    public void update() {
        n++;
    }
}
