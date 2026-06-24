package application;

import domain.part1.FittingRegionCounterPart1;
import infrastructure.TreeFarmSource;

import java.io.IOException;

public class TreeFarmSolver {
    private final TreeFarmSource source;
    private final TreeFarmParser parser;

    public TreeFarmSolver(TreeFarmSource source) {
        this.source = source;
        this.parser = new TreeFarmParser();
    }

    public int solvePart1() throws IOException {
        var plan = parser.parse(source.getLines());
        return new FittingRegionCounterPart1().countFittingRegions(plan);
    }
}
