package com.github.grimsa.aoc2018.day3;

import java.awt.*;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class Area extends Rectangle {
    private static final Pattern CLAIM_PATTERN = Pattern.compile("^#(?<id>\\d+) @ (?<left>\\d+),(?<top>\\d+): (?<width>\\d+)x(?<height>\\d+)$");
    private final int id;

    public static Area parse(final String areaString) {
        final var matcher = CLAIM_PATTERN.matcher(areaString);
        if (!matcher.matches()) {
            throw new IllegalArgumentException();
        }

        return new Area(
                Integer.parseInt(matcher.group("id")),
                Integer.parseInt(matcher.group("left")),
                Integer.parseInt(matcher.group("top")),
                Integer.parseInt(matcher.group("width")),
                Integer.parseInt(matcher.group("height"))
        );
    }

    private Area(final int id, final int distanceFromLeft, final int distanceFromTop, final int width, final int height) {
        super(distanceFromLeft, distanceFromTop, width, height);
        this.id = id;
    }

    public Stream<Square> squares() {
        return IntStream.range(0, height).boxed()
                .flatMap(relativeY -> IntStream.range(0, width)
                        .mapToObj(relativeX -> new Square(x + relativeX, y + relativeY))
                );
    }

    @Override
    public boolean intersects(final Rectangle other) {
        return this != other
                && super.intersects(other);
    }

    public int getId() {
        return id;
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
