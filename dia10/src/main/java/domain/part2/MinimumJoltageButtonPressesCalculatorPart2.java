package domain.part2;

import domain.common.FactoryMachine;

import java.util.ArrayList;
import java.util.List;

public class MinimumJoltageButtonPressesCalculatorPart2 {
    private static final long NO_SOLUTION = Long.MAX_VALUE;

    public long calculate(List<FactoryMachine> machines) {
        if (machines == null || machines.isEmpty()) {
            throw new IllegalArgumentException("At least one machine is needed");
        }

        return machines.stream()
                .mapToLong(this::minimumPresses)
                .sum();
    }

    private long minimumPresses(FactoryMachine machine) {
        LinearSystem system = reduce(machine);
        long best = enumerateFreeVariables(machine, system, 0, new long[system.freeColumns().size()], 0, NO_SOLUTION);
        if (best == NO_SOLUTION) {
            throw new IllegalStateException("Machine cannot match the joltage requirements");
        }
        return best;
    }

    private LinearSystem reduce(FactoryMachine machine) {
        int counterCount = machine.joltageRequirements().size();
        int buttonCount = machine.buttonMasks().size();
        Fraction[][] matrix = new Fraction[counterCount][buttonCount + 1];

        for (int counter = 0; counter < counterCount; counter++) {
            for (int button = 0; button < buttonCount; button++) {
                boolean affectsCounter = (machine.buttonMasks().get(button) & (1 << counter)) != 0;
                matrix[counter][button] = Fraction.of(affectsCounter ? 1 : 0);
            }
            matrix[counter][buttonCount] = Fraction.of(machine.joltageRequirements().get(counter));
        }

        List<Integer> pivotColumns = new ArrayList<>();
        int rank = 0;
        for (int column = 0; column < buttonCount && rank < counterCount; column++) {
            int pivotRow = findPivotRow(matrix, rank, column);
            if (pivotRow == -1) {
                continue;
            }

            swapRows(matrix, rank, pivotRow);
            Fraction pivotValue = matrix[rank][column];
            for (int currentColumn = 0; currentColumn <= buttonCount; currentColumn++) {
                matrix[rank][currentColumn] = matrix[rank][currentColumn].divide(pivotValue);
            }

            for (int row = 0; row < counterCount; row++) {
                if (row == rank || matrix[row][column].isZero()) {
                    continue;
                }
                Fraction factor = matrix[row][column];
                for (int currentColumn = 0; currentColumn <= buttonCount; currentColumn++) {
                    matrix[row][currentColumn] = matrix[row][currentColumn]
                            .subtract(factor.multiply(matrix[rank][currentColumn]));
                }
            }

            pivotColumns.add(column);
            rank++;
        }

        validateConsistent(matrix, rank, buttonCount);

        boolean[] pivotColumn = new boolean[buttonCount];
        for (int column : pivotColumns) {
            pivotColumn[column] = true;
        }

        List<Integer> freeColumns = new ArrayList<>();
        for (int column = 0; column < buttonCount; column++) {
            if (!pivotColumn[column]) {
                freeColumns.add(column);
            }
        }

        return new LinearSystem(matrix, pivotColumns, freeColumns, rank);
    }

    private int findPivotRow(Fraction[][] matrix, int startRow, int column) {
        for (int row = startRow; row < matrix.length; row++) {
            if (!matrix[row][column].isZero()) {
                return row;
            }
        }
        return -1;
    }

    private void swapRows(Fraction[][] matrix, int first, int second) {
        if (first == second) {
            return;
        }
        Fraction[] temporary = matrix[first];
        matrix[first] = matrix[second];
        matrix[second] = temporary;
    }

    private void validateConsistent(Fraction[][] matrix, int rank, int buttonCount) {
        for (int row = rank; row < matrix.length; row++) {
            boolean allZero = true;
            for (int column = 0; column < buttonCount; column++) {
                if (!matrix[row][column].isZero()) {
                    allZero = false;
                    break;
                }
            }
            if (allZero && !matrix[row][buttonCount].isZero()) {
                throw new IllegalStateException("Machine joltage system is inconsistent");
            }
        }
    }

