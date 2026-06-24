package domain.common;

import java.util.List;

public record TreeRegion(int width, int height, List<Integer> presentCounts) {
    public TreeRegion {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Region dimensions must be positive");
        }
        if (presentCounts == null || presentCounts.isEmpty()) {
            throw new IllegalArgumentException("A region needs present counts");
        }
        for (int count : presentCounts) {
            if (count < 0) {
                throw new IllegalArgumentException("Present counts cannot be negative");
            }
        }
        presentCounts = List.copyOf(presentCounts);
    }

    public int area() {
        return width * height;
    }
}
