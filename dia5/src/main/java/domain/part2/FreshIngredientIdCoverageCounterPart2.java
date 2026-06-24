package domain.part2;

import domain.common.FreshIngredientIdRange;
import domain.common.InventoryDatabase;

import java.util.Comparator;
import java.util.List;

public class FreshIngredientIdCoverageCounterPart2 {
    public long count(InventoryDatabase database) {
        List<FreshIngredientIdRange> sortedRanges = database.freshRanges().stream()
                .sorted(Comparator.comparingLong(FreshIngredientIdRange::firstId))
                .toList();

        long freshIngredientIds = 0;
        FreshIngredientIdRange currentRange = null;

        for (FreshIngredientIdRange range : sortedRanges) {
            if (currentRange == null) {
                currentRange = range;
                continue;
            }

            if (currentRange.overlapsOrTouches(range)) {
                currentRange = currentRange.merge(range);
            } else {
                freshIngredientIds += currentRange.size();
                currentRange = range;
            }
        }

        if (currentRange != null) {
            freshIngredientIds += currentRange.size();
        }

        return freshIngredientIds;
    }
}
