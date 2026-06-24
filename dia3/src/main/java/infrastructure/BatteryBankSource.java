package infrastructure;

import java.io.IOException;
import java.util.List;

public interface BatteryBankSource {
    List<String> getLines() throws IOException;
}
