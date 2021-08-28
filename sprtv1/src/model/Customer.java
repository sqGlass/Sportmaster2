package model;

import shopper.Shopper;

import java.util.Objects;

public class Customer {

    private int id;
    private String name;
    private String login;
    private String password;
    private int balance;
    private int personalDailyDiscount;

    private Shopper shopper;

    public Customer(int id, String name, String login, String password, int balance, Shopper shopper) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
        this.balance = balance;
        this.shopper = shopper;
        this.personalDailyDiscount = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addMoney(int money) {
        this.balance += money;
    }

    public boolean substructMoney(int money) {
        if (this.balance - money >= 0) {
            this.balance -= money;
            return true;
        }
        else
            return false;
    }

    public void addItemToShopper(Item item) {
        shopper.addItem(item);
    }

    public void deleteItemFromShopper(Item item) {
        shopper.deleteItem(item);
    }

    public int getPersonalDailyDiscount() {
        return personalDailyDiscount;
    }

    public void setPersonalDailyDiscount(int personalDailyDiscount) {
        if (this.personalDailyDiscount == 0)
            this.personalDailyDiscount = personalDailyDiscount;
    }

    public boolean buyItems() {
        double priceAfterDiscount;
        if (this.shopper.getPurchses().isEmpty())
            return false;

        priceAfterDiscount = shopper.getSumCost() - (shopper.getSumCost() / 100.0 * personalDailyDiscount);
        if (priceAfterDiscount > balance)
            return false;
        balance -= priceAfterDiscount;
        shopper.getPurchses().clear();
        shopper.setSumCost(0);
        return true;
    }

    public Shopper getShopper() {
        return shopper;
    }

    public void setShopper(Shopper shopper) {
        this.shopper = shopper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id && balance == customer.balance && Objects.equals(name, customer.name) && Objects.equals(login, customer.login) && Objects.equals(password, customer.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, login, password, balance);
    }
}
