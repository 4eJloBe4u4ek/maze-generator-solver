package maze.generator;

import lombok.Getter;
import maze.model.Cell;

@Getter
public class GeneratorParams {
    private final int height;
    private final int width;
    private final Cell[][] grid;

    public GeneratorParams(int height, int width) {
        this.height = height;
        this.width = width;
        this.grid = new Cell[this.height][this.width];
    }
}
