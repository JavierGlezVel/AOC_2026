package domain.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record PresentShape(int index, List<Cell> cells) {
    public PresentShape {
        if (index < 0) {
            throw new IllegalArgumentException("Shape index cannot be negative");
        }
        if (cells == null || cells.isEmpty()) {
            throw new IllegalArgumentException("A present shape needs at least one occupied cell");
        }
        cells = normalize(cells);
    }

    public int area() {
        return cells.size();
    }

    public List<List<Cell>> variants() {
        Set<List<Cell>> uniqueVariants = new HashSet<>();
        for (int reflection : List.of(1, -1)) {
            for (int rotation = 0; rotation < 4; rotation++) {
                List<Cell> transformed = new ArrayList<>();
                for (Cell cell : cells) {
                    int x = cell.x() * reflection;
                    int y = cell.y();
                    for (int turn = 0; turn < rotation; turn++) {
                        int nextX = -y;
                        y = x;
                        x = nextX;
                    }
                    transformed.add(new Cell(x, y));
                }
                uniqueVariants.add(normalize(transformed));
            }
        }
        return List.copyOf(uniqueVariants);
    }

    public static List<Cell> normalize(List<Cell> cells) {
        int minX = cells.stream().mapToInt(Cell::x).min().orElseThrow();
        int minY = cells.stream().mapToInt(Cell::y).min().orElseThrow();
        return cells.stream()
                .map(cell -> new Cell(cell.x() - minX, cell.y() - minY))
                .distinct()
                .sorted(java.util.Comparator.comparingInt(Cell::y).thenComparingInt(Cell::x))
                .toList();
    }
}
