package maze.generator;

import maze.model.Maze;

/**
 * Интерфейс {@code Generator} определяет контракт для всех генераторов лабиринтов.
 */
public interface Generator {
    Maze generate(int height, int width);
}
