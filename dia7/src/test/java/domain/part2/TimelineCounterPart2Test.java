package domain.part2;

import application.TachyonManifoldParser;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimelineCounterPart2Test {
    @Test
    void countsTimelinesFromOfficialExample() {
        var manifold = new TachyonManifoldParser().parse(List.of(
                ".......S.......",
                "...............",
                ".......^.......",
                "...............",
                "......^.^......",
                "...............",
                ".....^.^.^.....",
                "...............",
                "....^.^...^....",
                "...............",
                "...^.^...^.^...",
                "...............",
                "..^...^.....^..",
                "...............",
                ".^.^.^.^.^...^.",
                "..............."
        ));

        BigInteger timelines = new TimelineCounterPart2().count(manifold);

        assertEquals(BigInteger.valueOf(40), timelines);
    }

    @Test
    void keepsDifferentTimelinesThatReachTheSameColumn() {
        var manifold = new TachyonManifoldParser().parse(List.of(
                "..S..",
                "..^..",
                ".^.^.",
                "....."
        ));

        BigInteger timelines = new TimelineCounterPart2().count(manifold);

        assertEquals(BigInteger.valueOf(4), timelines);
    }

    @Test
    void countsTimelinesThatLeaveTheManifoldFromTheSide() {
        var manifold = new TachyonManifoldParser().parse(List.of(
                "S..",
                "^..",
                "...",
                "..."
        ));

        BigInteger timelines = new TimelineCounterPart2().count(manifold);

        assertEquals(BigInteger.valueOf(2), timelines);
    }
}
