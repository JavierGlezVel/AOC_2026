package application;

import domain.common.FreshIngredientIdRange;
import domain.common.InventoryDatabase;

import java.util.ArrayList;
import java.util.List;

public class InventoryDatabaseParser {
    public InventoryDatabase parse(List<String> lines) {
        List<FreshIngredientIdRange> freshRanges = new ArrayList<>();
        List<Long> availableIngredientIds = new ArrayList<>();
        boolean parsingAvailableIds = false;

        for (String line : lines) {
            String normalizedLine = line.trim();
            if (normalizedLine.isEmpty()) {
                parsingAvailableIds = true;
                continue;
            }

            if (parsingAvailableIds) {
                availableIngredientIds.add(Long.parseLong(normalizedLine));
            } else {
                freshRanges.add(parseRange(normalizedLine));
            }
        }

        return new InventoryDatabase(freshRanges, availableIngredientIds);
    }

    private FreshIngredientIdRange parseRange(String line) {
        String[] limits = line.split("-");
        if (limits.length != 2) {
            throw new IllegalArgumentException("Invalid range: " + line);
        }

        long firstId = Long.parseLong(limits[0]);
        long lastId = Long.parseLong(limits[1]);
        return new FreshIngredientIdRange(firstId, lastId);
    }
}
