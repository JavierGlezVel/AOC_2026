package domain.common;

import java.util.List;
import java.util.Objects;

public class TotalOutputJoltageCalculator {
    private final JoltageCalculator joltageCalculator;

    public TotalOutputJoltageCalculator(JoltageCalculator joltageCalculator) {
        this.joltageCalculator = Objects.requireNonNull(joltageCalculator);
    }

    public long calculate(List<BatteryBank> banks) {
        return banks.stream()
                .mapToLong(joltageCalculator::calculate)
                .sum();
    }
}
