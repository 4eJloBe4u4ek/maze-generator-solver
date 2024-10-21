package maze.model;

/**
 * Представляет ячейку в лабиринте.
 *
 * @param row  строка ячейки
 * @param col  столбец ячейки
 * @param type тип ячейки (стена, проход, пустыня, дорога)
 */
public record Cell(int row, int col, Type type) {
    public enum Type { WALL, PASSAGE, DESERT, ROAD }
}
