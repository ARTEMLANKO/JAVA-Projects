package markup;

import java.util.List;

public class Emphasis extends Abstract{
    public Emphasis (List<Element> elements) {
        super(elements, "*",  "<emphasis>", "</emphasis>");
    }
}
