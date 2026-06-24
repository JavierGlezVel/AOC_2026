package domain.part2;

import application.InventoryDatabaseParser;
import domain.common.FreshIngredientIdRange;
import domain.common.InventoryDatabase;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FreshIngredientIdCoverageCounterPart2Test {
    @Test
    void countsFreshIdsCoveredByRangesFromOfficialExample() {
        var database = new InventoryDatabaseParser().parse(List.of(
                "3-5",
                "10-14",
                "16-20",
                "12-18",
                "",
                "1",
                "5",
                "8",
                "11",
                "17",
                "32"
        ));

        long freshIngredientIds = new FreshIngredientIdCoverageCounterPart2().count(database);

        assertEquals(14, freshIngredientIds);
    }

    @Test
    void countsOverlappingRangesOnlyOnce() {
        InventoryDatabase database = new InventoryDatabase(
                List.of(
                        new FreshIngredientIdRange(10, 20),
                        new FreshIngredientIdRange(15, 25),
                        new FreshIngredientIdRange(30, 30)
                ),
                List.of()
        );

        long freshIngredientIds = new FreshIngredientIdCoverageCounterPart2().count(database);

        assertEquals(17, freshIngredientIds);
    }
}
