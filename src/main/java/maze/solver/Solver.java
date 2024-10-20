package maze.solver;

import maze.model.Coordinate;
import maze.model.Maze;
import java.util.List;

public interface Solver {
    List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end);
}
