package domain.part2;

import domain.common.Dial;
import domain.common.Rotation;

import java.util.List;

public class PasswordCalculatorPart2 {
    public int calculate(List<Rotation> rotations) {
        Dial dial = new Dial();
        int count = 0;
        for (Rotation r : rotations) {
            count += countZeros(dial.getPosition(), r);
            dial.rotate(r);
        }
        return count;
    }

    private int countZeros(int start, Rotation r) {
        int steps = r.steps();
        if (steps <= 0) {
            return 0;
        }
        if (r.direction() == 'L') {
            if (start == 0) {
                return steps / 100;
            }
            if (steps < start) {
                return 0;
            }
            return 1 + (steps - start) / 100;
        }
        if (start == 0) {
            return steps / 100;
        }
        int toZero = 100 - start;
        if (steps < toZero) {
            return 0;
        }
        return 1 + (steps - toZero) / 100;
    }
}
