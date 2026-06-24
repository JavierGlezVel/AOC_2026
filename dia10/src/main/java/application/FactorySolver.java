package application;

import domain.part1.MinimumButtonPressesCalculatorPart1;
import domain.part2.MinimumJoltageButtonPressesCalculatorPart2;
import infrastructure.FactoryMachineSource;

import java.io.IOException;

public class FactorySolver {
    private final FactoryMachineSource source;
    private final FactoryMachineParser parser;

    public FactorySolver(FactoryMachineSource source) {
        this.source = source;
        this.parser = new FactoryMachineParser();
    }

    public int solvePart1() throws IOException {
        var machines = parser.parse(source.getLines());
        return new MinimumButtonPressesCalculatorPart1().calculate(machines);
    }

    public long solvePart2() throws IOException {
        var machines = parser.parse(source.getLines());
        return new MinimumJoltageButtonPressesCalculatorPart2().calculate(machines);
    }
}
