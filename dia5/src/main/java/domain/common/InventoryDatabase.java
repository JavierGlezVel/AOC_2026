package domain.common;

import java.util.List;

public record InventoryDatabase(
        List<FreshIngredientIdRange> freshRanges,
        List<Long> availableIngredientIds
) {
    public InventoryDatabase {
        if (freshRanges == null || availableIngredientIds == null) {
            throw new IllegalArgumentException("Database sections cannot be null");
        }
        freshRanges = List.copyOf(freshRanges);
        availableIngredientIds = List.copyOf(availableIngredientIds);
    }
}
