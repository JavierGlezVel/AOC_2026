package application;

import domain.common.DeviceNetwork;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DeviceNetworkParser {
    public DeviceNetwork parse(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("The reactor manual needs at least one device");
        }

        Map<String, List<String>> outputsByDevice = new LinkedHashMap<>();
        for (String line : lines) {
            parseLine(line, outputsByDevice);
        }

        return new DeviceNetwork(outputsByDevice);
    }

    private void parseLine(String line, Map<String, List<String>> outputsByDevice) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("Device line cannot be blank");
        }

        String[] parts = line.split(": ", 2);
        if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new IllegalArgumentException("Invalid device description: " + line);
        }
        if (outputsByDevice.containsKey(parts[0])) {
            throw new IllegalArgumentException("Duplicated device description: " + parts[0]);
        }

        outputsByDevice.put(parts[0], List.of(parts[1].split(" ")));
    }
}
