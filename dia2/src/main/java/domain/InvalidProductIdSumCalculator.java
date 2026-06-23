package domain;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class InvalidProductIdSumCalculator {
    public long calculate(List<ProductIdRange> ranges, Collection<Long> invalidIds) {
        Set<Long> invalidIdsInRanges = new TreeSet<>();
        for (ProductIdRange range : ranges) {
            for (long invalidId : invalidIds) {
                if (range.contains(invalidId)) {
                    invalidIdsInRanges.add(invalidId);
                }
            }
        }
        return invalidIdsInRanges.stream()
                .mapToLong(Long::longValue)
                .sum();
    }
}
