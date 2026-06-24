package application;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeviceNetworkParserTest {
    @Test
    void parsesDeviceOutputs() {
        var network = new DeviceNetworkParser().parse(List.of(
                "you: bbb ccc",
                "bbb: out"
        ));

        assertEquals(List.of("bbb", "ccc"), network.outputsFrom("you"));
        assertEquals(List.of("out"), network.outputsFrom("bbb"));
    }

    @Test
    void rejectsInvalidDescriptions() {
        DeviceNetworkParser parser = new DeviceNetworkParser();

        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("you bbb ccc")));
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("aaa: out")));
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("you: out", "you: aaa")));
    }
}
