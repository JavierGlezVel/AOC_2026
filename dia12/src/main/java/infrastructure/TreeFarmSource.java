package infrastructure;

import java.io.IOException;
import java.util.List;

public interface TreeFarmSource {
    List<String> getLines() throws IOException;
}
