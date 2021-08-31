package model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import shopper.Order;

@Data
public class Customer {

    @Setter(AccessLevel.NONE)
    private int id;
    private String name;
    private String login;
    private String password;
    @Setter(AccessLevel.NONE)
    private int balance;
    private int personalDailyDiscount;

    private Order shopper;

    public Customer(int id, String name, String login, String password, int balance, Order shopper) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
        this.balance = balance;
        this.shopper = shopper;
        this.personalDailyDiscount = 0;
    }


    public void addMoney(int money) {
        this.balance += money;
    }

    public boolean substructMoney(int money) {
        if (this.balance - money >= 0) {
            this.balance -= money;
            return true;
        } else
            return false;
    }

    public void addItemToShopper(Item item) {
        shopper.addItem(item);
    }

    public void deleteItemFromShopper(Item item) {
        shopper.deleteItem(item);
    }

    public void setPersonalDailyDiscount(int personalDailyDiscount) {
        if (this.personalDailyDiscount == 0) {
            this.personalDailyDiscount = personalDailyDiscount;
        }
    }

    public boolean buyItems() {
        double priceAfterDiscount;
        if (this.shopper.getPurchses().isEmpty()) {
            return false;
        }

        priceAfterDiscount = shopper.getSumCost() - (shopper.getSumCost() / 100.0 * personalDailyDiscount);
        if (priceAfterDiscount > balance) {
            return false;
        }
        balance -= priceAfterDiscount;
        shopper.getPurchses().clear();
        shopper.setSumCost(0);
        return true;
    }
}