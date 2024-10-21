package maze.generator;

import java.security.SecureRandom;
import java.util.Map;
import maze.model.Cell;
import maze.model.Coordinate;
import maze.model.Maze;

public abstract class BaseGenerator implements Generator {
    protected static final int MAX_CHANCE = 100;
    protected static final int LOOP_CHANCE = 15;
    protected static final Map<Cell.Type, Integer> SURFACE_PROBABILITIES = Map.of(
        Cell.Type.DESERT, 15,
        Cell.Type.ROAD, 15,
        Cell.Type.PASSAGE, 70
    );

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
        int chance = secureRandom.nextInt(MAX_CHANCE) + 1;
        int cumulativeProbability = 0;

        for (Map.Entry<Cell.Type, Integer> entry : SURFACE_PROBABILITIES.entrySet()) {
            cumulativeProbability += entry.getValue();
            if (chance <= cumulativeProbability) {
                return entry.getKey();
            }
        }

        return Cell.Type.PASSAGE;
    }

    protected void addLoops() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (grid[row][col].type() == Cell.Type.WALL) {
                    int chance = secureRandom.nextInt(MAX_CHANCE) + 1;
                    if (chance <= LOOP_CHANCE) {
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
