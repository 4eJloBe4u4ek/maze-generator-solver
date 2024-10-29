package maze.generator;

import maze.model.Maze;

/**
 * Интерфейс {@code MazeGenerationAlgorithm} определяет контракт для всех генераторов лабиринтов.
 */
public interface MazeGenerationAlgorithm {
    Maze generate();
}
