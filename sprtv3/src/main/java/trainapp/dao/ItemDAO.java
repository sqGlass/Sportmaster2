package trainapp.dao;

import org.springframework.stereotype.Component;
import trainapp.models.Item;

import java.util.ArrayList;
import java.util.Arrays;
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
        Item ShoesPattern = Item
                .builder()
                .price(300)
                .type("shoes")
                .model("Zoom Freak 3")
                .build();

        Item JacketsPattern = Item
                .builder()
                .price(300)
                .type("jackets")
                .model("Jordan Essentials")
                .color("red")
                .build();

        items = Arrays.asList(ShoesPattern
                        .toBuilder()
                        .id(IDs.getAndIncrement())
                        .size(49)
                        .color("green")
                        .build(),

                        ShoesPattern
                        .toBuilder()
                        .id(IDs.getAndIncrement())
                        .size(45)
                        .color("black")
                        .build(),

                        ShoesPattern
                        .toBuilder()
                        .id(IDs.getAndIncrement())
                        .size(46)
                        .color("red")
                        .build(),

                        JacketsPattern
                        .toBuilder()
                        .id(IDs.getAndIncrement())
                        .size(52)
                        .build(),

                        JacketsPattern
                        .toBuilder()
                        .id(IDs.getAndIncrement())
                        .size(56)
                        .build()
                );
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
