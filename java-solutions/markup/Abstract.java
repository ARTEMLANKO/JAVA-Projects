package markup;

import java.util.List;

public abstract class Abstract implements Element {
    private List<Element> elements;
    private String MarkDownTag;
    private String DocBookBegin;
    private String DocBookEnd;

    public Abstract (List<Element> elements, String MarkDownTag, String DocBookBegin, String DocBookEnd) {
        this.elements = elements;
        this.MarkDownTag = MarkDownTag;
        this.DocBookBegin = DocBookBegin;
        this.DocBookEnd = DocBookEnd;
    }

    public void toMarkdown(StringBuilder sb) {
        sb.append(MarkDownTag);
        for (Element el : elements) {
            el.toMarkdown(sb);
        }
        sb.append(MarkDownTag);
    }

    public void toDocBook(StringBuilder sb) {
        sb.append(DocBookBegin);
        for (Element el : elements) {
            el.toDocBook(sb);
        }
        sb.append(DocBookEnd);
    }
}
