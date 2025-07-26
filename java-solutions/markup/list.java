package markup;

import java.util.List;

public class list implements Token {
    private List<ListItem> elements;

    private String DocBookBegin;
    private String DocBookEnd;
    public list (List<ListItem> elements, String DocBookBegin, String DocBookEnd) {
        this.elements = elements;
        this.DocBookBegin = DocBookBegin;
        this.DocBookEnd = DocBookEnd;
    }

    public void toDocBook(StringBuilder ans) {
        ans.append(DocBookBegin);
        for (ListItem el : elements) {
            el.toDocBook(ans);
        }
        ans.append(DocBookEnd);
    }
}
