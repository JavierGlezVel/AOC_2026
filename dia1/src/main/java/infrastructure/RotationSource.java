package infrastructure;

import java.io.IOException;
import java.util.List;

public interface RotationSource {
    List<String> getLines() throws IOException;
}
