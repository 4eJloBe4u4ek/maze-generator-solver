package maze.solver;

import java.util.Collections;
import java.util.List;
import maze.model.Coordinate;
import maze.model.Maze;

public class AStarSolver extends BaseSolver {
    private Coordinate start;
    private Coordinate end;

    @Override
    public List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end) {
        this.start = start;
        this.end = end;

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

    @Override
    protected int calculatePriority(Node node) {
        return node.cost + heuristic(node.coordinate, end);
    }

    private int heuristic(Coordinate a, Coordinate b) {
        return Math.abs(a.row() - b.row()) + Math.abs(a.col() - b.col());
    }
}
