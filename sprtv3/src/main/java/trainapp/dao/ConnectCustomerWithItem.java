package trainapp.dao;

import org.springframework.stereotype.Component;
import trainapp.models.Item;

@Component
public class ConnectCustomerWithItem {

    public void addItemToOrder(CustomerDAO customerDAO, ItemDAO itemDAO, int itemId) {
        if (itemDAO.getItemById(itemId) != null) {
            customerDAO.getCurrentCustomer().addItemToShopper(itemDAO.getItemById(itemId));
            itemDAO.deleteItemById(itemId);
        }
    }

    public void deleteItemFromOrder(CustomerDAO customerDAO, ItemDAO itemDAO, int itemID) {
        for (Item ite : customerDAO.getCurrentCustomer().getShopper().getPurchses()) {
            if (ite.getId() == itemID) {
                customerDAO.getCurrentCustomer().deleteItemFromShopper(ite);
                itemDAO.returnItemToShopFromShopper(ite);
                break;
            }
        }
    }
}
