package com.github.grimsa.aoc2018;

import com.github.grimsa.io.ClasspathFileReader;
import com.google.common.collect.*;
import one.util.streamex.EntryStream;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.github.grimsa.io.PatternMatcher.matchOnlyGroupAsInt;

final class Day4 {
    public static void main(final String... args) {
        final var guardShiftRecords = StreamEx.of(new ClasspathFileReader("input/2018/day4.txt").get())
                .sorted()
                .groupRuns((first, second) -> !second.contains("begins shift"))
                .map(GuardShiftRecord::new)
                .peek(gsr -> System.out.println(gsr.guardId + " -> " + gsr.timesAsleep))
                .toList();
        System.out.println("Part one answer: " + solvePartOne(guardShiftRecords));
        System.out.println("Part two answer: " + solvePartTwo(guardShiftRecords));
    }

    private static int solvePartOne(final List<GuardShiftRecord> guardShiftRecords) {
        final var totalTimeAsleep = StreamEx.of(guardShiftRecords)
                .groupingBy(GuardShiftRecord::guardId, Collectors.summingInt(GuardShiftRecord::totalTimeAsleep));
        final var targetGuardId = EntryStream.of(totalTimeAsleep)
                .maxBy(Entry::getValue)
                .map(Entry::getKey)
                .orElseThrow();
        final var recordsOfTargetGuard = StreamEx.of(guardShiftRecords)
                .filterBy(GuardShiftRecord::guardId, targetGuardId)
                .toList();
        final var minuteAsleepMost = IntStreamEx.range(0, 60)
                .maxByInt(minute -> timesAsleepOnMinute(minute, recordsOfTargetGuard))
                .orElseThrow();
        return targetGuardId * minuteAsleepMost;
    }

    private static int solvePartTwo(final List<GuardShiftRecord> guardShiftRecords) {
        final var recordsByGuardId = StreamEx.of(guardShiftRecords).groupingTo(GuardShiftRecord::guardId, ArrayList::new);
        return EntryStream.of(recordsByGuardId)
                .mapKeys(guardId -> EntryStream.zip(
                        Collections.nCopies(60, guardId),
                        IntStreamEx.range(0, 60).boxed().toList())
                )
                .flatMapKeys(Function.identity())
                .mapToValue((guardIdAndMinutePair, records) -> StreamEx.of(records)
                        .filter(record -> record.wasAsleepOnMinute(guardIdAndMinutePair.getValue()))
                        .count()
                )
                .maxByLong(Entry::getValue)
                .map(Entry::getKey)
                .map(guardIdAndMinutePair -> guardIdAndMinutePair.getKey() * guardIdAndMinutePair.getValue())
                .orElseThrow();
    }

    private static int timesAsleepOnMinute(final int minute, final List<GuardShiftRecord> guardShiftRecords) {
        return (int) StreamEx.of(guardShiftRecords).filter(guardShiftRecord -> guardShiftRecord.wasAsleepOnMinute(minute)).count();
    }

    private static class GuardShiftRecord {
        private static final Pattern GUARD_ID_PATTERN = Pattern.compile(".*Guard #(\\d+) begins shift");
        private static final Pattern FALLS_ASLEEP_PATTERN = Pattern.compile(".*00:(\\d{2})] falls asleep");
        private static final Pattern WAKES_UP_PATTERN = Pattern.compile(".*00:(\\d{2})] wakes up");

        private final int guardId;
        private final RangeSet<Integer> timesAsleep;

        GuardShiftRecord(final List<String> shiftRecordEntries) {
            guardId = matchOnlyGroupAsInt(shiftRecordEntries.get(0), GUARD_ID_PATTERN);
            timesAsleep = StreamEx.of(shiftRecordEntries)
                    .skip(1)
                    .intervalMap((first, second) -> first.contains("falls asleep"),
                            (fallsAsleep, wakesUp) -> Range.closedOpen(
                                    matchOnlyGroupAsInt(fallsAsleep, FALLS_ASLEEP_PATTERN),
                                    matchOnlyGroupAsInt(wakesUp, WAKES_UP_PATTERN)
                            ))
                    .collect(TreeRangeSet::create, TreeRangeSet::add, RangeSet::addAll);
        }

        int totalTimeAsleep() {
            return StreamEx.of(timesAsleep.asRanges())
                    .mapToInt(range -> ContiguousSet.create(range, DiscreteDomain.integers()).size())
                    .sum();
        }

        boolean wasAsleepOnMinute(final int minute) {
            return timesAsleep.contains(minute);
        }

        int guardId() {
            return guardId;
        }
    }
}
