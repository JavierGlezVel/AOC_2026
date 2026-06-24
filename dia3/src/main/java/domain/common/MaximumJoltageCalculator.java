package domain.common;

public class MaximumJoltageCalculator implements JoltageCalculator {
    private final int batteriesToTurnOn;

    public MaximumJoltageCalculator() {
        this(2);
    }

    public MaximumJoltageCalculator(int batteriesToTurnOn) {
        if (batteriesToTurnOn < 1) {
            throw new IllegalArgumentException("Batteries to turn on must be >= 1");
        }
        this.batteriesToTurnOn = batteriesToTurnOn;
    }

    @Override
    public long calculate(BatteryBank bank) {
        String ratings = bank.ratings();
        if (ratings.length() < batteriesToTurnOn) {
            throw new IllegalArgumentException("Bank has fewer batteries than required");
        }

        StringBuilder maximumJoltage = new StringBuilder();
        int searchStart = 0;

        for (int selected = 0; selected < batteriesToTurnOn; selected++) {
            int searchEnd = ratings.length() - (batteriesToTurnOn - selected);
            int bestIndex = searchStart;

            for (int currentIndex = searchStart; currentIndex <= searchEnd; currentIndex++) {
                if (ratings.charAt(currentIndex) > ratings.charAt(bestIndex)) {
                    bestIndex = currentIndex;
                }
            }

            maximumJoltage.append(ratings.charAt(bestIndex));
            searchStart = bestIndex + 1;
        }

        return Long.parseLong(maximumJoltage.toString());
    }
}
