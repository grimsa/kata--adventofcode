package com.github.grimsa.aoc2018.day3;

import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class Area {
    private static final Pattern CLAIM_PATTERN = Pattern.compile("^#\\d+ @ (?<left>\\d+),(?<top>\\d+): (?<width>\\d+)x(?<height>\\d+)$");
    private final Square topLeftCorner;
    private final int width;
    private final int height;

    public static Area parse(final String areaString) {
        final var matcher = CLAIM_PATTERN.matcher(areaString);
        if (!matcher.matches()) {
            throw new IllegalArgumentException();
        }

        return new Area(
                Integer.parseInt(matcher.group("left")),
                Integer.parseInt(matcher.group("top")),
                Integer.parseInt(matcher.group("width")),
                Integer.parseInt(matcher.group("height"))
        );
    }

    private Area(final int distanceFromLeft, final int distanceFromTop, final int width, final int height) {
        topLeftCorner = new Square(distanceFromLeft, distanceFromTop);
        this.width = width;
        this.height = height;
    }

    public Stream<Square> squares() {
        return IntStream.range(0, height).boxed()
                .flatMap(relativeY -> IntStream.range(0, width)
                        .mapToObj(relativeX -> new Square(topLeftCorner.x + relativeX, topLeftCorner.y + relativeY))
                );
    }

    public static final class Square {
        private final int x;
        private final int y;

        private Square(final int x, final int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || getClass() != other.getClass()) {
                return false;
            }

            final var square = (Square) other;
            return x == square.x &&
                    y == square.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
