package domain.part1;

import domain.common.Dial;
import domain.common.Rotation;

import java.util.List;

public class PasswordCalculatorPart1 {
    public int calculate(List<Rotation> rotations) {
        Dial dial = new Dial();
        int count = 0;
        for (Rotation r : rotations) {
            dial.rotate(r);
            if (dial.getPosition() == 0) {
                count++;
            }
        }
        return count;
    }
}
