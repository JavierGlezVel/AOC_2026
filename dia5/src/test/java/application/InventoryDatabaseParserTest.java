package application;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InventoryDatabaseParserTest {
    @Test
    void parsesFreshRangesAndAvailableIngredientIds() {
        var database = new InventoryDatabaseParser().parse(List.of(
                "3-5",
                "10-14",
                "",
                "1",
                "5"
        ));

        assertEquals(2, database.freshRanges().size());
        assertEquals(2, database.availableIngredientIds().size());
        assertEquals(3, database.freshRanges().getFirst().firstId());
        assertEquals(5, database.availableIngredientIds().getLast());
    }

    @Test
    void rejectsInvalidRanges() {
        InventoryDatabaseParser parser = new InventoryDatabaseParser();

        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("5-3")));
    }
}
