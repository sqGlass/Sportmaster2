package model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import shopper.Order;

import java.util.Random;

@Data
public class Customer {

    @Setter(AccessLevel.NONE)
    private int id;
    private String name;
    private String login;
    private String password;
    @Setter(AccessLevel.NONE)
    private int balance;
    @Setter(AccessLevel.NONE)
    private int personalDiscount;

    private Order shopper;

    public Customer(int id, String name, String login, String password, int balance, Order shopper) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
        this.balance = balance;
        this.shopper = shopper;
        this.personalDiscount = getPersonalDiscount();
    }

    public void addItemToShopper(Item item) {
        shopper.addItem(item);
    }

    public void deleteItemFromShopper(Item item) {
        shopper.deleteItem(item);
    }

    public int getPersonalDiscount() {
        return new Random().nextInt(30 - 5 + 1) + 5;
    }

    public boolean buyItems() {
        double priceAfterDiscount;
        if (this.shopper.getPurchses().isEmpty()) {
            return false;
        }

        priceAfterDiscount = shopper.getSumCost() - (shopper.getSumCost() / 100.0 * personalDiscount);
        if (priceAfterDiscount > balance) {
            return false;
        }
        balance -= priceAfterDiscount;
        shopper.getPurchses().clear();
        shopper.setSumCost(0);
        return true;
    }
}