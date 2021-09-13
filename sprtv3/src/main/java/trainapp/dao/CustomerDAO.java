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
        customers.add(Customer
                .builder()
                .id(id.getAndIncrement())
                .name("Sasha")
                .login("sasha1337")
                .password("1234")
                .balance(1500)
                .shopper(new Order())
                .build()
        );
        customers.add(Customer
                .builder()
                .id(id.getAndIncrement())
                .name("Vasya")
                .login("vasyaKill")
                .password("777")
                .balance(700)
                .shopper(new Order())
                .build()
        );
    }

    public Customer findCustomerByLoginAndPassword(String login, String password) {
        for (Customer cust : customers) {
            if (cust.getLogin().equals(login) && cust.getPassword().equals(password)) {
                return cust;
            }
        }
        return null;
    }
}

