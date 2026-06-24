package domain.common;

import java.util.List;
import java.util.Map;

public record DeviceNetwork(Map<String, List<String>> outputsByDevice) {
    public static final String START_DEVICE = "you";
    public static final String OUTPUT_DEVICE = "out";

    public DeviceNetwork {
        if (outputsByDevice == null || outputsByDevice.isEmpty()) {
            throw new IllegalArgumentException("The device network needs at least one device");
        }
        if (!outputsByDevice.containsKey(START_DEVICE)) {
            throw new IllegalArgumentException("The device network needs the start device: " + START_DEVICE);
        }

        outputsByDevice.forEach((device, outputs) -> {
            if (device == null || device.isBlank()) {
                throw new IllegalArgumentException("Device names cannot be blank");
            }
            if (outputs == null || outputs.isEmpty()) {
                throw new IllegalArgumentException("Device outputs cannot be empty");
            }
            for (String output : outputs) {
                if (output == null || output.isBlank()) {
                    throw new IllegalArgumentException("Output device names cannot be blank");
                }
            }
        });

        outputsByDevice = outputsByDevice.entrySet().stream()
                .collect(java.util.stream.Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        entry -> List.copyOf(entry.getValue())
                ));
    }

    public List<String> outputsFrom(String device) {
        return outputsByDevice.getOrDefault(device, List.of());
    }
}
