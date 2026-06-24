package domain.part1;

import domain.common.Cell;
import domain.common.PresentShape;
import domain.common.TreeFarmPlan;
import domain.common.TreeRegion;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FittingRegionCounterPart1 {
    private static final int EXACT_SOLVER_MAX_CELLS = 160;

    public int countFittingRegions(TreeFarmPlan plan) {
        if (plan == null) {
            throw new IllegalArgumentException("Tree farm plan cannot be null");
        }

        int fittingRegions = 0;
        for (TreeRegion region : plan.regions()) {
            if (canFit(plan.shapes(), region)) {
                fittingRegions++;
            }
        }
        return fittingRegions;
    }

    private boolean canFit(List<PresentShape> shapes, TreeRegion region) {
        int requiredArea = requiredArea(shapes, region);
        if (requiredArea > region.area()) {
            return false;
        }
        if (requiredArea == 0) {
            return true;
        }
        if (region.area() <= EXACT_SOLVER_MAX_CELLS) {
            return canFitExactly(shapes, region);
        }
        return true;
    }

    private int requiredArea(List<PresentShape> shapes, TreeRegion region) {
        int area = 0;
        for (int index = 0; index < shapes.size(); index++) {
            area += shapes.get(index).area() * region.presentCounts().get(index);
        }
        return area;
    }

    private boolean canFitExactly(List<PresentShape> shapes, TreeRegion region) {
        List<PiecePlacements> pieces = new ArrayList<>();
        for (int shapeIndex = 0; shapeIndex < shapes.size(); shapeIndex++) {
            List<Long> placements = placementsFor(shapes.get(shapeIndex), region);
            for (int count = 0; count < region.presentCounts().get(shapeIndex); count++) {
                pieces.add(new PiecePlacements(shapeIndex, shapes.get(shapeIndex).area(), placements));
            }
        }

        pieces.sort(Comparator
                .comparingInt((PiecePlacements piece) -> piece.placements().size())
                .thenComparing(Comparator.comparingInt(PiecePlacements::area).reversed()));

        Map<SearchState, Boolean> memoizedFailures = new HashMap<>();
        return search(pieces, 0, 0L, memoizedFailures);
    }

    private List<Long> placementsFor(PresentShape shape, TreeRegion region) {
        List<Long> placements = new ArrayList<>();
        for (List<Cell> variant : shape.variants()) {
            int variantWidth = variant.stream().mapToInt(Cell::x).max().orElseThrow() + 1;
            int variantHeight = variant.stream().mapToInt(Cell::y).max().orElseThrow() + 1;
            for (int y = 0; y <= region.height() - variantHeight; y++) {
                for (int x = 0; x <= region.width() - variantWidth; x++) {
                    long mask = 0L;
                    for (Cell cell : variant) {
                        int bit = (y + cell.y()) * region.width() + x + cell.x();
                        mask |= 1L << bit;
                    }
                    placements.add(mask);
                }
            }
        }
        return placements.stream().distinct().toList();
    }

    private boolean search(
            List<PiecePlacements> pieces,
            int pieceIndex,
            long occupiedCells,
            Map<SearchState, Boolean> memoizedFailures
    ) {
        if (pieceIndex == pieces.size()) {
            return true;
        }

        SearchState state = new SearchState(pieceIndex, occupiedCells);
        if (memoizedFailures.containsKey(state)) {
            return false;
        }

        for (long placement : pieces.get(pieceIndex).placements()) {
            if ((occupiedCells & placement) == 0 && search(pieces, pieceIndex + 1, occupiedCells | placement, memoizedFailures)) {
                return true;
            }
        }

        memoizedFailures.put(state, false);
        return false;
    }

    private record PiecePlacements(int shapeIndex, int area, List<Long> placements) {
    }

    private record SearchState(int pieceIndex, long occupiedCells) {
    }
}
