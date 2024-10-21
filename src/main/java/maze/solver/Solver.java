package maze.solver;

import java.util.List;
import maze.model.Coordinate;
import maze.model.Maze;

/**
 * Интерфейс {@code Solver} определяет контракт для реализации алгоритмов решения лабиринтов.
 */
public interface Solver {
    List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end);
}
