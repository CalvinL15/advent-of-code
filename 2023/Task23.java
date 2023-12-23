import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Task23 {
    private static final int[] dx = {0, 0, 1, -1};
    private static final int[] dy = {1, -1, 0, 0};
    private static final char PATH = '.';
    private static final char FOREST = '#';
    private static int[] getNextMove(char direction) {
        switch (direction) {
            case '^': return new int[]{-1, 0};
            case '>': return new int[]{0, 1};
            case 'v': return new int[]{1, 0};
            case '<': return new int[]{0, -1};
            default: return new int[]{0, 0};
        }
    }

    private static int findLongestHike(ArrayList<String> map, boolean[][] visited) {
        int maxSteps = 0;

        for (int col = 0; col < map.get(0).length(); col++) {
            if (map.get(0).charAt(col) == PATH) {
                maxSteps = Math.max(maxSteps, search(map, visited, 0, col, 0, (char) 0));
                break;
            }
        }
        return maxSteps;
    }
    private static int search(ArrayList<String> map, boolean[][] visited, int row, int col, int steps, char direction) {
        if (row == map.size() - 1 && map.get(row).charAt(col) == PATH) {
            return steps;
        }

        visited[row][col] = true;
        int maxSteps = 0;

        char currentTile = map.get(row).charAt(col);
        if (isSlope(currentTile)) {
            direction = currentTile;
        }

        if (direction == 0) {
            for (int i = 0; i < 4; i++) {
                int newRow = row + dy[i];
                int newCol = col + dx[i];

                if (isValidMove(map, visited, newRow, newCol)) {
                    maxSteps = Math.max(maxSteps, search(map, visited, newRow, newCol, steps + 1, direction));
                }
            }
        } else {
            int[] move = getNextMove(direction);
            int newRow = row + move[0];
            int newCol = col + move[1];

            if (isValidMove(map, visited, newRow, newCol)) {
                maxSteps = Math.max(maxSteps, search(map, visited, newRow, newCol, steps + 1, (char) 0));
            }
        }

        visited[row][col] = false;
        return maxSteps;
    }

    private static boolean isValidMove(ArrayList<String> map, boolean[][] visited, int row, int col) {
        return row >= 0 && row < map.size() && col >= 0 && col < map.get(row).length()
                && map.get(row).charAt(col) != FOREST && !visited[row][col];
    }

    private static boolean isSlope(char tile) {
        return tile == '^' || tile == '>' || tile == 'v' || tile == '<';
    }

    public static void main(String[] args) {
        File input = new File("2023/input.txt");
        String line;
        ArrayList<String> mapA = new ArrayList();
        ArrayList<String> mapB = new ArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
             while((line = reader.readLine()) != null) {
                mapA.add(line);
                mapB.add(line.replace('>', '.').replace('<','.').replace('^', '.').replace('v', '.'));

             }
            int rows = mapA.size();
            int cols = mapA.get(0).length();
            boolean[][] visited = new boolean[rows][cols];
            System.out.println("task A: " + findLongestHike(mapA, visited));
            // this is not an optimal solution - you need to compress the graph so that there are less paths to consider
            System.out.println("task B: " + findLongestHike(mapB, visited));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
