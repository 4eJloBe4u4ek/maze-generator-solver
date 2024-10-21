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

public abstract class BaseSolver implements Solver {
    protected static final int PASSAGE_COST = 5;
    protected static final int ROAD_COST = 3;
    protected static final int DESERT_COST = 7;

    protected Coordinate end;

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

    protected abstract int calculatePriority(Node node);

    protected int getCostForCellType(Cell.Type cellType) {
        return switch (cellType) {
            case PASSAGE -> PASSAGE_COST;
            case ROAD -> ROAD_COST;
            case DESERT -> DESERT_COST;
            default -> Integer.MAX_VALUE;
        };
    }

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
