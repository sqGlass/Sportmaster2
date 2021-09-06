package trainapp.dao;

import org.springframework.stereotype.Component;
import trainapp.models.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ItemDAO {

    private List<String> types;
    private List<Item> items;
    private AtomicInteger IDs;

    {
        IDs = new AtomicInteger(0);
        types = new ArrayList<>();
        types.add("shoes");
        types.add("jackets");
    }

    {
        items = new ArrayList<>();
        items.add(new Item(IDs.getAndIncrement(), 46, 300, "shoes", "Zoom Freak 3", "red"));
        items.add(new Item(IDs.getAndIncrement(), 49, 300, "shoes", "Zoom Freak 3", "green"));
        items.add(new Item(IDs.getAndIncrement(), 45, 300, "shoes", "Zoom Freak 3", "black"));

        items.add(new Item(IDs.getAndIncrement(), 52, 250, "jackets", "Jordan Essentials", "red"));
        items.add(new Item(IDs.getAndIncrement(), 56, 250, "jackets", "Jordan Essentials", "red"));
    }

    public List<Item> getItems() {
        return items;
    }

    public Item getItemById(int id) {

        for (Item ite : items) {
            if (ite.getId() == id) {
                return ite;
            }
        }
        return null;
    }

    public void deleteItemById(int id) {
        items.removeIf(ite -> ite.getId() == id);
    }


    public List<String> getTypes() {
        return types;
    }

    public List<Item> getItemsByType(String type) {
        List<Item> retItems = new ArrayList<>();
        for (Item item : items) {
            if (item.getType().equals(type)) {
                retItems.add(item);
            }
        }
        return retItems;
    }

    public void returnItemToShopFromShopper(Item item) {
        items.add(item);
    }

}
