package maze.solver;

import java.util.Collections;
import java.util.List;
import maze.model.Coordinate;
import maze.model.Maze;

public class DijkstraSolver extends BaseSolver {
    @Override
    public List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end) {
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
    protected int calculatePriority(BaseSolver.Node node) {
        return node.cost;
    }
}
