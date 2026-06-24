package domain.part1;

import domain.common.DeviceNetwork;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ReactorPathCounterPart1 {
    public BigInteger countPaths(DeviceNetwork network) {
        if (network == null) {
            throw new IllegalArgumentException("Device network cannot be null");
        }

        return countFrom(DeviceNetwork.START_DEVICE, network, new HashMap<>(), new HashSet<>());
    }

    private BigInteger countFrom(
            String device,
            DeviceNetwork network,
            Map<String, BigInteger> memoizedPaths,
            Set<String> visiting
    ) {
        if (DeviceNetwork.OUTPUT_DEVICE.equals(device)) {
            return BigInteger.ONE;
        }
        if (memoizedPaths.containsKey(device)) {
            return memoizedPaths.get(device);
        }
        if (!visiting.add(device)) {
            throw new IllegalStateException("Cycle detected while counting paths through: " + device);
        }

        BigInteger totalPaths = BigInteger.ZERO;
        for (String output : network.outputsFrom(device)) {
            totalPaths = totalPaths.add(countFrom(output, network, memoizedPaths, visiting));
        }

        visiting.remove(device);
        memoizedPaths.put(device, totalPaths);
        return totalPaths;
    }
}
