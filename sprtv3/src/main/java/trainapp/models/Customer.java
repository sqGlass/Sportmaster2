package trainapp.models;

import lombok.*;

import java.util.Random;

@Data
@Builder
public class Customer {

    @Setter(AccessLevel.NONE)
    private Integer id;
    private String name;
    private String login;
    @ToString.Exclude
    private String password;
    @Setter(AccessLevel.NONE)
    private int balance;
    @Setter(AccessLevel.NONE)
    @Builder.Default
    private int personalDiscount = takePersonalDiscount();

    private Order shopper;

    public void addItemToShopper(Item item) {
        shopper.addItem(item);
    }

    public void deleteItemFromShopper(Item item) {
        shopper.deleteItem(item);
    }

    public static int takePersonalDiscount() {
        return new Random().nextInt(30 - 5 + 1) + 5;
    }

    public boolean isItemInOrder(int id) {
        for (Item ite : shopper.getPurchases()) {
            if (ite.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public boolean buyItems() {
        double priceAfterDiscount;
        if (this.shopper.getPurchases().isEmpty()) {
            return false;
        }

        priceAfterDiscount = shopper.getSumCost() - (shopper.getSumCost() / 100.0 * personalDiscount);
        if (priceAfterDiscount > balance) {
            return false;
        }
        balance -= priceAfterDiscount;
        shopper.getPurchases().clear();
        shopper.setSumCost(0);
        return true;
    }

}