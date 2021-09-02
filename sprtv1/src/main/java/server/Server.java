package server;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import dao.ConnectCustomersWithItem;
import dao.CustomerDAO;
import dao.ItemDAO;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Server {
    private static HttpServer httpServer;

    private static CustomerDAO customerDAO = new CustomerDAO();
    private static ItemDAO itemDAO = new ItemDAO();
    private static ConnectCustomersWithItem connectCustomersWithItem = new ConnectCustomersWithItem();

    public static void main(String[] args) throws Exception {
        httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

        httpServer.createContext("/", new Hello());
        httpServer.createContext("/login", new Autorization());
        httpServer.createContext("/account", new Account());
        httpServer.createContext("/orders", new MyShopper());
        httpServer.createContext("/items/categories/jackets", new ItemsJackets());
        httpServer.createContext("/items/categories/shoes", new ItemsShoes());
        httpServer.createContext("/items/categories", new Items());
        httpServer.createContext("/items", new AllItems());

        httpServer.setExecutor(null);
        httpServer.start();

        // Kogda server.stop?
    }

    static class Hello implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            Server.doResponse(t, "src/main/resources/views/startPage.json");
        }
    }

    static class Autorization implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            if (!t.getRequestMethod().equalsIgnoreCase("POST")) {
                doResponse(t, "src/main/resources/views/startPage.json");
                return;
            }

            Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
            if (params.size() != 2 || !params.containsKey("login") || !params.containsKey("pass")) {

                doResponse(t, "src/main/resources/views/autorizationError.json");
                return;
            }
            if (params.get("login").length() < 1 || params.get("pass").length() < 1 ||
                    customerDAO.findCustomerByLoginAndPassword(params.get("login"), params.get("pass")) == null) {
                doResponse(t, "src/main/resources/views/autorizationError.json");
                return;
            }
            customerDAO.setCurrentCustomer(customerDAO.findCustomerByLoginAndPassword(params.get("login"), params.get("pass")));

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(new File("src/main/resources/views/items.json"), customerDAO.getCurrentCustomer());
            Server.doResponse(t, "src/main/resources/views/items.json");
        }
    }

    static class Account implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(new File("src/main/resources/views/items.json"), customerDAO.getCurrentCustomer());

            Server.doResponse(t, "src/main/resources/views/items.json");
        }
    }

    static class Items implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(new File("src/main/resources/views/items.json"), itemDAO.getTypes());

            Server.doResponse(t, "src/main/resources/views/items.json");
        }
    }

    static class AllItems implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            int id;
            if (t.getRequestMethod().equalsIgnoreCase("GET")) {
                if (countFolders(t) == 1) {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.enable(SerializationFeature.INDENT_OUTPUT);
                    mapper.writeValue(new File("src/main/resources/views/items.json"), itemDAO.getItems());
                } else if (countFolders(t) == 2) {
                    // check for correct index
                    id = Server.findId(t, 2, 2);

                    // if index bad - go to startPage
                    if (id == -1) {
                        Server.doResponse(t, "src/main/resources/views/startPage.json");
                        return;
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.enable(SerializationFeature.INDENT_OUTPUT);
                    mapper.writeValue(new File("src/main/resources/views/items.json"), itemDAO.getItemById(id));
                }
            } else if (t.getRequestMethod().equalsIgnoreCase("POST")) {
                id = Server.findId(t, 2, 2);

                // if index bad - go to startPage
                if (id == -1) {
                    Server.doResponse(t, "src/main/resources/views/startPage.json");
                    return;
                }
                if (customerDAO.getCurrentCustomer() == null) {
                    doResponse(t, "src/main/resources/views/askAutorization.json");
                    return;
                }
                connectCustomersWithItem.addItemToOrder(customerDAO, itemDAO, id);

                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
                mapper.writeValue(new File("src/main/resources/views/items.json"),
                        customerDAO.getCurrentCustomer().getShopper());
            }
            Server.doResponse(t, "src/main/resources/views/items.json");
        }
    }

    static class ItemsJackets implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(new File("src/main/resources/views/items.json"), itemDAO.getItemsByType("jackets"));

            Server.doResponse(t, "src/main/resources/views/items.json");
        }
    }

    static class ItemsShoes implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(new File("src/main/resources/views/items.json"), itemDAO.getItemsByType("shoes"));
            Server.doResponse(t, "src/main/resources/views/items.json");
        }
    }


    static class MyShopper implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            int id;
            if (customerDAO.getCurrentCustomer() == null) {
                doResponse(t, "src/main/resources/views/askAutorization.json");
                return;
            }

            if (t.getRequestMethod().equalsIgnoreCase("GET")) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
                mapper.writeValue(new File("src/main/resources/views/items.json"),
                        customerDAO.getCurrentCustomer().getShopper());
            } else if (t.getRequestMethod().equalsIgnoreCase("POST")) {
                if (customerDAO.getCurrentCustomer().getShopper().getPurchses().isEmpty()) {
                    Server.doResponse(t, "src/main/resources/views/emptyShopper.json");
                } else if (!customerDAO.getCurrentCustomer().buyItems()) {
                    Server.doResponse(t, "src/main/resources/views/notEnoughMoney.json");
                } else {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.enable(SerializationFeature.INDENT_OUTPUT);
                    mapper.writeValue(new File("src/main/resources/views/items.json"), customerDAO.getCurrentCustomer());
                }
            } else if (t.getRequestMethod().equalsIgnoreCase("DELETE")) {
                // check for correct index
                id = Server.findId(t, 2, 2);

                // if index bad - go to startPage
                if (id == -1) {
                    Server.doResponse(t, "src/main/resources/views/startPage.json");
                    return;
                }
                connectCustomersWithItem.deleteItemFromOrder(customerDAO, itemDAO, id);
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
                mapper.writeValue(new File("src/main/resources/views/items.json"), customerDAO.getCurrentCustomer().getShopper());
            }
            Server.doResponse(t, "src/main/resources/views/items.json");
        }
    }

    public static void doResponse(HttpExchange t, String pathName) throws IOException {
        File file = new File(pathName);
        byte[] bytearray = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        bis.read(bytearray, 0, bytearray.length);

        t.sendResponseHeaders(200, file.length());
        OutputStream os = t.getResponseBody();
        os.write(bytearray, 0, bytearray.length);
        os.close();
    }

    public static int findId(HttpExchange t, int numberForCheck, int expectedNumberOfFolders) {
        String[] paths = t.getRequestURI().toString().split("/");
        int id = -1;
        if (paths.length - 1 == expectedNumberOfFolders) {
            boolean isNumber = Pattern.matches("[0-9]+", paths[numberForCheck]);
            if (isNumber) {
                id = Integer.parseInt(paths[numberForCheck]);
            }
        }
        return id;
    }

    public static int countFolders(HttpExchange t) {
        String[] paths = t.getRequestURI().toString().split("/");
        return paths.length - 1;
    }


    public static Map<String, String> queryToMap(String query) {
        if (query == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }


}
