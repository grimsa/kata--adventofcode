package com.github.grimsa.aoc2018;

import com.github.grimsa.io.ClasspathFileReader;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.stream.Stream;

final class Day1 {
    public static void main(final String... args) {
        final var frequencyChanges = new ClasspathFileReader("input/2018/day1.txt").get();
        System.out.println("Part one answer: " + getTotal(frequencyChanges));
        System.out.println("Part two answer: " + getTotalAtFirstRepetition(frequencyChanges));
    }

    private static int getTotal(final List<String> frequencyChanges) {
        return frequencyChanges.stream()
                .mapToInt(Integer::parseInt)
                .sum();
    }

    private static int getTotalAtFirstRepetition(final List<String> frequencyChanges) {
        return cycle(frequencyChanges)
                .mapToInt(Integer::parseInt)
                .map(new RunningTotalAsInt())
                .filter(new UniqueInts().negate())
                .findFirst()
                .orElseThrow();
    }

    private static <T> Stream<T> cycle(final List<T> elements) {
        return Stream.generate(elements::stream).flatMap(Function.identity());
    }

    private static class RunningTotalAsInt implements IntUnaryOperator {
        int total;

        @Override
        public int applyAsInt(int change) {
            return total += change;
        }
    }

    private static class UniqueInts implements IntPredicate {
        private final Set<Integer> uniqueValues = new HashSet<>();

        @Override
        public boolean test(int value) {
            return uniqueValues.add(value);
        }
    }
}
