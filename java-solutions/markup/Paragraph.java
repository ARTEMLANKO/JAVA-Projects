package markup;

import java.util.List;

public class Paragraph implements Token {
    private List<Element> elements;

    public Paragraph (final List<Element> elements) {
        this.elements = elements;
    }

    public void toMarkdown(final StringBuilder ans) {
        for (final Element el : elements) {
            el.toMarkdown(ans);
        }
    }

    public void toDocBook(final StringBuilder ans) {
        ans.append("<para>");
        for (final Element el : elements) {
            el.toDocBook(ans);
        }
        ans.append("</para>");
    }
}
