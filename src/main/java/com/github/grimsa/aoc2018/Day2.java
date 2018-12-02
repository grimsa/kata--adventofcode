package com.github.grimsa.aoc2018;

import com.github.grimsa.io.ClasspathFileReader;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day2 {
    public static void main(final String... args) {
        final var boxIds = new ClasspathFileReader("input/2018/day2.txt").get();
        System.out.println("Part one answer: " + getChecksum(boxIds));
    }

    private static long getChecksum(final List<String> boxIds) {
        final var letterCountsList = boxIds.stream()
                .map(Day2::distinctLetterCounts)
                .collect(Collectors.toList());
        final var countsContainingTwo = letterCountsList.stream()
                .filter(counts -> counts.contains(2L))
                .count();
        final var countsContainingThree = letterCountsList.stream()
                .filter(counts -> counts.contains(3L))
                .count();
        return countsContainingTwo * countsContainingThree;
    }

    private static Set<Long> distinctLetterCounts(final String string) {
        return string.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(Function.identity(), Collectors.counting()),
                        countsByLetter -> Set.copyOf(countsByLetter.values())
                ));
    }
}
