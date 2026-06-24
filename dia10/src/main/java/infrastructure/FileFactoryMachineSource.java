package infrastructure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileFactoryMachineSource implements FactoryMachineSource {
    private final String path;

    public FileFactoryMachineSource(String path) {
        this.path = path;
    }

    @Override
    public List<String> getLines() throws IOException {
        return Files.readAllLines(Path.of(path));
    }
}
