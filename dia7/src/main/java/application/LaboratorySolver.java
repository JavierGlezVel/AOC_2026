package application;

import domain.part1.BeamSplitCounterPart1;
import domain.part2.TimelineCounterPart2;
import infrastructure.DiagramSource;

import java.io.IOException;
import java.math.BigInteger;

public class LaboratorySolver {
    private final DiagramSource source;
    private final TachyonManifoldParser parser;

    public LaboratorySolver(DiagramSource source) {
        this.source = source;
        this.parser = new TachyonManifoldParser();
    }

    public int solvePart1() throws IOException {
        var manifold = parser.parse(source.getLines());
        return new BeamSplitCounterPart1().count(manifold);
    }

    public BigInteger solvePart2() throws IOException {
        var manifold = parser.parse(source.getLines());
        return new TimelineCounterPart2().count(manifold);
    }
}
