package domain.common;

import java.util.List;

public record TreeFarmPlan(List<PresentShape> shapes, List<TreeRegion> regions) {
    public TreeFarmPlan {
        if (shapes == null || shapes.isEmpty()) {
            throw new IllegalArgumentException("The farm plan needs at least one present shape");
        }
        if (regions == null || regions.isEmpty()) {
            throw new IllegalArgumentException("The farm plan needs at least one tree region");
        }
        for (int index = 0; index < shapes.size(); index++) {
            if (shapes.get(index).index() != index) {
                throw new IllegalArgumentException("Present shapes must be indexed from zero without gaps");
            }
        }
        for (TreeRegion region : regions) {
            if (region.presentCounts().size() != shapes.size()) {
                throw new IllegalArgumentException("Each region needs one count per present shape");
            }
        }
        shapes = List.copyOf(shapes);
        regions = List.copyOf(regions);
    }
}
