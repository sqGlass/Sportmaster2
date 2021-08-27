package dao;

import com.fasterxml.jackson.annotation.JsonView;
import model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    private List<String> types;
    private List<Item> items;
    private static int IDs;

    {
        types = new ArrayList<>();
        types.add("shoes");
        types.add("jackets");
    }

    {
        items = new ArrayList<>();
        items.add(new Item(IDs++,46, "shoes", "Zoom Freak 3", "red"));
        items.add(new Item(IDs++,49, "shoes", "Zoom Freak 3", "green"));
        items.add(new Item(IDs++,45, "shoes", "Zoom Freak 3", "black"));

        items.add(new Item(IDs++,52, "jackets", "Jordan Essentials", "red"));
        items.add(new Item(IDs++,56, "jackets", "Jordan Essentials", "red"));
    }

    public List<Item> getItems() {
        return items;
    }

    public Item getItemById(int id) {
        if (id < IDs && id >= 0)
            return items.get(id);
        else
            return null;
    }

    public  List<String> getTypes() {
        return types;
    }

    public List<Item> getItemsByType(String type) {
        List<Item> retItems = new ArrayList<>();
        for (Item item:
             items) {
            if (item.getType().equals(type))
                retItems.add(item);
        }
        return retItems;
    }
}
