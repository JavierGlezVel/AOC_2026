package domain.part1;

import application.FactoryMachineParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MinimumButtonPressesCalculatorPart1Test {
    @Test
    void sumsMinimumPressesFromOfficialExample() {
        var machines = new FactoryMachineParser().parse(List.of(
                "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}",
                "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}",
                "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}"
        ));

        int minimumPresses = new MinimumButtonPressesCalculatorPart1().calculate(machines);

        assertEquals(7, minimumPresses);
    }

    @Test
    void repeatedButtonPressesAreNotNeededForLightToggles() {
        var machines = new FactoryMachineParser().parse(List.of(
                "[##] (0) (1) (0,1) {1,1}"
        ));

        int minimumPresses = new MinimumButtonPressesCalculatorPart1().calculate(machines);

        assertEquals(1, minimumPresses);
    }
}
