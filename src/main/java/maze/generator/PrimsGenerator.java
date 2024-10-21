package maze.generator;

import java.util.ArrayList;
import java.util.List;
import maze.model.Cell;
import maze.model.Coordinate;
import maze.model.Direction;
import maze.model.Maze;

/**
 * Класс {@code PrimsGenerator} реализует алгоритм Прима для генерации лабиринта.
 */
public class PrimsGenerator extends BaseGenerator {

    /**
     * Создает экземпляр генератора Прима с заданными координатами старта и конца.
     *
     * @param start координаты начальной точки лабиринта.
     * @param end   координаты конечной точки лабиринта.
     */
    public PrimsGenerator(Coordinate start, Coordinate end) {
        super(start, end);
    }

    /**
     * Генерирует лабиринт заданных размеров.
     *
     * @param height высота лабиринта.
     * @param width  ширина лабиринта.
     * @return сгенерированный лабиринт.
     */
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

    /**
     * Обновляет список граничных ячеек на основе заданной координаты: если ее нет в списке
     * и она соединена с одним проходом или конечной точкой.
     *
     * @param coordinate координаты для обновления границ.
     * @param boundaryCoordinates список границ для добавления новых координат.
     */
    private void updateBoundaryCoordinates(Coordinate coordinate, List<Coordinate> boundaryCoordinates) {
        for (Direction direction : Direction.values()) {
            int row = coordinate.row() + direction.rowOffset();
            int col = coordinate.col() + direction.colOffset();

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

    /**
     * Проверяет, соединяет ли указанная координата только один проход или является соседней с конечной точкой.
     *
     * @param coordinate координаты для проверки.
     * @return true, если координата соединяет один проход или является соседом конечной точки; иначе false.
     */
    private boolean connectsSingleOrEndPassage(Coordinate coordinate) {
        int passageCount = 0;
        boolean isEndNeighbor = false;

        for (Direction direction : Direction.values()) {
            int row = coordinate.row() + direction.rowOffset();
            int col = coordinate.col() + direction.colOffset();

            if (row == end.row() && col == end.col()) {
                isEndNeighbor = true;
                continue;
            }

            if (isValidCell(row, col, Cell.Type.PASSAGE)
                || isValidCell(row, col, Cell.Type.DESERT)
                || isValidCell(row, col, Cell.Type.ROAD)) {
                passageCount++;
            }
        }
        return passageCount == 1 || isEndNeighbor;
    }
}
