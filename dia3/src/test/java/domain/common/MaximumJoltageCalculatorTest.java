package domain.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MaximumJoltageCalculatorTest {
    private final MaximumJoltageCalculator calculator = new MaximumJoltageCalculator();

    @Test
    void findsMaximumJoltageForEachOfficialExampleBank() {
        assertEquals(98, calculator.calculate(new BatteryBank("987654321111111")));
        assertEquals(89, calculator.calculate(new BatteryBank("811111111111119")));
        assertEquals(78, calculator.calculate(new BatteryBank("234234234234278")));
        assertEquals(92, calculator.calculate(new BatteryBank("818181911112111")));
    }

    @Test
    void keepsTheOriginalBatteryOrder() {
        assertEquals(89, calculator.calculate(new BatteryBank("819")));
    }

    @Test
    void findsMaximumJoltageWithTwelveBatteriesForEachOfficialExampleBank() {
        MaximumJoltageCalculator part2Calculator = new MaximumJoltageCalculator(12);

        assertEquals(987654321111L, part2Calculator.calculate(new BatteryBank("987654321111111")));
        assertEquals(811111111119L, part2Calculator.calculate(new BatteryBank("811111111111119")));
        assertEquals(434234234278L, part2Calculator.calculate(new BatteryBank("234234234234278")));
        assertEquals(888911112111L, part2Calculator.calculate(new BatteryBank("818181911112111")));
    }

    @Test
    void rejectsBanksWithFewerBatteriesThanRequired() {
        MaximumJoltageCalculator part2Calculator = new MaximumJoltageCalculator(12);

        assertThrows(IllegalArgumentException.class, () -> part2Calculator.calculate(new BatteryBank("12345678911")));
    }
}
