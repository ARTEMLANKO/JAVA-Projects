package markup;

public class Text implements Element {
    public String string;
    public Text (String a) {
        string = a;
    }
    public void toMarkdown(StringBuilder ans) {
        ans.append(string);
    }
    public void toDocBook(StringBuilder ans) {
        ans.append(string);
    }
}
