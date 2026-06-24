package infrastructure;

import java.io.IOException;
import java.util.List;

public interface FactoryMachineSource {
    List<String> getLines() throws IOException;
}
