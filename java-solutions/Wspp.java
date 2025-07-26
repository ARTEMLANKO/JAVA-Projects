import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map;
import java.lang.Character;
import java.util.Collections;
import java.util.function.Predicate;

public class Wspp {

    public static void addWord(LinkedHashMap<String, IntList> dict, String word1, int i) {
        if (word1.isEmpty()) {
            return;
        }
        IntList a = dict.getOrDefault(word1, new IntList());
        a.append(i);
        dict.put(word1, a);
    }

    public static void main (String[] args) {
        LinkedHashMap<String, IntList> dict = new LinkedHashMap<>();
        StringBuilder word = new StringBuilder();
        try {
            Predicate<Character> predict = s -> ((Character.isLetter(s)) || s == '\'' || Character.getType(s) == Character.DASH_PUNCTUATION);
            MyReader file = new MyReader(args[0], predict);
            //System.err.println(1);
            char[] buffer = new char[1024];
            int i = 0;
            int j = 0;
            while (file.hasNextLine()) {
                j = 0;
                while (file.hasNext()) {
                    i++;
                    addWord(dict, file.nextWord().toLowerCase(), i);
                }
            }
            file.closeAll();
        } catch (FileNotFoundException exp) {
            System.out.println("No input file");
        } catch (IOException exp) {
            System.out.println("Error reading input file: " + exp.getMessage());
        }
        //System.err.println(1);
        try {
            BufferedWriter outputfile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), "utf-8"));
            try {
                for (Map.Entry<String, IntList> entry : dict.entrySet()) {
                    int sz = entry.getValue().getSize();
                    outputfile.write(entry.getKey() + " " + entry.getValue().getSize());
                    int tek = 0;
                    for (int i1 : entry.getValue().get()) {
                        outputfile.write(" " + i1);
                    }
                    //System.err.println();
                    //System.err.println(entry.getKey() + " " + entry.getValue());
                    outputfile.newLine();
                }
            } finally {
                outputfile.close();
            }
        } catch (FileNotFoundException exp) {
            System.out.println("No output file");
        } catch (IOException exp) {
            System.out.println("IOException while working with file, exception type is: " + exp.getClass().getCanonicalName());
        }
        //System.err.println(1);
    }
}