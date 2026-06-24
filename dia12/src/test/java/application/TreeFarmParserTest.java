package application;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TreeFarmParserTest {
    @Test
    void parsesShapesAndRegions() {
        var plan = new TreeFarmParser().parse(List.of(
                "0:",
                "##.",
                ".##",
                "",
                "1:",
                "###",
                "",
                "4x4: 2 0"
        ));

        assertEquals(2, plan.shapes().size());
        assertEquals(4, plan.shapes().getFirst().area());
        assertEquals(1, plan.regions().size());
        assertEquals(4, plan.regions().getFirst().width());
        assertEquals(List.of(2, 0), plan.regions().getFirst().presentCounts());
    }
}
