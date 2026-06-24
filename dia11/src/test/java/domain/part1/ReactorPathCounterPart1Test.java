package domain.part1;

import application.DeviceNetworkParser;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReactorPathCounterPart1Test {
    @Test
    void countsPathsFromOfficialExample() {
        var network = new DeviceNetworkParser().parse(List.of(
                "aaa: you hhh",
                "you: bbb ccc",
                "bbb: ddd eee",
                "ccc: ddd eee fff",
                "ddd: ggg",
                "eee: out",
                "fff: out",
                "ggg: out",
                "hhh: ccc fff iii",
                "iii: out"
        ));

        BigInteger paths = new ReactorPathCounterPart1().countPaths(network);

        assertEquals(BigInteger.valueOf(5), paths);
    }

    @Test
    void rejectsCyclesReachedFromStart() {
        var network = new DeviceNetworkParser().parse(List.of(
                "you: aaa",
                "aaa: you out"
        ));

        assertThrows(IllegalStateException.class, () -> new ReactorPathCounterPart1().countPaths(network));
    }
}
