package com.github.grimsa.io;

import java.util.regex.Pattern;

public final class PatternMatcher {
    private PatternMatcher() {
    }

    public static int matchOnlyGroupAsInt(final String input, final Pattern pattern) {
        final var matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(input + " does not match " + pattern);
        }

        return Integer.parseInt(matcher.group(1));
    }
}
