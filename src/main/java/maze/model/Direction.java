package maze.model;

import lombok.Getter;

/**
 * {@code Direction} представляет возможные направления для перемещения в лабиринте.
 */
@Getter public enum Direction {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    private final int rowOffset;
    private final int colOffset;

    Direction(int rowOffset, int colOffset) {
        this.rowOffset = rowOffset;
        this.colOffset = colOffset;
    }
}
