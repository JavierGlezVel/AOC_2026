package application;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FactoryMachineParserTest {
    @Test
    void parsesMachineDescriptionIgnoringJoltageRequirements() {
        var machines = new FactoryMachineParser().parse(List.of(
                "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}"
        ));

        var machine = machines.getFirst();

        assertEquals(4, machine.lightCount());
        assertEquals(0b0110, machine.targetMask());
        assertEquals(List.of(0b1000, 0b1010, 0b0100, 0b1100, 0b0101, 0b0011), machine.buttonMasks());
        assertEquals(List.of(3, 5, 4, 7), machine.joltageRequirements());
    }

    @Test
    void rejectsInvalidMachineDescriptions() {
        FactoryMachineParser parser = new FactoryMachineParser();

        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("[.#] {1,2}")));
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("[.#] (2) {1,2}")));
    }
}
