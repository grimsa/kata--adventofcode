package com.github.grimsa.aoc2018;

import com.github.grimsa.io.ClasspathFileReader;
import com.google.common.base.Strings;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day2 {
    public static void main(final String... args) {
        final var boxIds = new ClasspathFileReader("input/2018/day2.txt").get();
        System.out.println("Part one answer: " + getChecksum(boxIds));
        System.out.println("Part two answer: " + findPrototypeFabricBoxIdCommonLetters(boxIds));
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

    private static String findPrototypeFabricBoxIdCommonLetters(final List<String> boxIds) {
        for (int i = 0; i < boxIds.size(); i++) {
            for (int j = i + 1; j < boxIds.size(); j++) {
                final var one = boxIds.get(i);
                final var two = boxIds.get(j);
                if (distance(one, two) == 1) {
                    return commonPart(one, two);
                }
            }
        }

        return "N/A";
    }

    private static String commonPart(final String one, final String two) {
        return Strings.commonPrefix(one, two) + Strings.commonSuffix(one, two);
    }

    private static long distance(final String one, final String two) {
        return IntStream.range(0, one.length())
                .filter(index -> one.charAt(index) != two.charAt(index))
                .count();
    }
}
