package domain.part2;

import domain.common.InvalidProductIdSumCalculator;
import domain.common.ProductIdRange;

import java.util.Set;
import java.util.List;

public class InvalidProductIdSumCalculatorPart2 {
    public long calculate(List<ProductIdRange> ranges) {
        long maxId = ranges.stream()
                .mapToLong(ProductIdRange::lastId)
                .max()
                .orElse(0);

        Set<Long> invalidIds = new RepeatedAtLeastTwiceProductIdGenerator().generateUntil(maxId);
        return new InvalidProductIdSumCalculator().calculate(ranges, invalidIds);
    }
}
