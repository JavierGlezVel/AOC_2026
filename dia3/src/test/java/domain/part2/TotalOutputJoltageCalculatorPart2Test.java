package domain.part2;

import application.BatteryBankParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TotalOutputJoltageCalculatorPart2Test {
    @Test
    void sumsMaximumJoltagesFromOfficialExample() {
        var banks = new BatteryBankParser().parse(List.of(
                "987654321111111",
                "811111111111119",
                "234234234234278",
                "818181911112111"
        ));

        long totalOutputJoltage = new TotalOutputJoltageCalculatorPart2().calculate(banks);

        assertEquals(3121910778619L, totalOutputJoltage);
    }
}
