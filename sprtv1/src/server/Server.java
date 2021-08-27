package server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import dao.ItemDAO;
import shopper.Shopper;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;

public class Server {
    private static HttpServer httpServer;
    private static ItemDAO itemDAO = new ItemDAO();
    private static Shopper shopper = new Shopper();
    public static void main(String[] args) throws Exception{
        httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

        httpServer.createContext("/exit", new Exit());
        httpServer.createContext("/", new Hello());
        httpServer.createContext("/items/jackets", new ItemsJackets());
        httpServer.createContext("/items/shoes", new ItemsShoes());
        httpServer.createContext("/items/id", new ItemsId());
        httpServer.createContext("/items", new Items());
        httpServer.setExecutor(null);
        httpServer.start();
    }

    static class Hello implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {

            File file = new File ("src/views/startPage.json");
            byte [] bytearray  = new byte [(int)file.length()];
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(bytearray, 0, bytearray.length);

            t.sendResponseHeaders(200, file.length());
            OutputStream os = t.getResponseBody();
            os.write(bytearray,0,bytearray.length);
            os.close();
        }
    }

    static class Exit implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String response = "Bye!";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
            t.close();
            httpServer.stop(0);
        }
    }

    static class Items implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(new File("src/views/users.json"), itemDAO.getTypes());
            mapper.writerWithDefaultPrettyPrinter().writeValueAsString(itemDAO.getTypes());

            File file = new File("src/views/users.json");
            byte[] bytearray = new byte[(int) file.length()];
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(bytearray, 0, bytearray.length);

            t.sendResponseHeaders(200, file.length());
            OutputStream os = t.getResponseBody();
            os.write(bytearray, 0, bytearray.length);
            os.close();
        }
    }
    static class ItemsJackets implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(new File("src/views/users.json"), itemDAO.getItemsByType("jackets"));
            mapper.writerWithDefaultPrettyPrinter().writeValueAsString(itemDAO.getItemsByType("jackets"));

            File file = new File ("src/views/users.json");
            byte [] bytearray  = new byte [(int)file.length()];
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(bytearray, 0, bytearray.length);

            t.sendResponseHeaders(200, file.length());
            OutputStream os = t.getResponseBody();
            os.write(bytearray,0,bytearray.length);
            os.close();
        }
    }

    static class ItemsShoes implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(new File("src/views/users.json"), itemDAO.getItemsByType("shoes"));
            mapper.writerWithDefaultPrettyPrinter().writeValueAsString(itemDAO.getItemsByType("shoes"));

            File file = new File ("src/views/users.json");
            byte [] bytearray  = new byte [(int)file.length()];
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(bytearray, 0, bytearray.length);

            t.sendResponseHeaders(200, file.length());
            OutputStream os = t.getResponseBody();
            os.write(bytearray,0,bytearray.length);
            os.close();
        }
    }
    static class ItemsId implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {

            String[] paths = t.getRequestURI().toString().split("/");
            int id = -1;
            if (paths.length == 4)
            {
                boolean isNumber = Pattern.matches("[0-9]+", paths[3]);
                if (isNumber)
                    id = Integer.parseInt(paths[3]);
            }
            if (id == -1)
            {
                File file = new File ("src/views/startPage.json");
                byte [] bytearray  = new byte [(int)file.length()];
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                bis.read(bytearray, 0, bytearray.length);

                // ok, we are ready to send the response.
                t.sendResponseHeaders(200, file.length());
                OutputStream os = t.getResponseBody();
                os.write(bytearray,0,bytearray.length);
                os.close();
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(new File("src/views/users.json"), itemDAO.getItemById(id));
            mapper.writerWithDefaultPrettyPrinter().writeValueAsString(itemDAO.getItemById(id));

            File file = new File ("src/views/users.json");
            byte [] bytearray  = new byte [(int)file.length()];
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(bytearray, 0, bytearray.length);

            t.sendResponseHeaders(200, file.length());
            OutputStream os = t.getResponseBody();
            os.write(bytearray,0,bytearray.length);
            os.close();
        }
    }



}
