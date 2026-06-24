package domain.part2;

import domain.common.DeviceNetwork;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ReactorRequiredDevicePathCounterPart2 {
    private static final String START_DEVICE = "svr";
    private static final String FIRST_REQUIRED_DEVICE = "dac";
    private static final String SECOND_REQUIRED_DEVICE = "fft";

    public BigInteger countPaths(DeviceNetwork network) {
        if (network == null) {
            throw new IllegalArgumentException("Device network cannot be null");
        }

        return countFrom(new PathState(START_DEVICE, false, false), network, new HashMap<>(), new HashSet<>());
    }

    private BigInteger countFrom(
            PathState state,
            DeviceNetwork network,
            Map<PathState, BigInteger> memoizedPaths,
            Set<PathState> visiting
    ) {
        PathState updatedState = state.withVisitedDevice();
        if (DeviceNetwork.OUTPUT_DEVICE.equals(updatedState.device())) {
            return updatedState.hasVisitedBothRequiredDevices() ? BigInteger.ONE : BigInteger.ZERO;
        }
        if (memoizedPaths.containsKey(updatedState)) {
            return memoizedPaths.get(updatedState);
        }
        if (!visiting.add(updatedState)) {
            throw new IllegalStateException("Cycle detected while counting paths through: " + updatedState.device());
        }

        BigInteger totalPaths = BigInteger.ZERO;
        for (String output : network.outputsFrom(updatedState.device())) {
            totalPaths = totalPaths.add(countFrom(updatedState.moveTo(output), network, memoizedPaths, visiting));
        }

        visiting.remove(updatedState);
        memoizedPaths.put(updatedState, totalPaths);
        return totalPaths;
    }

    private record PathState(String device, boolean visitedDac, boolean visitedFft) {
        PathState withVisitedDevice() {
            return new PathState(
                    device,
                    visitedDac || FIRST_REQUIRED_DEVICE.equals(device),
                    visitedFft || SECOND_REQUIRED_DEVICE.equals(device)
            );
        }

        PathState moveTo(String nextDevice) {
            return new PathState(nextDevice, visitedDac, visitedFft);
        }

        boolean hasVisitedBothRequiredDevices() {
            return visitedDac && visitedFft;
        }
    }
}
