package maze.solver;

public class DijkstraSolver extends BaseSolver {
    @Override
    protected int calculatePriority(BaseSolver.Node node) {
        return node.cost;
    }
}
