package domain.common;

public record FreshIngredientIdRange(long firstId, long lastId) {
    public FreshIngredientIdRange {
        if (firstId < 0 || lastId < 0) {
            throw new IllegalArgumentException("Range limits must be >= 0");
        }
        if (firstId > lastId) {
            throw new IllegalArgumentException("First ID must be <= last ID");
        }
    }

    public boolean contains(long ingredientId) {
        return firstId <= ingredientId && ingredientId <= lastId;
    }

    public boolean overlapsOrTouches(FreshIngredientIdRange other) {
        return firstId <= other.lastId + 1 && other.firstId <= lastId + 1;
    }

    public FreshIngredientIdRange merge(FreshIngredientIdRange other) {
        if (!overlapsOrTouches(other)) {
            throw new IllegalArgumentException("Ranges do not overlap or touch");
        }
        return new FreshIngredientIdRange(
                Math.min(firstId, other.firstId),
                Math.max(lastId, other.lastId)
        );
    }

    public long size() {
        return lastId - firstId + 1;
    }
}
