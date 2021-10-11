package ru.tuanviet.javabox;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class App {

    public static void main(String[] args) {

        String query = "https://api.npoint.io/f744aa71f0b7c142f0fd";
        String query2 = "https://api.npoint.io/a742b65192a1e1e22858";
        String firstAnswer = App.getResponse(query);
        String secondAnswer = App.getResponse(query2);
        if (secondAnswer == null || firstAnswer == null) {
            System.out.println("Error");
            System.exit(1);
        }

        firstAnswer = firstAnswer.substring(firstAnswer.indexOf("["), firstAnswer.indexOf("]"));
        firstAnswer = firstAnswer.replaceAll("\"", "").replaceAll("\\[", "").replaceAll("]", "");
        List<String> firstNames = Arrays.asList(firstAnswer.split(","));
        App.replace(firstNames);

        secondAnswer = secondAnswer.replaceAll("\"", "").replaceAll("\\[", "").replaceAll("]", "");
        List<String> lastNames = Arrays.asList(secondAnswer.split(","));
        App.replace(lastNames);

        App.showThreeRandomNames(firstNames, lastNames);


    }
    public static String getResponse(String query) {
        HttpURLConnection connection = null;
        StringBuilder sb = new StringBuilder();
        try {
            connection = (HttpURLConnection) new URL(query).openConnection();
            connection.setRequestMethod("GET");

            connection.connect();

            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            }
            else {
                System.out.println("Error " + connection.getResponseCode() + ", " + connection.getResponseMessage());
            }
        }
        catch (Throwable cause) {
            cause.printStackTrace();
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    public static void replace(List<String> strings)
    {
        ListIterator<String> iterator = strings.listIterator();
        while (iterator.hasNext())
        {
            String str = iterator.next();
            iterator.set(str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase());
        }
    }

    public static void showThreeRandomNames(List<String> firstNames, List<String> lastNames) {

        for (int i = 0; i < 3; i++) {
            int firstNameInd = ThreadLocalRandom.current().nextInt(0, firstNames.size());
            int lastNameInd = ThreadLocalRandom.current().nextInt(0, lastNames.size());
            System.out.println(firstNames.get(firstNameInd) + " " + lastNames.get(lastNameInd));
        }
    }
}
