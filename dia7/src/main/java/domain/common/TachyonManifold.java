package domain.common;

import java.util.List;

public record TachyonManifold(List<String> rows) {
    private static final char START = 'S';
    private static final char SPLITTER = '^';
    private static final char EMPTY = '.';

    public TachyonManifold {
        if (rows == null || rows.isEmpty()) {
            throw new IllegalArgumentException("A tachyon manifold needs at least one row");
        }
        rows = List.copyOf(rows);
        int width = rows.getFirst().length();
        if (width == 0) {
            throw new IllegalArgumentException("A tachyon manifold needs at least one column");
        }

        int starts = 0;
        for (String row : rows) {
            validateRow(row, width);
            starts += countStarts(row);
        }
        if (starts != 1) {
            throw new IllegalArgumentException("A tachyon manifold needs exactly one start");
        }
    }

    public int height() {
        return rows.size();
    }

    public int width() {
        return rows.getFirst().length();
    }

    public GridPosition start() {
        for (int row = 0; row < height(); row++) {
            int column = rows.get(row).indexOf(START);
            if (column >= 0) {
                return new GridPosition(row, column);
            }
        }
        throw new IllegalStateException("A validated manifold always has a start");
    }

    public boolean isSplitterAt(GridPosition position) {
        return rows.get(position.row()).charAt(position.column()) == SPLITTER;
    }

    public boolean containsColumn(int column) {
        return 0 <= column && column < width();
    }

    private static void validateRow(String row, int width) {
        if (row == null || row.length() != width) {
            throw new IllegalArgumentException("All rows in a tachyon manifold must have the same width");
        }
        if (!row.matches("[.S^]+")) {
            throw new IllegalArgumentException("Manifold rows can only contain '.', 'S' and '^': " + row);
        }
    }

    private static int countStarts(String row) {
        int starts = 0;
        for (int i = 0; i < row.length(); i++) {
            if (row.charAt(i) == START) {
                starts++;
            }
        }
        return starts;
    }
}
