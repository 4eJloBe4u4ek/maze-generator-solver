package maze.solver;

import maze.model.Coordinate;

public class AStarSolver extends BaseSolver {
    @Override
    protected int calculatePriority(Node node) {
        return node.cost + heuristic(node.coordinate, end);
    }

    private int heuristic(Coordinate a, Coordinate b) {
        return Math.abs(a.row() - b.row()) + Math.abs(a.col() - b.col());
    }
}
