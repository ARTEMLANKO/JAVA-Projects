package markup;

public interface Element {
    void toMarkdown(StringBuilder sb);
    void toDocBook(StringBuilder sb);
}