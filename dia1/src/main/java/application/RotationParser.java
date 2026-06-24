package application;

import domain.common.Rotation;

import java.util.ArrayList;
import java.util.List;

public class RotationParser {
    public List<Rotation> parse(List<String> lines) {
        List<Rotation> rotations = new ArrayList<>();

        for (String line : lines) {
            char dir = line.charAt(0);
            int steps = Integer.parseInt(line.substring(1));
            rotations.add(new Rotation(dir, steps));
        }
        return rotations;
    }
}
