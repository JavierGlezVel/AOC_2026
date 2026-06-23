package domain;

public record Rotation(char direction, int steps) {
    public Rotation {
        if (direction != 'L' && direction != 'R') {
            throw new IllegalArgumentException("Direction must be L or R");
        }
        if (steps < 0) {
            throw new IllegalArgumentException("Steps must be >= 0");
        }
    }

    @Override
    public String toString() {
        return direction() + String.valueOf(steps());
    }
}
