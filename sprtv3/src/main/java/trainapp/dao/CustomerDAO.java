package trainapp.dao;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import trainapp.models.Customer;
import trainapp.models.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Component
public class CustomerDAO {

    private List<Customer> customers;
    private Customer currentCustomer;
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private AtomicInteger id;

    {
        customers = new ArrayList<>();
        id = new AtomicInteger(0);
        customers.add(new Customer(id.getAndIncrement(), "Sasha", "sasha1337", "1234", 1500, new Order()));
        customers.add(new Customer(id.getAndIncrement(), "Vasya", "vasyaKill", "777", 700, new Order()));
    }

    public Customer findCustomerByLoginAndPassword(String login, String password) {
        for (Customer cust : customers) {
            if (cust.getLogin().equals(login) && cust.getPassword().equals(password)) {
                return cust;
            }
        }
        return null;
    }

    public Customer getCustomerById(int id) {
        for (Customer cust : customers) {
            if (cust.getId() == id) {
                return cust;
            }
        }
        return null;
    }

}

