package markup;

import java.util.List;

public class Strong extends Abstract {
    public String string;
    public Strong (List<Element> elements) {
        super(elements, "__", "<emphasis role='bold'>", "</emphasis>");
    }
}
