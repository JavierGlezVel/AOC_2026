package application;

import domain.common.FactoryMachine;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FactoryMachineParser {
    private static final Pattern MACHINE_PATTERN = Pattern.compile("^\\[([.#]+)]((?: \\([0-9,]+\\))+ )\\{([0-9,]+)}$");
    private static final Pattern BUTTON_PATTERN = Pattern.compile("\\(([0-9,]+)\\)");

    public List<FactoryMachine> parse(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("The factory manual needs at least one machine");
        }

        return lines.stream()
                .map(this::parseLine)
                .toList();
    }

    private FactoryMachine parseLine(String line) {
        if (line == null) {
            throw new IllegalArgumentException("Machine line cannot be null");
        }

        Matcher machineMatcher = MACHINE_PATTERN.matcher(line);
        if (!machineMatcher.matches()) {
            throw new IllegalArgumentException("Invalid machine description: " + line);
        }

        String diagram = machineMatcher.group(1);
        List<Integer> joltageRequirements = parseJoltageRequirements(machineMatcher.group(3));
        if (joltageRequirements.size() != diagram.length()) {
            throw new IllegalArgumentException("The indicator diagram and joltage requirements must have the same size");
        }
        List<Integer> buttonMasks = parseButtons(machineMatcher.group(2), diagram.length());
        return new FactoryMachine(diagram.length(), parseTargetMask(diagram), buttonMasks, joltageRequirements);
    }

    private int parseTargetMask(String diagram) {
        int targetMask = 0;
        for (int i = 0; i < diagram.length(); i++) {
            if (diagram.charAt(i) == '#') {
                targetMask |= 1 << i;
            }
        }
        return targetMask;
    }

    private List<Integer> parseButtons(String buttonsText, int lightCount) {
        Matcher buttonMatcher = BUTTON_PATTERN.matcher(buttonsText);
        List<Integer> buttonMasks = new ArrayList<>();

        while (buttonMatcher.find()) {
            buttonMasks.add(parseButtonMask(buttonMatcher.group(1), lightCount));
        }

        return buttonMasks;
    }

    private List<Integer> parseJoltageRequirements(String requirementsText) {
        List<Integer> requirements = new ArrayList<>();
        for (String requirementText : requirementsText.split(",")) {
            requirements.add(Integer.parseInt(requirementText));
        }
        return requirements;
    }

    private int parseButtonMask(String buttonText, int lightCount) {
        int buttonMask = 0;
        for (String indexText : buttonText.split(",")) {
            int index = Integer.parseInt(indexText);
            if (index < 0 || index >= lightCount) {
                throw new IllegalArgumentException("Button light index is out of range: " + index);
            }
            buttonMask |= 1 << index;
        }
        return buttonMask;
    }
}
