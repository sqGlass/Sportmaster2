package trainapp.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import trainapp.dao.ConnectCustomerWithItem;
import trainapp.dao.CustomerDAO;
import trainapp.dao.ItemDAO;
import trainapp.dao.NullUserException;
import trainapp.models.Customer;
import trainapp.models.Item;
import trainapp.models.Order;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final CustomerDAO customerDAO;
    private final ItemDAO itemDAO;
    private final ConnectCustomerWithItem connectCustomersWithItem;

    @GetMapping(value = "/")
    public ModelAndView sayHello() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("startPage");
        return modelAndView;
    }

    @PostMapping(value = "/login")
    @SneakyThrows
    public Customer getAll(@RequestParam("login") String login,
                           @RequestParam("pass") String password) {
        customerDAO.setCurrentCustomer(customerDAO.findCustomerByLoginAndPassword(login, password));
        if (customerDAO.getCurrentCustomer() == null) {
            throw new NullUserException("User not found");
        }
        return customerDAO.getCurrentCustomer();
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @SneakyThrows
    public Customer getAccount() {

        if (customerDAO.getCurrentCustomer() == null) {
            throw new NullUserException("U r not login!");
        }
        return customerDAO.getCurrentCustomer();
    }

    @GetMapping(value = "/items", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Item> getAll() {
        return itemDAO.getItems();
    }

    @GetMapping(value = "/items/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SneakyThrows
    public Item getItem(@PathVariable("itemId") int itemId) {
        Optional<Item> optItem = Optional.ofNullable(itemDAO.getItemById(itemId));
        return optItem.orElseThrow(() -> new IllegalArgumentException("Item not found!"));
    }

    @PostMapping(value = "/items/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SneakyThrows
    public Customer itemToOrder(@PathVariable("itemId") int itemId) {
        if (itemDAO.getItemById(itemId) == null) {
            throw new IllegalArgumentException("No item by this ID");
        }

        if (customerDAO.getCurrentCustomer() == null) {
            throw new NullUserException("U r not login!");
        }
        connectCustomersWithItem.addItemToOrder(customerDAO, itemDAO, itemId);
        return customerDAO.getCurrentCustomer();

    }

    @GetMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    @SneakyThrows
    public Order showOrder() {
        if (customerDAO.getCurrentCustomer() == null) {
            throw new NullUserException("U r not login!");
        }
        return customerDAO.getCurrentCustomer().getShopper();
    }

    @PostMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public String buyOrder() {

        if (customerDAO.getCurrentCustomer() == null) {
            return "U have to be login!";
        }
        if (customerDAO.getCurrentCustomer().getShopper().getPurchases().isEmpty()) {
            return "Order is empty!";
        }
        if (!customerDAO.getCurrentCustomer().buyItems()) {
            return "Not enough money, sir!";
        }
        return "Success buy!\n" + customerDAO.getCurrentCustomer().toString();
    }

    @DeleteMapping(value = "/orders/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SneakyThrows
    public Order buyOrder(@PathVariable("itemId") int itemId) {

        if (customerDAO.getCurrentCustomer() == null) {
            throw new NullUserException("U r not login!");
        }
        if (!customerDAO.getCurrentCustomer().isItemInOrder(itemId)) {
            throw new IllegalArgumentException("No item by this ID");
        }
        connectCustomersWithItem.deleteItemFromOrder(customerDAO, itemDAO, itemId);
        return customerDAO.getCurrentCustomer().getShopper();
    }

    @GetMapping(value = "/items/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getItemsCategories() {

        return itemDAO.getTypes();
    }

    @GetMapping(value = "/items/categories/shoes", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Item> getShoes() {

        return itemDAO.getItemsByType("shoes");
    }

    @GetMapping(value = "/items/categories/jackets", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Item> getJackets() {

        return itemDAO.getItemsByType("jackets");
    }
}
