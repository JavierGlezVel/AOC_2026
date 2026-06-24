package domain.part2;

import application.FactoryMachineParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MinimumJoltageButtonPressesCalculatorPart2Test {
    @Test
    void sumsMinimumPressesFromOfficialExample() {
        var machines = new FactoryMachineParser().parse(List.of(
                "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}",
                "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}",
                "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}"
        ));

        long minimumPresses = new MinimumJoltageButtonPressesCalculatorPart2().calculate(machines);

        assertEquals(33, minimumPresses);
    }

    @Test
    void buttonsCanBePressedMultipleTimesForJoltageCounters() {
        var machines = new FactoryMachineParser().parse(List.of(
                "[##] (0,1) (0) {4,2}"
        ));

        long minimumPresses = new MinimumJoltageButtonPressesCalculatorPart2().calculate(machines);

        assertEquals(4, minimumPresses);
    }
}
