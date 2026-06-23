package application;

import domain.PasswordCalculatorPart1;
import domain.PasswordCalculatorPart2;
import infrastructure.RotationSource;

import java.io.IOException;

public class SafeSolver {
    private final RotationSource source;
    private final RotationParser parser;

    public SafeSolver(RotationSource source) {
        this.source = source;
        this.parser = new RotationParser();
    }

    public int solvePart1() throws IOException {
        var lines = source.getLines();
        var rotations = parser.parse(lines);
        return new PasswordCalculatorPart1().calculate(rotations);
    }

    public int solvePart2() throws IOException {
        var lines = source.getLines();
        var rotations = parser.parse(lines);
        return new PasswordCalculatorPart2().calculate(rotations);
    }
}
