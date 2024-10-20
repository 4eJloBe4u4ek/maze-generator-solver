package maze.generator;

import maze.model.Cell;
import maze.model.Coordinate;
import maze.model.Maze;
import java.security.SecureRandom;

public abstract class BaseGenerator implements Generator {
    protected final SecureRandom secureRandom = new SecureRandom();
    protected int height;
    protected int width;
    protected Cell[][] grid;
    protected final Coordinate start;
    protected final Coordinate end;

    public BaseGenerator(Coordinate start, Coordinate end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public abstract Maze generate(int height, int width);

    protected void initializeGrid() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col] = new Cell(row, col, Cell.Type.WALL);
            }
        }
    }

    protected Cell.Type determineNonWallSurfaceType() {
        int chance = secureRandom.nextInt(100) + 1;
        if (chance < 15) {
            return Cell.Type.DESERT;
        } else if (chance < 30) {
            return Cell.Type.ROAD;
        } else {
            return Cell.Type.PASSAGE;
        }
    }

    protected void addLoops() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (grid[row][col].type() == Cell.Type.WALL) {
                    int chance = secureRandom.nextInt(100) + 1;
                    if (chance < 15) {
                        grid[row][col] = new Cell(row, col, determineNonWallSurfaceType());
                    }
                }
            }
        }
    }

    protected boolean isValidCell(int row, int col, Cell.Type type) {
        return row >= 0 && row < height && col >= 0 && col < width && grid[row][col].type() == type;
    }
}
