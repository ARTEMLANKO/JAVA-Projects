import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.lang.Character;

// :NOTE: назавать логичнее
// :NOTE: Использовать готовый IntList как поле
public class IntList1 {
    // :NOTE: pritvate
    int[] list;
    int i;
    IntList ilist;
    int currstring;
    int kol;
    int size;
    int firsty;
    int firstx;

    public IntList1() {
        list = new int[1];
        i = 0;
        size = 0;
        firsty = 0;
        firstx = 0;
        currstring = -1;
        kol = 0;
    }

    public void append(int y, int x) {
        if (i == list.length) {
            list = Arrays.copyOf(list, list.length * 2);
        }
        if (currstring != y) {
            currstring = y;
            kol = 1;
        } else {
            kol += 1;
        }
        if (kol % 2 == 0) {
            list[i] = x;
            i++;
        }
        size++;
    }

    public int[] get() {
        list = Arrays.copyOf(list, i);
        return list;
    }

    public int[] getfirst() {
        int[] b = new int[2];
        b[0] = firsty;
        b[1] = firstx;
        return b;
    }

    public int getcurr() {
        return currstring;
    }

    public int getsize() {
        return size;
    }

}