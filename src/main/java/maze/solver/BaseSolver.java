package maze.solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import maze.model.Cell;
import maze.model.Coordinate;
import maze.model.Direction;
import maze.model.Maze;

/**
 * Абстрактный класс {@code BaseSolver} предоставляет базовую реализацию для решения лабиринтов.
 * Этот класс использует алгоритм поиска с приоритетом для нахождения кратчайшего пути от
 * заданной начальной точки до конечной в лабиринте.
 * Подклассы должны реализовать метод {@link #calculatePriority(Node)},
 * который рассчитывает приоритет для узлов в очереди.
 */
public abstract class BaseSolver implements Solver {
    protected static final int PASSAGE_COST = 5;
    protected static final int ROAD_COST = 3;
    protected static final int DESERT_COST = 7;

    protected Coordinate end;

    /**
     * Решает лабиринт, находя кратчайший путь от начальной до конечной точки.
     *
     * @param maze лабиринт, в котором необходимо найти путь.
     * @param start начальная точка.
     * @param end конечная точка.
     * @return список координат, представляющих путь от начальной до конечной точки,
     * или пустой список, если путь не найден.
     */
    @Override
    public List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end) {
        this.end = end;

        int capacity = maze.height() * maze.width();
        PriorityQueue<Node> openNodes = new PriorityQueue<>(capacity, Comparator.comparingInt(this::calculatePriority));
        Map<Coordinate, Integer> costMap = new HashMap<>(capacity);
        Set<Coordinate> closedCoordinates = new HashSet<>(capacity);

        openNodes.add(new Node(start, null, 0));
        costMap.put(start, 0);

        while (!openNodes.isEmpty()) {
            Node current = openNodes.poll();
            if (current.coordinate.equals(end)) {
                return reconstructPath(current);
            }

            closedCoordinates.add(current.coordinate);

            for (Coordinate neighbor : getNeighbors(current.coordinate, maze)) {
                if (closedCoordinates.contains(neighbor)) {
                    continue;
                }

                int newCost = current.cost + getCostForCellType(maze.grid()[neighbor.row()][neighbor.col()].type());
                if (newCost < costMap.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    costMap.put(neighbor, newCost);
                    openNodes.add(new Node(neighbor, current, newCost));
                }
            }
        }

        return Collections.emptyList();
    }

    /**
     * Рассчитывает приоритет узла для очереди.
     * Подклассы должны реализовать этот метод, чтобы определить,
     * как будет оцениваться приоритет для каждого узла.
     *
     * @param node узел, для которого нужно рассчитать приоритет.
     * @return приоритет узла.
     */
    protected abstract int calculatePriority(Node node);

    /**
     * Получает стоимость для указанного типа ячейки.
     *
     * @param cellType тип ячейки (проход, дорога, пустыня).
     * @return стоимость ячейки.
     */
    protected int getCostForCellType(Cell.Type cellType) {
        return switch (cellType) {
            case PASSAGE -> PASSAGE_COST;
            case ROAD -> ROAD_COST;
            case DESERT -> DESERT_COST;
            default -> Integer.MAX_VALUE;
        };
    }

    /**
     * Реконструирует путь от конечного узла до начального, используя ссылки на родительские узлы.
     *
     * @param node конечный узел.
     * @return список координат, представляющих путь от начальной до конечной точки.
     */
    protected List<Coordinate> reconstructPath(Node node) {
        List<Coordinate> path = new ArrayList<>();
        Node currentNode = node;

        while (currentNode != null) {
            path.add(currentNode.coordinate);
            currentNode = currentNode.parent;
        }
        Collections.reverse(path);
        return path;
    }

    /**
     * Получает соседние координаты (не стенки) для заданной координаты в лабиринте.
     *
     * @param current текущая координата.
     * @param maze лабиринт, в котором находятся координаты.
     * @return список соседних координат.
     */
    protected List<Coordinate> getNeighbors(Coordinate current, Maze maze) {
        List<Coordinate> neighbors = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            int row = current.row() + direction.rowOffset();
            int col = current.col() + direction.colOffset();

            if (row >= 0 && row < maze.height() && col >= 0 && col < maze.width()) {
                if (maze.grid()[row][col].type() != Cell.Type.WALL) {
                    neighbors.add(new Coordinate(row, col));
                }
            }
        }
        return neighbors;
    }

    /**
     * Вложенный класс, представляющий узел в поисковом алгоритме.
     */
    protected static class Node {
        Coordinate coordinate;
        Node parent;
        int cost;

        public Node(Coordinate coordinate, Node parent, int cost) {
            this.coordinate = coordinate;
            this.parent = parent;
            this.cost = cost;
        }
    }
}
