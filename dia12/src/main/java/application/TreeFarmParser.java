package application;

import domain.common.Cell;
import domain.common.PresentShape;
import domain.common.TreeFarmPlan;
import domain.common.TreeRegion;

import java.util.ArrayList;
import java.util.List;

public class TreeFarmParser {
    public TreeFarmPlan parse(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("The tree farm input cannot be empty");
        }

        List<PresentShape> shapes = new ArrayList<>();
        List<TreeRegion> regions = new ArrayList<>();
        int lineIndex = 0;

        while (lineIndex < lines.size() && lines.get(lineIndex).matches("\\d+:")) {
            int shapeIndex = parseShapeIndex(lines.get(lineIndex));
            lineIndex++;
            List<Cell> cells = new ArrayList<>();
            int y = 0;
            while (lineIndex < lines.size() && !lines.get(lineIndex).isBlank()) {
                String row = lines.get(lineIndex);
                for (int x = 0; x < row.length(); x++) {
                    char cell = row.charAt(x);
                    if (cell == '#') {
                        cells.add(new Cell(x, y));
                    } else if (cell != '.') {
                        throw new IllegalArgumentException("Unexpected shape cell: " + cell);
                    }
                }
                lineIndex++;
                y++;
            }
            shapes.add(new PresentShape(shapeIndex, cells));
            while (lineIndex < lines.size() && lines.get(lineIndex).isBlank()) {
                lineIndex++;
            }
        }

        while (lineIndex < lines.size()) {
            if (!lines.get(lineIndex).isBlank()) {
                regions.add(parseRegion(lines.get(lineIndex)));
            }
            lineIndex++;
        }

        return new TreeFarmPlan(shapes, regions);
    }

    private int parseShapeIndex(String line) {
        if (!line.endsWith(":")) {
            throw new IllegalArgumentException("Invalid shape header: " + line);
        }
        return Integer.parseInt(line.substring(0, line.length() - 1));
    }

    private TreeRegion parseRegion(String line) {
        String[] parts = line.split(": ", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid region description: " + line);
        }

        String[] dimensions = parts[0].split("x", 2);
        if (dimensions.length != 2) {
            throw new IllegalArgumentException("Invalid region dimensions: " + line);
        }

        List<Integer> counts = new ArrayList<>();
        for (String countText : parts[1].split(" ")) {
            counts.add(Integer.parseInt(countText));
        }
        return new TreeRegion(Integer.parseInt(dimensions[0]), Integer.parseInt(dimensions[1]), counts);
    }
}
