package application;

import domain.part1.ReactorPathCounterPart1;
import domain.part2.ReactorRequiredDevicePathCounterPart2;
import infrastructure.DeviceNetworkSource;

import java.io.IOException;
import java.math.BigInteger;

public class ReactorSolver {
    private final DeviceNetworkSource source;
    private final DeviceNetworkParser parser;

    public ReactorSolver(DeviceNetworkSource source) {
        this.source = source;
        this.parser = new DeviceNetworkParser();
    }

    public BigInteger solvePart1() throws IOException {
        var network = parser.parse(source.getLines());
        return new ReactorPathCounterPart1().countPaths(network);
    }

    public BigInteger solvePart2() throws IOException {
        var network = parser.parse(source.getLines());
        return new ReactorRequiredDevicePathCounterPart2().countPaths(network);
    }
}
