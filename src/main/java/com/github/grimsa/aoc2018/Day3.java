package com.github.grimsa.aoc2018;

import com.github.grimsa.aoc2018.day3.Area;
import com.github.grimsa.io.ClasspathFileReader;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day3 {
    public static void main(final String... args) {
        final var claims = new ClasspathFileReader("input/2018/day3.txt").get().stream()
                .map(Area::parse)
                .collect(Collectors.toList());
        System.out.println("Part one answer: " + countOverlappingSquares(claims));
    }

    private static long countOverlappingSquares(final List<Area> claims) {
        return claims.stream()
                .flatMap(Area::squares)
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(Function.identity(), Collectors.counting()),
                        countsBySquare -> countsBySquare.values().stream()
                                .filter(count -> count > 1)
                                .count()
                ));
    }
}
