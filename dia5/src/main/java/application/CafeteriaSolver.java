package application;

import domain.part1.FreshIngredientCounterPart1;
import domain.part2.FreshIngredientIdCoverageCounterPart2;
import infrastructure.DatabaseSource;

import java.io.IOException;

public class CafeteriaSolver {
    private final DatabaseSource source;
    private final InventoryDatabaseParser parser;

    public CafeteriaSolver(DatabaseSource source) {
        this.source = source;
        this.parser = new InventoryDatabaseParser();
    }

    public int solvePart1() throws IOException {
        var database = parser.parse(source.getLines());
        return new FreshIngredientCounterPart1().count(database);
    }

    public long solvePart2() throws IOException {
        var database = parser.parse(source.getLines());
        return new FreshIngredientIdCoverageCounterPart2().count(database);
    }
}
