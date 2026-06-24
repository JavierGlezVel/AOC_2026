package domain.common;

public record BatteryBank(String ratings) {
    public BatteryBank {
        if (ratings == null || ratings.length() < 2) {
            throw new IllegalArgumentException("A battery bank needs at least two batteries");
        }
        if (!ratings.matches("[1-9]+")) {
            throw new IllegalArgumentException("Battery ratings must be digits from 1 to 9: " + ratings);
        }
    }
}
