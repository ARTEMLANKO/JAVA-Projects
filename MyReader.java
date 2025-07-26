import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Predicate;
import java.util.NoSuchElementException;
import static java.lang.Character.isDigit;

public class MyReader {
    private InputStreamReader inputReader;
    private char[] buf = new char[1024];
    private int bufferSize = 0;
    private int pointer = 0;
    private StringBuilder token = new StringBuilder();
    // :NOTE: access
    Predicate<Character> predict;

    public MyReader(Predicate<Character> predicate) throws IOException {
        inputReader = new InputStreamReader(System.in, "utf-8");
        predict = predicate;
    }

    public MyReader(String fileIn, Predicate<Character> predicate) throws IOException {
        inputReader = new InputStreamReader(new FileInputStream(fileIn), "utf-8");
        predict = predicate;
    }

    private void readInput() {
        pointer = 0;
        try {
            bufferSize = inputReader.read(buf);
        } catch (IOException e) {
            // :NOTE: consumed
            System.err.println("IOException while working with file, exception type is: " + e.getClass().getCanonicalName());
        }
    }

    public boolean hasNextLine() {
        if (pointer < bufferSize) {
            return true;
        } else {
            readInput();
            if (bufferSize == -1) {
                return false;
            }
        }
        return true;
    }

    public boolean hasNext() {
        token.setLength(0);
        while (true) {
            if (pointer == bufferSize) {
                readInput();
                if (bufferSize == -1) {
                    return false;
                }
            }
            if (predict.test(buf[pointer])) {
                break;
            } else {
                // :NOTE: simplify
                if (buf[pointer] == '\r') {
                    pointer++;
                    if (pointer == bufferSize) {
                        readInput();
                    }
                    if (pointer == -1) {
                        return false;
                    }
                    if (buf[pointer] == '\n') {
                        pointer++;
                        if (pointer == bufferSize) {
                            readInput();
                        }
                        return false;
                    }
                    return false;
                } else if (buf[pointer] == '\n') {
                    pointer++;
                    if (pointer == bufferSize) {
                        readInput();
                    }
                    return false;
                } else {
                    pointer++;
                }
            }
        }

        while (true) {
            if (predict.test(buf[pointer])) {
                token.append(buf[pointer++]);
                if (pointer == bufferSize) {
                    readInput();
                }
                if (bufferSize == -1) {
                    return true;
                }
            } else {
                if (pointer == bufferSize) {
                    readInput();
                }
                return true;
            }
        }
    }

    public String next() {
        if (token.length() == 0) {
            if (hasNext()) {
                String word = token.toString();
                token.setLength(0);
                return word;
            } else {
                throw new NoSuchElementException("No next");
            }
        }
        // :NOTE: copy-paste
        String wrd = token.substring(0);
        token.setLength(0);
        return wrd;
    }

    public int nextInt() {
        final String s = next();
        if (Character.toLowerCase(s.charAt(s.length() - 1)) == 'o') {
            return Integer.parseUnsignedInt(s.substring(0, s.length() - 1), 8);
        }
        return Integer.parseInt(s.substring(0, s.length()));
    }

    public String nextWord() {
        return next();
    }

    public void closeAll() throws IOException {
        inputReader.close();
    }
}
//