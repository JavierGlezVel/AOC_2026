package domain.part1;

import application.BatteryBankParser;
import domain.common.JoltageCalculator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TotalOutputJoltageCalculatorPart1Test {
    @Test
    void sumsMaximumJoltagesFromOfficialExample() {
        var banks = new BatteryBankParser().parse(List.of(
                "987654321111111",
                "811111111111119",
                "234234234234278",
                "818181911112111"
        ));

        long totalOutputJoltage = new TotalOutputJoltageCalculatorPart1().calculate(banks);

        assertEquals(357, totalOutputJoltage);
    }

    @Test
    void canUseAnyCalculatorThatSatisfiesTheJoltageContract() {
        var banks = new BatteryBankParser().parse(List.of("12", "34", "56"));
        JoltageCalculator constantCalculator = bank -> 10;

        long totalOutputJoltage = new TotalOutputJoltageCalculatorPart1(constantCalculator).calculate(banks);

        assertEquals(30, totalOutputJoltage);
    }
}
