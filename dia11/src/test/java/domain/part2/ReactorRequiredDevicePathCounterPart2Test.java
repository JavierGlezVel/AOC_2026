package domain.part2;

import application.DeviceNetworkParser;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReactorRequiredDevicePathCounterPart2Test {
    @Test
    void countsPathsFromOfficialExampleThatVisitBothRequiredDevices() {
        var network = new DeviceNetworkParser().parse(List.of(
                "svr: aaa bbb",
                "aaa: fft",
                "fft: ccc",
                "bbb: tty",
                "tty: ccc",
                "ccc: ddd eee",
                "ddd: hub",
                "hub: fff",
                "eee: dac",
                "dac: fff",
                "fff: ggg hhh",
                "ggg: out",
                "hhh: out",
                "you: svr"
        ));

        BigInteger paths = new ReactorRequiredDevicePathCounterPart2().countPaths(network);

        assertEquals(BigInteger.valueOf(2), paths);
    }

    @Test
    void ignoresPathsThatMissOneRequiredDevice() {
        var network = new DeviceNetworkParser().parse(List.of(
                "svr: dac fft",
                "dac: out",
                "fft: out",
                "you: svr"
        ));

        BigInteger paths = new ReactorRequiredDevicePathCounterPart2().countPaths(network);

        assertEquals(BigInteger.ZERO, paths);
    }
}
