package server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import dao.CustomerDAO;
import dao.ItemDAO;
import model.Customer;
import model.Item;
import shopper.Shopper;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

public class Server {
    private static HttpServer httpServer;
    private static Customer customer;
    private static CustomerDAO customerDAO = new CustomerDAO();
    private static ItemDAO itemDAO = new ItemDAO();

    //private static Shopper shopper = new Shopper();

    public static void main(String[] args) throws Exception{
        httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

        httpServer.createContext("/", new Hello());

        httpServer.createContext("/login", new Autorization());

        httpServer.createContext("/myshopper", new MyShopper());
        httpServer.createContext("/items/jackets", new ItemsJackets());
        httpServer.createContext("/items/shoes", new ItemsShoes());
        httpServer.createContext("/items/id", new ItemsId());
        httpServer.createContext("/items", new Items());
        httpServer.createContext("/myshopper/delete", new DeletePurchase());
        httpServer.setExecutor(null);
        httpServer.start();

        // Kogda server.stop?
    }

    static class Hello implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            Server.doResponse(t, "src/views/startPage.json");
        }
    }

    static class Autorization implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            if (!t.getRequestMethod().equalsIgnoreCase("POST"))
            {
                doResponse(t, "src/views/startPage.json");
                return;
            }

            Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
            if (params.size() != 2 || !params.containsKey("login") || !params.containsKey("pass"))
            {

                doResponse(t, "src/views/autorizationError.json");
                return;
            }
            else if (params.get("login").length() < 1 || params.get("pass").length() < 1 ||
                    customerDAO.findCustomerByLoginAndPassword(params.get("login"), params.get("pass")) == null)
            {
                doResponse(t, "src/views/autorizationError.json");
                return;
            }

            customer = customerDAO.findCustomerByLoginAndPassword(params.get("login"), params.get("pass"));
            customer.setPersonalDailyDiscount(getPersonalDailyDiscount());
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(new File("src/views/items.json"),customer);
            Server.doResponse(t, "src/views/items.json");
        }
    }

    static class Items implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(new File("src/views/items.json"), itemDAO.getTypes());

            Server.doResponse(t, "src/views/items.json");
        }
    }
    static class ItemsJackets implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(new File("src/views/items.json"), itemDAO.getItemsByType("jackets"));

            Server.doResponse(t, "src/views/items.json");
        }
    }

    static class ItemsShoes implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(new File("src/views/items.json"), itemDAO.getItemsByType("shoes"));

            Server.doResponse(t, "src/views/items.json");

        }
    }
    static class ItemsId implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            // check for correct index
            int id = Server.findId(t);

            // if index bad - go to startPage
            if (id == -1)
            {
                Server.doResponse(t, "src/views/startPage.json");
                return;
            }

            // if index is good - check method: GET or POST
            if (t.getRequestMethod().equalsIgnoreCase("POST"))
            {
                if (customer == null)
                {
                    doResponse(t, "src/views/askAutorization.json");
                    return;
                }
                else if (itemDAO.getItemById(id) != null)
                {
                    customer.addItemToShopper(itemDAO.getItemById(id));
                    itemDAO.deleteItemById(id);
                }
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
                mapper.writeValue(new File("src/views/items.json"),customer.getShopper());
            }
            else
            {
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
                mapper.writeValue(new File("src/views/items.json"), itemDAO.getItemById(id));
            }
            Server.doResponse(t, "src/views/items.json");
        }
    }

    static class MyShopper implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            if (customer == null)
            {
                doResponse(t, "src/views/askAutorization.json");
                return;
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(new File("src/views/items.json"), customer.getShopper());

            Server.doResponse(t, "src/views/items.json");
        }
    }
    static class DeletePurchase implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            if (customer == null)
            {
                doResponse(t, "src/views/askAutorization.json");
                return;
            }
            // check for correct index
            int id = Server.findId(t);

            // if index bad - go to startPage
            if (id == -1) {
                Server.doResponse(t, "src/views/startPage.json");
                return;
            }
            if (t.getRequestMethod().equalsIgnoreCase("DELETE")) {
                for (Item ite:
                     customer.getShopper().getPurchses()) {
                    if (ite.getId() == id)
                    {
                        customer.deleteItemFromShopper(ite);
                        itemDAO.returnItemToShopFromShopper(ite);
                        break;
                    }
                }
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
                mapper.writeValue(new File("src/views/items.json"), customer.getShopper());

                Server.doResponse(t, "src/views/items.json");
            }
            else
                Server.doResponse(t,"src/views/startPage.json");
        }
    }
    public static void doResponse(HttpExchange t ,String pathName) throws IOException {
        File file = new File (pathName);
        byte [] bytearray  = new byte [(int)file.length()];
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        bis.read(bytearray, 0, bytearray.length);

        t.sendResponseHeaders(200, file.length());
        OutputStream os = t.getResponseBody();
        os.write(bytearray,0,bytearray.length);
        os.close();
    }

    public static int findId(HttpExchange t) {
        String[] paths = t.getRequestURI().toString().split("/");
        int id = -1;
        if (paths.length == 4) {
            boolean isNumber = Pattern.matches("[0-9]+", paths[3]);
            if (isNumber) {
                id = Integer.parseInt(paths[3]);
            }
        }
        return id;
    }

    public static Map<String, String> queryToMap(String query) {
        if(query == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }

    public static int getPersonalDailyDiscount() {
        return new Random().nextInt(30 - 5 + 1) + 5;
    }

}
