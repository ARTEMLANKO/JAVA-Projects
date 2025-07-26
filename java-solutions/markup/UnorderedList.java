package markup;

import java.util.List;

public class UnorderedList extends list {

    public UnorderedList (List<ListItem> items) {
        super(items, "<itemizedlist>", "</itemizedlist>");
    }
}
