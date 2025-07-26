import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.lang.Character;

public class WordStatInput {
    public static void main(String[] args) {
        LinkedHashMap<String, Integer> dict = new LinkedHashMap<>();
        StringBuilder word = new StringBuilder();
        String current;
        BufferedReader file = null;
        BufferedWriter file1 = null;
        try {
            file = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), "utf-8"), 1024);
            //System.out.println(args[0]);
            try {
                char[] buffer = new char[1024];
                while (true) {
                    int read = file.read(buffer);
                    if (read < 0) {
                        break;
                    }
                    current = new String(buffer, 0, read);
                    current = current.toLowerCase();
                    for (int i = 0; i < current.length(); i++) {
                        if (Character.getType(current.charAt(i)) == Character.DASH_PUNCTUATION
                            || ((current.charAt(i)) == '\'') || Character.isLetter(current.charAt(i))) {
                            word.append(current.charAt(i));
                        } else {
                            if (word.length() > 0) {
                                if (dict.containsKey(word.toString())) {
                                    dict.put(word.toString(), dict.get(word.toString()) + 1);
                                } else {
                                    dict.put(word.toString(), 1);
                                }
                            }
                            word = new StringBuilder();
                        }
                    }
                }
                if (word.length() > 0) {
                    if (dict.containsKey(word.toString())) {
                        dict.put(word.toString(), dict.get(word.toString()) + 1);
                    } else {
                        dict.put(word.toString(), 1);
                    }
                }
            } catch (IOException exp) {
                System.out.println("IOException: " + exp.getMessage());
            }
        } catch (FileNotFoundException exp) {
            System.out.println("No input file");
        } catch (UnsupportedEncodingException exp) {
            System.out.println("Encoding troubles");
        } finally {
            try {
                if (file != null) {
                    file.close();
                }
            } catch (IOException exp) {
                System.out.println("IOException: " + exp.getMessage());
            }

        }
        try {
            file1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), "utf-8"));
            for (Map.Entry<String, Integer> entry : dict.entrySet()) {
                file1.write(entry.getKey() + " " + dict.get(entry.getKey()));
                System.out.println(entry.getKey() + " " + dict.get(entry.getKey()));
                file1.newLine();
            }
        } catch (FileNotFoundException exp) {
            System.out.println("No input file");
        } catch (UnsupportedEncodingException exp) {
            System.out.println("Encoding troubles");
        } catch (IOException exp) {
            System.out.println("IOException: " + exp.getMessage());
        } finally {
            try {
                if (file1 != null) {
                    file1.close();
                }
            } catch (IOException exp) {
                System.out.println("IOException: " + exp.getMessage());
            }
        }
    }
}