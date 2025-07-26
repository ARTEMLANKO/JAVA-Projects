package markup;

import java.util.List;

public class Strikeout extends Abstract {
    public String string;
    public Strikeout (List<Element> elements) {
        super(elements, "~",  "<emphasis role='strikeout'>", "</emphasis>");
    }
}