    private long enumerateFreeVariables(
            FactoryMachine machine,
            LinearSystem system,
            int freeIndex,
            long[] freeValues,
            long freePresses,
            long best
    ) {
        if (freePresses >= best) {
            return best;
        }
        if (freeIndex == system.freeColumns().size()) {
            return Math.min(best, evaluateSolution(machine, system, freeValues, freePresses, best));
        }

        int button = system.freeColumns().get(freeIndex);
        long maxPresses = maxPressesForButton(machine, button);
        for (long presses = 0; presses <= maxPresses; presses++) {
            freeValues[freeIndex] = presses;
            best = enumerateFreeVariables(machine, system, freeIndex + 1, freeValues, freePresses + presses, best);
        }
        return best;
    }

    private long evaluateSolution(
            FactoryMachine machine,
            LinearSystem system,
            long[] freeValues,
            long freePresses,
            long best
    ) {
        long totalPresses = freePresses;
        int buttonCount = machine.buttonMasks().size();

        for (int pivotRow = 0; pivotRow < system.rank(); pivotRow++) {
            Fraction value = system.matrix()[pivotRow][buttonCount];
            for (int freeIndex = 0; freeIndex < system.freeColumns().size(); freeIndex++) {
                int freeColumn = system.freeColumns().get(freeIndex);
                value = value.subtract(system.matrix()[pivotRow][freeColumn].multiply(freeValues[freeIndex]));
            }

            if (!value.isInteger() || value.isNegative()) {
                return NO_SOLUTION;
            }

            totalPresses += value.asLong();
            if (totalPresses >= best) {
                return NO_SOLUTION;
            }
        }

        return totalPresses;
    }

    private long maxPressesForButton(FactoryMachine machine, int button) {
        int buttonMask = machine.buttonMasks().get(button);
        long maxPresses = Long.MAX_VALUE;
        for (int counter = 0; counter < machine.joltageRequirements().size(); counter++) {
            if ((buttonMask & (1 << counter)) != 0) {
                maxPresses = Math.min(maxPresses, machine.joltageRequirements().get(counter));
            }
        }
        return maxPresses;
    }

    private record LinearSystem(
            Fraction[][] matrix,
            List<Integer> pivotColumns,
            List<Integer> freeColumns,
            int rank
    ) {
    }

    private record Fraction(long numerator, long denominator) {
        private Fraction {
            if (denominator == 0) {
                throw new IllegalArgumentException("Denominator cannot be zero");
            }
            if (denominator < 0) {
                numerator = -numerator;
                denominator = -denominator;
            }
            long gcd = gcd(Math.abs(numerator), denominator);
            numerator /= gcd;
            denominator /= gcd;
        }

        static Fraction of(long value) {
            return new Fraction(value, 1);
        }

        Fraction subtract(Fraction other) {
            return new Fraction(
                    numerator * other.denominator - other.numerator * denominator,
                    denominator * other.denominator
            );
        }

        Fraction multiply(Fraction other) {
            return new Fraction(numerator * other.numerator, denominator * other.denominator);
        }

        Fraction multiply(long value) {
            return new Fraction(numerator * value, denominator);
        }

        Fraction divide(Fraction other) {
            return new Fraction(numerator * other.denominator, denominator * other.numerator);
        }

        boolean isZero() {
            return numerator == 0;
        }

        boolean isInteger() {
            return denominator == 1;
        }

        boolean isNegative() {
            return numerator < 0;
        }

        long asLong() {
            return numerator;
        }

        private static long gcd(long first, long second) {
            while (second != 0) {
                long temporary = first % second;
                first = second;
                second = temporary;
            }
            return first == 0 ? 1 : first;
        }
    }
}
