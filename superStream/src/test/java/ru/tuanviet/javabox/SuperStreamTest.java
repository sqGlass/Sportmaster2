package ru.tuanviet.javabox;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

public class SuperStreamTest {
    SuperStream<Integer> superStream = new SuperStream<>(Arrays.asList(1, 2, 3, 10, 56));
    List<Integer> testList = Arrays.asList(1, 2, 3, 10, 56);
    Set<Integer> testSet = new HashSet<>(Arrays.asList(1, 2, 3, 10, 56));

    @Test
    public void shouldCreateListFromIterable() {
        List<Integer> resultList = superStream.toList();
        assertThat(resultList).isEqualTo(testList);
    }

    @Test
    public void shouldCreateSetFromIterable() {
        Set<Integer> resultSet = superStream.toSet();
        assertThat(resultSet).isEqualTo(testSet);
    }

    @Test
    public void shouldDoesMathOperationsInMapMethod() {
        assertThat(superStream
                .map(i -> i * 2)
                .map(i -> i * 10)
                .toList())
                .isEqualTo(Arrays.asList(20, 40, 60, 200, 1120));
    }

    @Test
    public void shouldFilterResultValues() {
        assertThat(superStream
                .map(i -> i * 2)
                .map(i -> i * 10)
                .filter(i -> i > 100)
                .map(i -> i * 2)
                .toList())
                .isEqualTo(Arrays.asList(400, 2240));
    }

    @Test
    public void shouldJoinIterableAndReturnString() {
        assertThat(superStream
                .join())
                .isEqualTo("1231056");
    }

    @Test
    public void shouldJoinAfterMapAndFilter() {
        assertThat(superStream
                .map(i -> i * 2)
                .map(i -> i * 10)
                .filter(i -> i > 100)
                .map(i -> i * 2)
                .join())
                .isEqualTo("4002240");
    }

    @Test
    public void shouldJoinValuesWithDivider() {
        assertThat(superStream.join("|")).isEqualTo("1|2|3|10|56");
    }
}
