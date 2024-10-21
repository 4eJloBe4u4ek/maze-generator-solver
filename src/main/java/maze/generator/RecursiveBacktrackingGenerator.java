package maze.generator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import maze.model.Cell;
import maze.model.Coordinate;
import maze.model.Direction;
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

        Deque<Coordinate> visitedCoordinates = new ArrayDeque<>();
        visitedCoordinates.push(start);
        grid[start.row()][start.col()] = new Cell(start.row(), start.col(), Cell.Type.PASSAGE);

        while (!visitedCoordinates.isEmpty()) {
            Coordinate current = visitedCoordinates.peek();
            List<Coordinate> neighbors = getEligibleNeighbors(current);

            if (!neighbors.isEmpty()) {
                Coordinate next = neighbors.get(secureRandom.nextInt(neighbors.size()));
                grid[next.row()][next.col()] = new Cell(next.row(), next.col(), determineNonWallSurfaceType());

                visitedCoordinates.push(next);
            } else {
                visitedCoordinates.pop();
            }
        }

        grid[end.row()][end.col()] = new Cell(end.row(), end.col(), Cell.Type.PASSAGE);

        addLoops();
        return new Maze(height, width, grid, start, end);
    }

    private List<Coordinate> getEligibleNeighbors(Coordinate coordinate) {
        List<Coordinate> neighbors = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            int row = coordinate.row() + direction.rowOffset();
            int col = coordinate.col() + direction.colOffset();

            if (isValidCell(row, col, Cell.Type.WALL) && hasSingleNeighborPassage(row, col)) {
                neighbors.add(new Coordinate(row, col));
            }
        }
        return neighbors;
    }

    private boolean hasSingleNeighborPassage(int row, int col) {
        int passageCount = 0;

        for (Direction direction : Direction.values()) {
            int adjRow = row + direction.rowOffset();
            int adjCol = col + direction.colOffset();

            if (isValidCell(adjRow, adjCol, Cell.Type.PASSAGE)
                || isValidCell(adjRow, adjCol, Cell.Type.ROAD)
                || isValidCell(adjRow, adjCol, Cell.Type.DESERT)) {
                passageCount++;
            }
        }
        return passageCount == 1;
    }
}
