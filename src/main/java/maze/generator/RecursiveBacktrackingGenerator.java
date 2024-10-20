package maze.generator;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import maze.model.Cell;
import maze.model.Coordinate;
import maze.model.Maze;

public class RecursiveBacktrackingGenerator extends BaseGenerator {

    public RecursiveBacktrackingGenerator(Coordinate start, Coordinate end) {
        super(start, end);
    }

    @Override
    public Maze generate(int height, int width) {
        this.height = height;
        this.width = width;
        this.grid = new Cell[this.height][this.width];
        initializeGrid();

        Stack<Coordinate> stack = new Stack<>();
        stack.push(start);
        grid[start.row()][start.col()] = new Cell(start.row(), start.col(), Cell.Type.PASSAGE);

        while (!stack.isEmpty()) {
            Coordinate current = stack.peek();
            List<Coordinate> neighbors = getEligibleNeighbors(current);

            if (!neighbors.isEmpty()) {
                Coordinate next = neighbors.get(secureRandom.nextInt(neighbors.size()));
                grid[next.row()][next.col()] = new Cell(next.row(), next.col(), determineNonWallSurfaceType());

                stack.push(next);
            } else {
                stack.pop();
            }
        }

        grid[end.row()][end.col()] = new Cell(end.row(), end.col(), Cell.Type.PASSAGE);

        addLoops();
        return new Maze(height, width, grid, start, end);
    }

    private List<Coordinate> getEligibleNeighbors(Coordinate coordinate) {
        List<Coordinate> neighbors = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] direction : directions) {
            int row = coordinate.row() + direction[0];
            int col = coordinate.col() + direction[1];

            if (isValidCell(row, col, Cell.Type.WALL) && hasSingleNeighborPassage(row, col)) {
                neighbors.add(new Coordinate(row, col));
            }
        }
        return neighbors;
    }

    private boolean hasSingleNeighborPassage(int row, int col) {
        int passageCount = 0;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] direction : directions) {
            int adjRow = row + direction[0];
            int adjCol = col + direction[1];

            if (isValidCell(adjRow, adjCol, Cell.Type.PASSAGE) ||
                isValidCell(adjRow, adjCol, Cell.Type.ROAD) ||
                isValidCell(adjRow, adjCol, Cell.Type.DESERT)) {
                passageCount++;
            }
        }
        return passageCount == 1;
    }
}
