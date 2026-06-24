package domain.part2;

import domain.common.GridPosition;
import domain.common.TachyonManifold;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class TimelineCounterPart2 {
    public BigInteger count(TachyonManifold manifold) {
        GridPosition start = manifold.start();
        Map<Integer, BigInteger> activeTimelines = Map.of(start.column(), BigInteger.ONE);
        BigInteger completedTimelines = BigInteger.ZERO;

        for (int row = start.row() + 1; row < manifold.height() && !activeTimelines.isEmpty(); row++) {
            Map<Integer, BigInteger> nextActiveTimelines = new HashMap<>();

            for (Map.Entry<Integer, BigInteger> entry : activeTimelines.entrySet()) {
                int column = entry.getKey();
                BigInteger timelines = entry.getValue();
                GridPosition position = new GridPosition(row, column);

                if (manifold.isSplitterAt(position)) {
                    completedTimelines = split(manifold, nextActiveTimelines, completedTimelines, column - 1, timelines);
                    completedTimelines = split(manifold, nextActiveTimelines, completedTimelines, column + 1, timelines);
                } else {
                    addTimelines(nextActiveTimelines, column, timelines);
                }
            }

            activeTimelines = nextActiveTimelines;
        }

        return completedTimelines.add(activeTimelines.values().stream()
                .reduce(BigInteger.ZERO, BigInteger::add));
    }

    private BigInteger split(TachyonManifold manifold,
                             Map<Integer, BigInteger> activeTimelines,
                             BigInteger completedTimelines,
                             int column,
                             BigInteger timelines) {
        if (manifold.containsColumn(column)) {
            addTimelines(activeTimelines, column, timelines);
            return completedTimelines;
        }
        return completedTimelines.add(timelines);
    }

    private void addTimelines(Map<Integer, BigInteger> activeTimelines, int column, BigInteger timelines) {
        activeTimelines.merge(column, timelines, BigInteger::add);
    }
}
