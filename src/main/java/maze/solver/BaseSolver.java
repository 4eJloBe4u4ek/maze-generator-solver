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
import maze.model.Maze;

public abstract class BaseSolver implements Solver {

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

    protected PriorityQueue<Node> openNodes;
    protected Map<Coordinate, Integer> costMap = new HashMap<>();
    protected Set<Coordinate> closedCoordinates = new HashSet<>();

    public BaseSolver() {
        this.openNodes = new PriorityQueue<>(Comparator.comparingInt(this::calculatePriority));
    }

    @Override
    public abstract List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end);

    protected abstract int calculatePriority(Node node);

    protected int getCostForCellType(Cell.Type cellType) {
        return switch (cellType) {
            case PASSAGE -> 5;
            case ROAD -> 3;
            case DESERT -> 7;
            default -> Integer.MAX_VALUE;
        };
    }

    protected List<Coordinate> reconstructPath(Node node) {
        List<Coordinate> path = new ArrayList<>();
        while (node != null) {
            path.add(node.coordinate);
            node = node.parent;
        }
        Collections.reverse(path);
        return path;
    }

    protected List<Coordinate> getNeighbors(Coordinate current, Maze maze) {
        List<Coordinate> neighbors = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] direction : directions) {
            int row = current.row() + direction[0];
            int col = current.col() + direction[1];

            if (row >= 0 && row < maze.height() && col >= 0 && col < maze.width()) {
                if (maze.grid()[row][col].type() != Cell.Type.WALL) {
                    neighbors.add(new Coordinate(row, col));
                }
            }
        }
        return neighbors;
    }
}
