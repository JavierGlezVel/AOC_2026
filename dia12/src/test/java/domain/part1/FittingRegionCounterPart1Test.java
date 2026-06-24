package domain.part1;

import application.TreeFarmParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FittingRegionCounterPart1Test {
    @Test
    void countsFittingRegionsFromOfficialExample() {
        var plan = new TreeFarmParser().parse(List.of(
                "0:",
                "###",
                "##.",
                "##.",
                "",
                "1:",
                "###",
                "##.",
                ".##",
                "",
                "2:",
                ".##",
                "###",
                "##.",
                "",
                "3:",
                "##.",
                "###",
                "##.",
                "",
                "4:",
                "###",
                "#..",
                "###",
                "",
                "5:",
                "###",
                ".#.",
                "###",
                "",
                "4x4: 0 0 0 0 2 0",
                "12x5: 1 0 1 0 2 2",
                "12x5: 1 0 1 0 3 2"
        ));

        int fittingRegions = new FittingRegionCounterPart1().countFittingRegions(plan);

        assertEquals(2, fittingRegions);
    }
}
