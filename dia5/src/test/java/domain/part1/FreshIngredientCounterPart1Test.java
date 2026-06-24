package domain.part1;

import application.InventoryDatabaseParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FreshIngredientCounterPart1Test {
    @Test
    void countsFreshAvailableIngredientsFromOfficialExample() {
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

        int freshIngredientIds = new FreshIngredientCounterPart1().count(database);

        assertEquals(3, freshIngredientIds);
    }
}
