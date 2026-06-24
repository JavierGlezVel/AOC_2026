package domain.part1;

import domain.common.GridPosition;
import domain.common.TachyonManifold;

import java.util.HashSet;
import java.util.Set;

public class BeamSplitCounterPart1 {
    public int count(TachyonManifold manifold) {
        int splits = 0;
        GridPosition start = manifold.start();
        Set<Integer> activeColumns = Set.of(start.column());

        for (int row = start.row() + 1; row < manifold.height() && !activeColumns.isEmpty(); row++) {
            Set<Integer> nextActiveColumns = new HashSet<>();

            for (int column : activeColumns) {
                GridPosition position = new GridPosition(row, column);
                if (manifold.isSplitterAt(position)) {
                    splits++;
                    addIfInside(manifold, nextActiveColumns, column - 1);
                    addIfInside(manifold, nextActiveColumns, column + 1);
                } else {
                    nextActiveColumns.add(column);
                }
            }

            activeColumns = nextActiveColumns;
        }

        return splits;
    }

    private void addIfInside(TachyonManifold manifold, Set<Integer> columns, int column) {
        if (manifold.containsColumn(column)) {
            columns.add(column);
        }
    }
}
