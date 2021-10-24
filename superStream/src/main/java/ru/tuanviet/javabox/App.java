package ru.tuanviet.javabox;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) {

        SuperStream<Integer> ss = new SuperStream<>(Arrays.asList(1, 2, 3));

        SuperStream<String> ss2 = new SuperStream<>(Arrays.asList("1", "2", "3"));
        Stream<String> ss3 = Stream.of("1", "2", "3");

        List<Integer> test = ss2.map(Integer::valueOf).toList();
        System.out.println(test.get(0) + test.get(1));

        List<Integer> result1 = ss.map(i -> i * 2).filter(i -> i > 2).toList();
        System.out.println(result1); // prints: [4, 6]

        Set<Integer> result2 = ss.filter(i -> i > 20).toSet();
        System.out.println(result2); // prints: []

        List<String> result3 = ss
                .map(i -> i * 2)
                .filter(i -> i > 2)
                .filter(i -> i > 5)
                .map(i -> String.format("this is %s", i))
                .toList();
        System.out.println(result3); // prints: ["this is 6"]

        String result4 = ss
                .map(i -> i * 2)
                .join();
        System.out.println(result4); // prints: "246"

        String result5 = ss
                .map(i -> i * 2)
                .join(" | ");
        System.out.println(result5); // prints: "2 | 4 | 6"
    }
}
