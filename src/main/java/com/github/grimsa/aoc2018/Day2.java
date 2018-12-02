package com.github.grimsa.aoc2018;

import com.github.grimsa.io.ClasspathFileReader;
import com.google.common.base.Strings;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        boxIds.sort(Comparator.naturalOrder());
        final var expectedLength = boxIds.get(0).length() - 1;
        return boxIds.stream()
                .flatMap(id -> streamOfPartsCommonWithEachOtherId(id, boxIds))
                .filter(commonPart -> commonPart.length() == expectedLength)
                .findFirst()
                .orElseThrow();
    }

    private static Stream<String> streamOfPartsCommonWithEachOtherId(final String id, final List<String> boxIds) {
        final var followingIds = boxIds.subList(Collections.binarySearch(boxIds, id) + 1, boxIds.size());
        return followingIds.stream()
                .map(otherId -> Strings.commonPrefix(id, otherId) + Strings.commonSuffix(id, otherId));
    }
}
