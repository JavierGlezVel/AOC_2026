package domain.part2;

import domain.common.BatteryBank;
import domain.common.JoltageCalculator;
import domain.common.MaximumJoltageCalculator;
import domain.common.TotalOutputJoltageCalculator;

import java.util.List;

public class TotalOutputJoltageCalculatorPart2 {
    private final TotalOutputJoltageCalculator calculator;

    public TotalOutputJoltageCalculatorPart2() {
        this(new MaximumJoltageCalculator(12));
    }

    public TotalOutputJoltageCalculatorPart2(JoltageCalculator joltageCalculator) {
        this.calculator = new TotalOutputJoltageCalculator(joltageCalculator);
    }

    public long calculate(List<BatteryBank> banks) {
        return calculator.calculate(banks);
    }
}
