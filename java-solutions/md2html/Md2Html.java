package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.lang.Character;


public class Md2Html {

    public static StringBuilder parse(int l, int r, String string, boolean haslink) {
        StringBuilder ans = new StringBuilder("");
        while (l <= r) {
            if (string.charAt(l) == '*' && l < r && string.charAt(l + 1) != '*') {
                l = notDoubleStarOrMinus(ans, string, l, r, '*', haslink);
            } else if (string.charAt(l) == '*' && l < r && string.charAt(l + 1) == '*') {
                l = doubleStarOrMinus(ans, string, l, r, '*', haslink);
            } else if (string.charAt(l) == '_' && l < r && string.charAt(l + 1) != '_') {
                l = notDoubleStarOrMinus(ans, string, l, r, '_', haslink);
            } else if (string.charAt(l) == '_' && l < r && string.charAt(l + 1) == '_') {
                l = doubleStarOrMinus(ans, string, l, r, '_', haslink);
            } else if (string.charAt(l) == '-' && l < r && string.charAt(l + 1) == '-') {
                int i = l + 2;
                while (i <= r - 1 && (string.charAt(i) != '-' || string.charAt(i + 1) != '-')) {
                    i++;
                }
                if (i <= r - 1) {
                    ans.append("<s>" + parse(l + 2, i - 1, string, haslink) + "</s>");
                    l = i + 2;
                } else {
                    ans.append("--");
                    l++;
                }
            } else if (string.charAt(l) == '`') {
                int i = l + 1;
                while (i <= r && string.charAt(i) != '`') {
                    i++;
                }
                if (i <= r) {
                    ans.append("<code>" + parse(l + 1, i - 1, string, haslink) + "</code>");
                    l = i + 1;
                } else {
                    ans.append('`');
                    l++;
                }
            } else if (string.charAt(l) == '\\' && l != r && (string.charAt(l + 1) == '_' || string.charAt(l + 1) == '*' || string.charAt(l + 1) == '[' || string.charAt(l + 1) == ']')) {
                ans.append(string.charAt(l + 1));
                l += 2;
            } else if (string.charAt(l) == '[') {
                int i = l + 1;
                while (i <= r && haslink) {
                    if (string.charAt(i-1) == '\\' && string.charAt(i) == ']') {
                        i++;
                    } else if (string.charAt(i) != ']') {
                        i++;
                    } else {
                        break;
                    }
                }
                int right_link = r + 1;
                int light_link = r + 1;
                if (i < r && haslink) {
                    right_link = i;
                    light_link = i;
                    if (string.charAt(i+1) == '<') {
                        right_link = i+2;
                        while (right_link <= r) {
                            if (string.charAt(right_link) == '>' && string.charAt(right_link-1) == '\\') {
                                right_link++;
                            } else if (string.charAt(right_link) != '>') {
                                right_link++;
                            } else {
                                break;
                            }
                        }
                    } else {
                        right_link = r+1;
                    }
                }
                if (right_link <= r) {
                    ans.append("<a href='" + string.substring(light_link+2, right_link) + "\'>" + parse(l + 1, i - 1, string, haslink) + "</a>");
                    l = right_link + 1;
                } else {
                    ans.append('[');
                    l++;
                    haslink = false;
                }
            } else {
                if (string.charAt(l) == '<') {
                    ans.append("&lt;");
                } else if (string.charAt(l) == '>') {
                    ans.append("&gt;");
                } else if (string.charAt(l) == '&') {
                    ans.append("&amp;");
                } else if (string.charAt(l) != '\\'){
                    ans.append(string.charAt(l));
                }
                l++;
            }
        }
        return ans;
    }

    private static int doubleStarOrMinus(StringBuilder ans, String string, int l, int r, char symbol, boolean haslink) {
        int i = l + 2;
        while (i <= r - 1 && (string.charAt(i) != symbol || string.charAt(i + 1) != symbol)) {
            i++;
        }
        if (i <= r - 1) {
            ans.append("<strong>" + parse(l + 2, i - 1, string, haslink) + "</strong>");
            l = i + 2;
        } else {
            ans.append(symbol + symbol);
            l += 2;
        }
        return l;
    }

