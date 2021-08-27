package shopper;

import model.Item;

import java.util.ArrayList;
import java.util.List;

public class Shopper {
    private List<Item> purchses = new ArrayList<>();

    public void addItem(Item item) {
        purchses.add(item);
    }
    public List<Item> getPurchses() {
        return purchses;
    }
}
