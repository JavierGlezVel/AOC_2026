package domain.part2;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RepeatedAtLeastTwiceProductIdGeneratorTest {
    @Test
    void generatesIdsMadeOfASequenceRepeatedAtLeastTwice() {
        Set<Long> invalidIds = new RepeatedAtLeastTwiceProductIdGenerator().generateUntil(1_300_000_000L);

        assertTrue(invalidIds.contains(12_341_234L));
        assertTrue(invalidIds.contains(123_123_123L));
        assertTrue(invalidIds.contains(1_212_121_212L));
        assertTrue(invalidIds.contains(1_111_111L));
    }
}
