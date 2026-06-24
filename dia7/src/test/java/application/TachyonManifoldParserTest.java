package application;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TachyonManifoldParserTest {
    @Test
    void parsesAValidManifold() {
        var manifold = new TachyonManifoldParser().parse(List.of(
                "..S..",
                ".....",
                ".^.^."
        ));

        assertEquals(3, manifold.height());
        assertEquals(5, manifold.width());
        assertEquals(0, manifold.start().row());
        assertEquals(2, manifold.start().column());
    }

    @Test
    void rejectsManifoldsWithoutExactlyOneStart() {
        TachyonManifoldParser parser = new TachyonManifoldParser();

        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of(".....")));
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("S...S")));
    }
}
