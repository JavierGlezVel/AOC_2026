package domain.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FreshIngredientIdRangeTest {
    @Test
    void containsBothLimits() {
        FreshIngredientIdRange range = new FreshIngredientIdRange(3, 5);

        assertTrue(range.contains(3));
        assertTrue(range.contains(5));
        assertFalse(range.contains(6));
    }

    @Test
    void mergesOverlappingOrTouchingRanges() {
        FreshIngredientIdRange first = new FreshIngredientIdRange(3, 5);
        FreshIngredientIdRange second = new FreshIngredientIdRange(6, 10);

        FreshIngredientIdRange merged = first.merge(second);

        assertEquals(new FreshIngredientIdRange(3, 10), merged);
        assertEquals(8, merged.size());
    }
}
