package domain.part1;

import application.TachyonManifoldParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BeamSplitCounterPart1Test {
    @Test
    void countsSplitsFromOfficialExample() {
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

        int splits = new BeamSplitCounterPart1().count(manifold);

        assertEquals(21, splits);
    }

    @Test
    void mergesBeamsThatReachTheSameColumn() {
        var manifold = new TachyonManifoldParser().parse(List.of(
                "..S..",
                "..^..",
                ".^.^.",
                "....."
        ));

        int splits = new BeamSplitCounterPart1().count(manifold);

        assertEquals(3, splits);
    }
}
