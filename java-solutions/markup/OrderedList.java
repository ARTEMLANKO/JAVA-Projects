package markup;

import java.util.List;

public class OrderedList extends list {
    public OrderedList (List<ListItem> elements) {
        super(elements, "<orderedlist>", "</orderedlist>");
    }
}
