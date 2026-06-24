import application.TreeFarmSolver;
import infrastructure.FileTreeFarmSource;
import infrastructure.TreeFarmSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        String inputPath = args.length > 0 ? args[0] : defaultInputPath();

        TreeFarmSource source = new FileTreeFarmSource(inputPath);
        TreeFarmSolver solver = new TreeFarmSolver(source);
        System.out.println("Parte 1: " + solver.solvePart1());
    }

    private static String defaultInputPath() {
        Path modulePath = Path.of("src", "main", "resources", "input.txt");
        if (Files.exists(modulePath)) {
            return modulePath.toString();
        }
        return Path.of("dia12", "src", "main", "resources", "input.txt").toString();
    }
}
