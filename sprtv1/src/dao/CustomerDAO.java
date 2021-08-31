package dao;

import model.Customer;
import shopper.Shopper;

import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    private List<Customer> customers;
    private static int id;

    {
        customers = new ArrayList<>();
        customers.add(new Customer(id++, "Sasha", "sasha1337", "1234", 1500, new Shopper()));
        customers.add(new Customer(id++, "Vasya", "vasyaKill", "777", 700, new Shopper()));
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public Customer findCustomerByLoginAndPassword(String login, String password) {
        for (Customer cust:
             customers) {
            if (cust.getLogin().equals(login) && cust.getPassword().equals(password))
                return cust;
        }
        return null;
    }

    public Customer getCustomerById(int id) {
        for (Customer cust:
             customers) {
            if (cust.getId() == id)
                return cust;
        }
        return null;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
}
