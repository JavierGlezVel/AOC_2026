package application;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BatteryBankParserTest {
    @Test
    void parsesOneBankPerLineIgnoringBlankLines() {
        var banks = new BatteryBankParser().parse(List.of(
                "987654321111111",
                "",
                " 811111111111119 "
        ));

        assertEquals(2, banks.size());
        assertEquals("987654321111111", banks.get(0).ratings());
        assertEquals("811111111111119", banks.get(1).ratings());
    }

    @Test
    void rejectsRatingsOutsideTheExpectedRange() {
        BatteryBankParser parser = new BatteryBankParser();

        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("120")));
    }
}
