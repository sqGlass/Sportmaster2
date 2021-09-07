package trainapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import trainapp.dao.ConnectCustomerWithItem;
import trainapp.dao.CustomerDAO;
import trainapp.dao.ItemDAO;
import trainapp.models.Customer;
import trainapp.models.Item;
import trainapp.models.Order;

import java.util.List;

@Controller
public class HomeController {

    private final CustomerDAO customerDAO;
    private final ItemDAO itemDAO;
    private final ConnectCustomerWithItem connectCustomersWithItem;

    @Autowired
    public HomeController(CustomerDAO customerDAO, ItemDAO itemDAO, ConnectCustomerWithItem connectCustomersWithItem) {
        this.customerDAO = customerDAO;
        this.itemDAO = itemDAO;
        this.connectCustomersWithItem = connectCustomersWithItem;
    }


    @ExceptionHandler(Exception.class)
    public @ResponseBody
    String handleAllException(Exception ex) {
        return ex.getMessage();
    }

    @GetMapping(value = "/")
    public String sayHello() {
        return "startPage";
    }

    @PostMapping(value = "/login")
    public @ResponseBody
    Customer getAll(@RequestParam("login") String login,
                    @RequestParam("pass") String password) throws Exception {
        customerDAO.setCurrentCustomer(customerDAO.findCustomerByLoginAndPassword(login, password));
        if (customerDAO.getCurrentCustomer() == null) {
            throw new Exception("User not found");
        }
        return (customerDAO.getCurrentCustomer());
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Customer getAccount() throws Exception {

        if (customerDAO.getCurrentCustomer() == null) {
            throw new Exception("U r not login!");
        }
        return (customerDAO.getCurrentCustomer());
    }

    @GetMapping(value = "/items", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<Item> getAll() {
        return (itemDAO.getItems());
    }

    @GetMapping(value = "/items/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Item getItem(@PathVariable("itemId") int itemId) throws Exception {
        if (itemDAO.getItemById(itemId) == null) {
            throw new Exception("Item not found!");
        }
        return (itemDAO.getItemById(itemId));
    }

    @PostMapping(value = "/items/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Customer itemToOrder(@PathVariable("itemId") int itemId) throws Exception {

        try {
            if (itemDAO.getItemById(itemId) == null) {
                throw new IllegalArgumentException();
            }
            connectCustomersWithItem.addItemToOrder(customerDAO, itemDAO, itemId);
            return (customerDAO.getCurrentCustomer());
        } catch (IllegalArgumentException ex) {
            throw new Exception("No item by this ID");
        } catch (NullPointerException ex) {
            throw new Exception("U have to be login!");
        }
    }

    @GetMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Order showOrder() throws Exception {
        try {
            return customerDAO.getCurrentCustomer().getShopper();
        } catch (NullPointerException e) {
            throw new Exception("U have to be login!");
        }
    }

    @PostMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    String buyOrder() {

        if (customerDAO.getCurrentCustomer() == null) {
            return ("U have to be login!");
        }
        if (customerDAO.getCurrentCustomer().getShopper().getPurchses().isEmpty()) {
            return "Order is empty!";
        }
        if (!customerDAO.getCurrentCustomer().buyItems()) {
            return "Not enough money, sir!";
        }
        return "Success buy!\n" + customerDAO.getCurrentCustomer().toString();
    }

    @DeleteMapping(value = "/orders/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Order buyOrder(@PathVariable("itemId") int itemId) throws Exception {

        try {
            if (customerDAO.getCurrentCustomer().isItemInOrder(itemId)) {
                connectCustomersWithItem.deleteItemFromOrder(customerDAO, itemDAO, itemId);
                return customerDAO.getCurrentCustomer().getShopper();
            }
            throw new IllegalArgumentException();
        } catch (NullPointerException e) {
            throw new Exception("U have to be login!");
        } catch (IllegalArgumentException e) {
            throw new Exception("No item in ur order by this ID");
        }
    }

    @GetMapping(value = "/items/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<String> getItemsCategories() {

        return (itemDAO.getTypes());
    }

    @GetMapping(value = "/items/categories/shoes", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<Item> getShoes() {

        return (itemDAO.getItemsByType("shoes"));
    }

    @GetMapping(value = "/items/categories/jackets", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<Item> getJackets() {

        return (itemDAO.getItemsByType("jackets"));
    }
}
