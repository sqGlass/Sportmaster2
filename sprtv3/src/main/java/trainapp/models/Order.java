package trainapp.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Order {

    private Integer sumCost;
    private List<Item> purchases;

    {
        sumCost = 0;
        purchases = new ArrayList<>();
    }

    public void addItem(Item item) {
        if (!purchases.contains(item)) {
            purchases.add(item);
            sumCost += item.getPrice();
        }
    }

    public void deleteItem(Item item) {
        if (purchases.contains(item)) {
            purchases.remove(item);
            sumCost -= item.getPrice();
        }
    }

}