    public static int notDoubleStarOrMinus(StringBuilder ans, String string, int l, int r, char symbol, boolean haslink) {
        int i = l + 1;
        while ((i <= r && string.charAt(i) != symbol) || (i <= r - 1 && (string.charAt(i) == symbol && string.charAt(i + 1) == symbol))) {
            if ((i <= r - 1 && (string.charAt(i) == symbol && string.charAt(i + 1) == symbol))) {
                i += 2;
            } else {
                i++;
            }
        }
        if (i <= r && string.charAt(i - 1) != '\\') {
            ans.append("<em>" + parse(l + 1, i - 1, string, haslink) + "</em>");
            l = i + 1;
        } else {
            ans.append(symbol);
            l++;
        }
        return l;
    }
    public static void main (String[] args) {
        BufferedReader file_in = null;
        BufferedWriter file_out = null;
        List<String> ans = new ArrayList<String>();
        try {
            file_in = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), "utf-8"), 1024);
            List<String> strings_in = file_in.lines().toList();
            List<StringBuilder> strings = new ArrayList<StringBuilder>();
            for (int i1 = 0; i1 < strings_in.size(); i1++) {
                if (strings_in.get(i1).isEmpty()) {
                    if (strings.size() == 0) {
                        continue;
                    }
                } else {
                    if (i1 == 0) {
                        strings.add(new StringBuilder(""));
                        strings.get(strings.size() - 1).append(strings_in.get(i1));
                    } else {
                        if (strings_in.get(i1 - 1).isEmpty()) {
                            strings.add(new StringBuilder(""));
                            strings.get(strings.size() - 1).append(strings_in.get(i1));
                        } else {
                            strings.get(strings.size() - 1).append(System.lineSeparator() + strings_in.get(i1));
                        }
                    }
                }
            }
            StringBuilder curstring;
            boolean h = false;
            int grids_number = 0;
            boolean p = false;
            for (StringBuilder i : strings) {
                //System.err.println(i);
                int j = 0;
                curstring = new StringBuilder();
                if (i.charAt(0) == '#') {
                    StringBuilder grids = new StringBuilder();
                    while (j < i.length() && i.charAt(j) == '#') {
                        j++;
                        grids.append("#");
                    }
                    if (j == i.length()) {
                        p = true;
                        curstring.append("<p>");
                        curstring.append(i);
                    } else {
                        if (Character.isWhitespace(i.charAt(j))) {
                            curstring.append("<h");
                            curstring.append(String.valueOf(j) + '>');
                            h = true;
                            grids_number = j;
                            j++;
                        } else {
                            p = true;
                            curstring.append("<p>");
                            curstring.append(grids);
                        }
                    }
                } else {
                    curstring.append("<p>");
                    p = true;
                }
                //System.err.println(curstring);
                curstring.append(parse(j, i.length() - 1, i.toString(), true));
                //System.err.println(curstring);
                if (p) {
                    p = false;
                    curstring.append("</p>");
                } else {
                    h = false;
                    curstring.append("</h" + String.valueOf(j-1) + ">");
                }
                ans.add(new String(curstring));
                curstring.setLength(0);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (file_in != null) {
                    file_in.close();
                }
            } catch (IOException exp) {
                System.err.println("IOException: " + exp.getMessage());
            }
        }
        try {
            file_out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), "utf-8"));
            for (String i2: ans) {
                file_out.write(i2.toString());
                file_out.newLine();
            }
        } catch (FileNotFoundException exp) {
            System.err.println("No input file");
        } catch (UnsupportedEncodingException exp) {
            System.err.println("Encoding troubles");
        } catch (IOException exp) {
            System.err.println("IOException: " + exp.getMessage());
        } finally {
            try {
                if (file_out != null) {
                    file_out.close();
                }
            } catch (IOException exp) {
                System.err.println("IOException: " + exp.getMessage());
            }
        }
    }
}