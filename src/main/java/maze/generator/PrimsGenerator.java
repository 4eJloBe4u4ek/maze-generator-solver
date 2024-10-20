package maze.generator;

import java.util.ArrayList;
import java.util.List;
import maze.model.Cell;
import maze.model.Coordinate;
import maze.model.Maze;

public class PrimsGenerator extends BaseGenerator {

    public PrimsGenerator(Coordinate start, Coordinate end) {
        super(start, end);
    }

    @Override
    public Maze generate(int height, int width) {
        this.height = height;
        this.width = width;
        this.grid = new Cell[this.height][this.width];
        initializeGrid();

        List<Coordinate> boundaryCoordinates = new ArrayList<>();
        grid[start.row()][start.col()] = new Cell(start.row(), start.col(), Cell.Type.PASSAGE);
        grid[end.row()][end.col()] = new Cell(end.row(), end.col(), Cell.Type.PASSAGE);

        updateBoundaryCoordinates(start, boundaryCoordinates);

        while (!boundaryCoordinates.isEmpty()) {
            int randomCoordinate = secureRandom.nextInt(boundaryCoordinates.size());
            Coordinate boundaryCoordinate = boundaryCoordinates.get(randomCoordinate);

            if (connectsSingleOrEndPassage(boundaryCoordinate)) {
                grid[boundaryCoordinate.row()][boundaryCoordinate.col()] =
                    new Cell(boundaryCoordinate.row(), boundaryCoordinate.col(), determineNonWallSurfaceType());

                updateBoundaryCoordinates(boundaryCoordinate, boundaryCoordinates);
            }

            boundaryCoordinates.remove(randomCoordinate);
        }

        addLoops();
        return new Maze(height, width, grid, start, end);
    }

    private void updateBoundaryCoordinates(Coordinate coordinate, List<Coordinate> boundaryCoordinates) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] direction : directions) {
            int row = coordinate.row() + direction[0];
            int col = coordinate.col() + direction[1];

            if (isValidCell(row, col, Cell.Type.WALL)) {
                Coordinate newBoundary = new Coordinate(row, col);
                if (connectsSingleOrEndPassage(newBoundary)) {
                    if (!boundaryCoordinates.contains(newBoundary)) {
                        boundaryCoordinates.add(newBoundary);
                    }
                }
            }
        }
    }

    private boolean connectsSingleOrEndPassage(Coordinate coordinate) {
        int passageCount = 0;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        boolean isEndNeighbor = false;

        for (int[] direction : directions) {
            int row = coordinate.row() + direction[0];
            int col = coordinate.col() + direction[1];

            if (row == end.row() && col == end.col()) {
                isEndNeighbor = true;
                continue;
            }

            if (isValidCell(row, col, Cell.Type.PASSAGE) || isValidCell(row, col, Cell.Type.DESERT) ||
                isValidCell(row, col, Cell.Type.ROAD)) {
                passageCount++;
            }
        }
        return passageCount == 1 || isEndNeighbor;
    }
}
