package maze.generator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import maze.model.Cell;
import maze.model.Coordinate;
import maze.model.Direction;
import maze.model.Maze;

/**
 * Класс {@code RecursiveBacktrackingGenerator} реализует алгоритм рекурсивного возврата для генерации лабиринта.
 * Он использует стек (Deque) для отслеживания посещенных координат и выбирает случайного соседа для создания прохода.
 */
public class RecursiveBacktrackingGenerator extends BaseGenerator {
    /**
     * Создает экземпляр генератора рекурсивного возврата с заданными координатами старта и конца.
     *
     * @param start координаты начальной точки лабиринта.
     * @param end   координаты конечной точки лабиринта.
     */
    public RecursiveBacktrackingGenerator(Coordinate start, Coordinate end, int height, int width) {
        super(start, end, height, width);
    }

    /**
     * Генерирует лабиринт заданных размеров.
     *
     * @return сгенерированный лабиринт.
     */
    @Override
    public Maze generate() {
        initializeGrid();

        Deque<Coordinate> visitedCoordinates = new ArrayDeque<>();
        visitedCoordinates.push(start);
        params.grid()[start.row()][start.col()] = new Cell(start.row(), start.col(), Cell.Type.PASSAGE);

        while (!visitedCoordinates.isEmpty()) {
            Coordinate current = visitedCoordinates.peek();
            List<Coordinate> neighbors = getEligibleNeighbors(current);

            if (!neighbors.isEmpty()) {
                Coordinate next = neighbors.get(getRandomInt(neighbors.size()));
                params.grid()[next.row()][next.col()] = new Cell(next.row(), next.col(), determineNonWallSurfaceType());

                visitedCoordinates.push(next);
            } else {
                visitedCoordinates.pop();
            }
        }

        params.grid()[end.row()][end.col()] = new Cell(end.row(), end.col(), Cell.Type.PASSAGE);

        addLoops();
        return new Maze(params.height(), params.width(), params.grid(), start, end);
    }

    /**
     * Получает список подходящих соседних координат для заданной координаты: действительная координата
     * с единственным соседним проходом.
     *
     * @param coordinate координаты для получения соседей.
     * @return список подходящих соседей.
     */
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

    /**
     * Проверяет, имеет ли указанная ячейка только одного соседнего прохода.
     *
     * @param row координата строки ячейки.
     * @param col координата столбца ячейки.
     * @return true, если ячейка имеет только одного соседнего прохода; иначе false.
     */
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
