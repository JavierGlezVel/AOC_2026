package domain.part1;

import domain.common.FactoryMachine;

import java.util.List;

public class MinimumButtonPressesCalculatorPart1 {
    public int calculate(List<FactoryMachine> machines) {
        if (machines == null || machines.isEmpty()) {
            throw new IllegalArgumentException("At least one machine is needed");
        }

        return machines.stream()
                .mapToInt(this::minimumPresses)
                .sum();
    }

    private int minimumPresses(FactoryMachine machine) {
        int bestPresses = Integer.MAX_VALUE;
        int buttonCount = machine.buttonMasks().size();
        int combinations = 1 << buttonCount;

        for (int combination = 0; combination < combinations; combination++) {
            int currentMask = 0;
            int presses = Integer.bitCount(combination);
            if (presses >= bestPresses) {
                continue;
            }

            for (int button = 0; button < buttonCount; button++) {
                if ((combination & (1 << button)) != 0) {
                    currentMask ^= machine.buttonMasks().get(button);
                }
            }

            if (currentMask == machine.targetMask()) {
                bestPresses = presses;
            }
        }

        if (bestPresses == Integer.MAX_VALUE) {
            throw new IllegalStateException("Machine cannot be configured with the available buttons");
        }
        return bestPresses;
    }
}
