package shopper;

import model.Item;

import java.util.ArrayList;
import java.util.List;

public class Shopper {

    private int sumCost;
    private List<Item> purchses;

    {
        sumCost = 0;
        purchses = new ArrayList<>();
    }

    public void addItem(Item item) {
        if (!purchses.contains(item))
        {
            purchses.add(item);
            sumCost+= item.getPrice();
        }
    }

    public void deleteItem(Item item) {
        if (purchses.contains(item))
        {
            purchses.remove(item);
            sumCost-=item.getPrice();
        }
    }
    public List<Item> getPurchses() {
        return purchses;
    }

    public int getSumCost() {
        return sumCost;
    }

    public void setSumCost(int sumCost) {
        this.sumCost = sumCost;
    }
}
