package domain.part1;

import domain.common.FreshIngredientIdRange;
import domain.common.InventoryDatabase;

public class FreshIngredientCounterPart1 {
    public int count(InventoryDatabase database) {
        int freshIngredientIds = 0;

        for (long ingredientId : database.availableIngredientIds()) {
            if (isFresh(ingredientId, database)) {
                freshIngredientIds++;
            }
        }

        return freshIngredientIds;
    }

    private boolean isFresh(long ingredientId, InventoryDatabase database) {
        for (FreshIngredientIdRange range : database.freshRanges()) {
            if (range.contains(ingredientId)) {
                return true;
            }
        }
        return false;
    }
}
