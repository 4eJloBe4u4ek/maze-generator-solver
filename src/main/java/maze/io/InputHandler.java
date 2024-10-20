package maze.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.Getter;
import maze.generator.Generator;
import maze.generator.MazeGenerator;
import maze.generator.PrimsGenerator;
import maze.generator.RecursiveBacktrackingGenerator;
import maze.model.Coordinate;
import maze.model.Maze;
import maze.solver.AStarSolver;
import maze.solver.DijkstraSolver;
import maze.solver.MazeSolverAlgorithm;
import maze.solver.Solver;

public class InputHandler {
    private final String SEPARATOR = " - ";

    private PrintStream out;
    private BufferedReader in;

    @Getter private int height;
    @Getter private int width;
    @Getter private Coordinate start;
    @Getter private Coordinate end;

    @Getter private MazeGenerator generatorType;
    @Getter private MazeSolverAlgorithm solverType;

    public InputHandler(PrintStream out, InputStream in) {
        this.out = out;
        this.in = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
    }

    public void run() throws IOException {
        boolean continueGenerating = true;

        while (continueGenerating) {
            getMazeParametersFromUser();

            Generator generator = getGenerator();
            Solver solver = getSolver();

            Maze maze = generator.generate(height, width);
            List<Coordinate> path = solver.solve(maze, start, end);

            MazeTextRenderer renderer = new MazeTextRenderer();
            out.println(renderer.render(maze) + '\n');
            if (path.isEmpty()) {
                out.println("Путь между точками не найден");
            } else {
                out.println(renderer.render(maze, path));
            }

            continueGenerating = shouldContinue();
        }

    }

    private boolean shouldContinue() throws IOException {
        out.println("Хотите сгенерировать новый лабиринт? (да/нет):");
        String input = in.readLine().trim().toLowerCase();
        while (true) {
            switch (input) {
                case "нет":
                    out.println("Программа завершена!");
                    return false;
                case "да":
                    return true;
                default:
                    out.println("Введите 'да' или 'нет'");
                    input = in.readLine().trim().toLowerCase();
            }
        }
    }

    public void getMazeParametersFromUser() throws IOException {
        height = getPositiveInteger("Введите высоту лабиринта (минимум 3):");
        width = getPositiveInteger("Введите ширину лабиринта (минимум 3):");
        start = getValidCoordinate("Введите координаты старта, нумерация с 0 ('строка столбец'):");
        end = getValidCoordinate("Введите координаты финиша, нумерация с 0 ('строка столбец'):");
        generatorType = selectGeneratorAndSolver("Выберите алгоритм генерации лабиринта:", MazeGenerator.values());
        solverType = selectGeneratorAndSolver("Выберите алгоритм нахождения пути:", MazeSolverAlgorithm.values());
    }

    private <T extends Enum<T>> T selectGeneratorAndSolver(String message, T[] enums) throws IOException {
        out.println(message);
        for (int i = 0; i < MazeGenerator.values().length; i++) {
            out.println((i + 1) + SEPARATOR + enums[i].name().toLowerCase());
        }

        while (true) {
            String input = in.readLine().toLowerCase().trim();
            for (int i = 0; i < enums.length; i++) {
                if (input.equals(String.valueOf(i + 1)) || input.equals(enums[i].name().toLowerCase())) {
                    return enums[i];
                }
            }
            out.println("Неверный выбор, попробуйте еще раз:");
        }
    }

    private Generator getGenerator() {
        return switch (generatorType) {
            case PRIMS -> new PrimsGenerator(start, end);
            case RECURSIVE_BACKTRACKER -> new RecursiveBacktrackingGenerator(start, end);
        };
    }

    private Solver getSolver() {
        return switch (solverType) {
            case ASTAR -> new AStarSolver();
            case DIJKSTRA -> new DijkstraSolver();
        };
    }

    private int getPositiveInteger(String message) throws IOException {
        out.println(message);

        while (true) {
            String input = in.readLine();

            if (input == null) {
                throw new IllegalArgumentException("Значение не может быть null");
            }
            input = input.trim();

            if (input.matches("\\d+")) {
                if (Integer.parseInt(input) >= 3) {
                    return Integer.parseInt(input);
                }
                out.println("Значение должно быть больше 2, попробуйте снова:");
            } else {
                out.println("Некорректное значение. Введите положительное целое число больше 2:");
            }
        }
    }

    private Coordinate getValidCoordinate(String message) throws IOException {
        out.println(message);

        while (true) {
            String input = in.readLine();
            String[] parts = input.split(" ");
            if (parts.length != 2) {
                out.println("Введите координаты в формате 'строка столбец' (0 <= и < размера)");
                continue;
            }

            if (parts[0].matches("\\d+") && parts[1].matches("\\d+")) {
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);

                if ((row == 0 && col < width) || (row == height - 1 && col < width) ||
                    (row < height && col == 0) || (row < height && col == width - 1)) {
                    return new Coordinate(row, col);
                } else {
                    out.println("Координата должна находится на границах лабиринта, попробуйте еще раз:");
                }
            } else {
                out.println("Некорректное значение, введите целые числа:");
            }
        }
    }
}
