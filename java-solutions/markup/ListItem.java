package markup;

import java.util.List;

public class ListItem {
    private List<Token> elements;

    public ListItem (final List<Token> elements) {
        this.elements = elements;
    }
    public void toDocBook(final StringBuilder ans) {
        ans.append("<listitem>");
        for (final Token el : elements) {
            el.toDocBook(ans);
        }
        ans.append("</listitem>");
    }
}
