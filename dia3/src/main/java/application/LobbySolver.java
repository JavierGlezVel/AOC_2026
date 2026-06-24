package application;

import domain.part1.TotalOutputJoltageCalculatorPart1;
import domain.part2.TotalOutputJoltageCalculatorPart2;
import infrastructure.BatteryBankSource;

import java.io.IOException;

public class LobbySolver {
    private final BatteryBankSource source;
    private final BatteryBankParser parser;

    public LobbySolver(BatteryBankSource source) {
        this.source = source;
        this.parser = new BatteryBankParser();
    }

    public long solvePart1() throws IOException {
        var banks = parser.parse(source.getLines());
        return new TotalOutputJoltageCalculatorPart1().calculate(banks);
    }

    public long solvePart2() throws IOException {
        var banks = parser.parse(source.getLines());
        return new TotalOutputJoltageCalculatorPart2().calculate(banks);
    }
}
