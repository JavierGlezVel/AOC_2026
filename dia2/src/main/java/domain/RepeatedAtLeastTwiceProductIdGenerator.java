package domain;

import java.util.Set;
import java.util.TreeSet;

public class RepeatedAtLeastTwiceProductIdGenerator {
    public Set<Long> generateUntil(long maxId) {
        Set<Long> invalidIds = new TreeSet<>();
        int maxDigits = String.valueOf(maxId).length();

        for (int blockLength = 1; blockLength <= maxDigits / 2; blockLength++) {
            long firstBlock = powerOfTen(blockLength - 1);
            long lastBlock = powerOfTen(blockLength) - 1;
            for (long block = firstBlock; block <= lastBlock; block++) {
                addRepeatedIds(block, maxDigits, maxId, invalidIds);
            }
        }
        return invalidIds;
    }

    private void addRepeatedIds(long block, int maxDigits, long maxId, Set<Long> invalidIds) {
        String blockDigits = String.valueOf(block);
        StringBuilder repeatedDigits = new StringBuilder();

        for (int repetitions = 1; repeatedDigits.length() <= maxDigits; repetitions++) {
            repeatedDigits.append(blockDigits);
            if (repetitions < 2) {
                continue;
            }

            long invalidId = Long.parseLong(repeatedDigits.toString());
            if (invalidId > maxId) {
                return;
            }
            invalidIds.add(invalidId);
        }
    }

    private long powerOfTen(int exponent) {
        long value = 1;
        for (int i = 0; i < exponent; i++) {
            value *= 10;
        }
        return value;
    }
}
