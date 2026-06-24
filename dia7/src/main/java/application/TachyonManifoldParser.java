package application;

import domain.common.TachyonManifold;

import java.util.List;

public class TachyonManifoldParser {
    public TachyonManifold parse(List<String> lines) {
        return new TachyonManifold(lines);
    }
}
