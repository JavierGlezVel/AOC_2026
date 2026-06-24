package domain.common;

import java.util.List;

public record FactoryMachine(int lightCount, int targetMask, List<Integer> buttonMasks, List<Integer> joltageRequirements) {
    public FactoryMachine {
        if (lightCount < 1 || lightCount > Integer.SIZE - 1) {
            throw new IllegalArgumentException("A machine needs between 1 and 31 lights");
        }
        if (targetMask < 0 || targetMask >= (1 << lightCount)) {
            throw new IllegalArgumentException("Target mask is out of range");
        }
        if (joltageRequirements == null || joltageRequirements.size() != lightCount) {
            throw new IllegalArgumentException("A machine needs one joltage requirement per light");
        }
        if (buttonMasks == null || buttonMasks.isEmpty()) {
            throw new IllegalArgumentException("A machine needs at least one button");
        }
        int validMask = (1 << lightCount) - 1;
        for (int buttonMask : buttonMasks) {
            if ((buttonMask & ~validMask) != 0) {
                throw new IllegalArgumentException("Button mask is out of range");
            }
        }
        for (int requirement : joltageRequirements) {
            if (requirement < 0) {
                throw new IllegalArgumentException("Joltage requirements cannot be negative");
            }
        }
        buttonMasks = List.copyOf(buttonMasks);
        joltageRequirements = List.copyOf(joltageRequirements);
    }
}
