package trainapp.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Order {

    private int sumCost;
    private List<Item> purchses;

    {
        sumCost = 0;
        purchses = new ArrayList<>();
    }

    public void addItem(Item item) {
        if (!purchses.contains(item)) {
            purchses.add(item);
            sumCost += item.getPrice();
        }
    }

    public void deleteItem(Item item) {
        if (purchses.contains(item)) {
            purchses.remove(item);
            sumCost -= item.getPrice();
        }
    }

}
