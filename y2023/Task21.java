import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Task21 {
    private static final int[] dx = {0, 0, 1, -1}; // Directions: N, S, E, W
    private static final int[] dy = {1, -1, 0, 0};
    private static final int MAX_STEPS = 64;
    private static boolean[][] grid;
    private static boolean[][][] visited;
    private static int rows, cols;

    private static void initialize(ArrayList<String> map) {
        rows = map.size();
        cols = map.get(0).length();
        grid = new boolean[rows][cols];
        visited = new boolean[rows][cols][MAX_STEPS + 1];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = map.get(i).charAt(j) != '#';
            }
        }
    }

    private static int bfs(int x, int y) {
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{x, y, 0});
        visited[x][y][0] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int curX = current[0];
            int curY = current[1];
            int steps = current[2];

            if (steps < MAX_STEPS) {
                for (int i = 0; i < 4; i++) {
                    int newX = curX + dx[i];
                    int newY = curY + dy[i];

                    if (isValid(newX, newY) && !visited[newX][newY][steps + 1]) {
                        visited[newX][newY][steps + 1] = true;
                        queue.offer(new int[]{newX, newY, steps + 1});
                    }
                }
            }
        }

        int count = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (visited[i][j][MAX_STEPS]) count++;
            }
        }

        return count;
    }

    private static boolean isValid(int x, int y) {
        return x >= 0 && y >= 0 && x < rows && y < cols && grid[x][y];
    }

    public static void main(String[] args){
        File input = new File("/input.txt");
        String line;
        ArrayList<String> map = new ArrayList();
        int curIdx = 0;
        int startingRow = 0, startingCol = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            while((line = reader.readLine()) != null){
                map.add(line);
                int startingIdx = line.indexOf('S');
                if(startingIdx != -1){
                    startingRow = curIdx;
                    startingCol = startingIdx;
                }
                curIdx++;
            }
            initialize(map);
            System.out.println(bfs(startingRow, startingCol));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
