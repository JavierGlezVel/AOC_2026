package infrastructure;

import java.io.IOException;
import java.util.List;

public interface DeviceNetworkSource {
    List<String> getLines() throws IOException;
}
