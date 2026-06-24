package domain.part1;

import domain.common.BatteryBank;
import domain.common.JoltageCalculator;
import domain.common.MaximumJoltageCalculator;
import domain.common.TotalOutputJoltageCalculator;

import java.util.List;

public class TotalOutputJoltageCalculatorPart1 {
    private final TotalOutputJoltageCalculator calculator;

    public TotalOutputJoltageCalculatorPart1() {
        this(new MaximumJoltageCalculator(2));
    }

    public TotalOutputJoltageCalculatorPart1(JoltageCalculator joltageCalculator) {
        this.calculator = new TotalOutputJoltageCalculator(joltageCalculator);
    }

    public long calculate(List<BatteryBank> banks) {
        return calculator.calculate(banks);
    }
}
