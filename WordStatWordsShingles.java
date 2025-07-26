

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.TreeMap;
import java.util.Set;
import java.util.Map;
import java.lang.Character;
import java.util.Collections;
import java.util.function.Predicate;

public class WordStatWordsShingles {
    public static void addToDict(TreeMap<String, Integer> dict, String word) {
        dict.put(word, dict.getOrDefault(word, 0) + 1);
    }

    public static void addWord(TreeMap<String, Integer> dict, String word) {
        if (word.isEmpty()) {
            return;
        }
        if (word.length() <= 3) {
            addToDict(dict, word);
        } else {
            for (int j = 0; j < word.length() - 2; j++) {
                String word1 = word.substring(j, j + 3);
                addToDict(dict, word1);
            }
        }
    }

    public static void main(String[] args) {
        TreeMap<String, Integer> dict = new TreeMap<>(Collections.reverseOrder());
        StringBuilder word = new StringBuilder();
        Predicate<Character> predict = s -> ((Character.isLetter(s)) || s == '\'' || Character.getType(s) == Character.DASH_PUNCTUATION);
        try {
            MyReader file = new MyReader(args[0], predict);
            //System.err.println(1);
            char[] buffer = new char[1024];
            while (file.hasNextLine()) {
                while (file.hasNext()) {
                    String g = file.nextWord();
                    //System.err.println(g);
                    addWord(dict, g.toLowerCase());
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
                for (Map.Entry<String, Integer> entry : dict.entrySet()) {
                    outputfile.write(entry.getKey() + " " + entry.getValue());
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
