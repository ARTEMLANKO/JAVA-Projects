import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.lang.Character;
import java.util.function.Predicate;

public class WsppCountEvenDigits {


    public static void addWord(LinkedHashMap<String, IntList1> dict, String word1, int i, int j) {
        if (word1.isEmpty()) {
            return;
        }
        IntList1 a = dict.get(word1);
        if (a == null) {
            a = new IntList1();
        }
        a.append(i, j);
        dict.put(word1, a);
    }

    public static void main (String[] args) {
        LinkedHashMap<String, IntList1> dict = new LinkedHashMap<>();
        try {
            Predicate<Character> predict = s -> ((Character.isLetter(s)) || s == '\'' || Character.getType(s) == Character.DASH_PUNCTUATION || Character.isDigit(s));
            MyReader file = new MyReader(args[0], predict);
            int i = 0;
            int j = 0;
            while (file.hasNextLine()) {
                i++;
                j = 0;
                while (file.hasNext()) {
                    j++;
                    String sss = file.nextWord().toLowerCase();
                    addWord(dict, sss, i, j);
                }
            }

            file.closeAll(); // :NOTE: игнорируем исключения в сканнере и не закрывааем сканер
        } catch (FileNotFoundException exp) {
            System.err.println("No input file");
        } catch (IOException exp) {
            System.err.println("Error reading input file: " + exp.getMessage());
        }
        try {
            BufferedWriter outputfile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), "utf-8"));
            try {
                List<Map.Entry<String, IntList1>> keys = new ArrayList<>(dict.entrySet());
                keys.sort(Comparator.comparingInt(entry -> entry.getValue().getsize())
//                        .thenComparingInt(key -> dict.get(key).getfirst()[0])
//                        .thenComparingInt(key -> dict.get(key).getfirst()[1]));
                );
                for (Map.Entry<String, IntList1> entry : keys) {
                    IntList1 b = entry.getValue();
                    outputfile.write(entry.getKey());
                    outputfile.write(" " + b.getsize());
                    int[] ne = b.get();
                    for (int z = 0; z < ne.length; z++) {
                        outputfile.write(" " + ne[z]);
                    }
                    outputfile.newLine();
                }
            } finally {
                outputfile.close();
            }
        } catch (FileNotFoundException exp) {
            System.err.println("No output file");
        } catch (IOException exp) {
            System.err.println("IOException while working with file, exception type is: " + exp.getClass().getCanonicalName());
        }
    }
}