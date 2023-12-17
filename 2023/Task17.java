import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.PriorityQueue;

public class Task17 {
    private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; // East, South, West, North
    private static final int MAX_STEPS_A = 3;
    private static final int MIN_STEPS_B = 4;
    private static final int MAX_STEPS_B = 10;

    private static class State implements Comparable<State> {
        int col, row, dir, steps, heatLoss;

        State(int row, int col, int dir, int steps, int heatLoss) {
            this.col = col;
            this.row = row;
            this.dir = dir;
            this.steps = steps;
            this.heatLoss = heatLoss;
        }

        @Override
        public int compareTo(State other) {
            return this.heatLoss - other.heatLoss;
        }
    }

    public static int findMinimumHeatLoss(int[][] cityBlocks, boolean isTaskA) {
        int height = cityBlocks.length;
        int width = cityBlocks[0].length;
        PriorityQueue<State> pq = new PriorityQueue<>();

        pq.offer(new State(0, 0, -1, 0, 0));

        int[][][][] dp = new int[height][width][4][isTaskA ? MAX_STEPS_A+1 : MAX_STEPS_B+1];
        for (int[][][] layer1 : dp) {
            for (int[][] layer2 : layer1) {
                for (int[] layer3 : layer2) {
                    java.util.Arrays.fill(layer3, Integer.MAX_VALUE);
                }
            }
        }

        while (!pq.isEmpty()) {
            State currentState = pq.poll();
            if (currentState.row == height - 1 && currentState.col == width - 1 && (isTaskA || currentState.steps >= MIN_STEPS_B)) {
                return currentState.heatLoss;
            }

            for (int i = 0; i < DIRECTIONS.length; i++) {
                // skip reversing direction
                if (currentState.dir != -1  && i == (currentState.dir + 2) % 4) continue;

                if (isTaskA) {
                    int newRow = currentState.row + DIRECTIONS[i][0];
                    int newCol = currentState.col + DIRECTIONS[i][1];
                    int newSteps = (i == currentState.dir) ? currentState.steps + 1 : 1;
                    if (newSteps > MAX_STEPS_A) continue;
                    if (newRow >= 0 && newRow < height && newCol >= 0 && newCol < width) {
                        int newHeatLoss = currentState.heatLoss + cityBlocks[newRow][newCol];
                        if (newHeatLoss < dp[newRow][newCol][i][newSteps]) {
                            dp[newRow][newCol][i][newSteps] = newHeatLoss;
                            pq.offer(new State(newRow, newCol, i, newSteps, newHeatLoss));
                        }
                    }
                } else {
                    if (i == currentState.dir) {
                        // Check if it's possible to continue in the same direction
                        int stepRow = currentState.row + DIRECTIONS[i][1];
                        int stepCol = currentState.col + DIRECTIONS[i][0];
                        if (stepRow >= 0 && stepRow < height && stepCol >= 0 && stepCol < width) {
                            int newHeatLoss = currentState.heatLoss + cityBlocks[stepRow][stepCol];
                            int newSteps = currentState.steps + 1;
                            if (newSteps >= MIN_STEPS_B && newSteps <= MAX_STEPS_B && newHeatLoss < dp[stepRow][stepCol][i][newSteps]) {
                                pq.offer(new State(stepRow, stepCol, i, newSteps, newHeatLoss));
                                dp[stepRow][stepCol][i][newSteps] = newHeatLoss;
                            }
                        }
                    } else if (currentState.dir == -1 || currentState.steps >= MIN_STEPS_B) {
                        for (int step = 1; step < MIN_STEPS_B; step++) {
                            // Calculate new position and heat loss for changing direction
                            int newRow = currentState.row + DIRECTIONS[i][1] * step;
                            int newCol = currentState.col + DIRECTIONS[i][0] * step;
                            if (newRow < 0 || newRow >= height || newCol < 0 || newCol >= width) break;

                            int newHeatLoss = currentState.heatLoss;
                            for (int j = 1; j <= step; j++) {
                                int stepX = currentState.row + DIRECTIONS[i][1] * j;
                                int stepY = currentState.col + DIRECTIONS[i][0] * j;
                                if (stepX < 0 || stepX >= height || stepY < 0 || stepY >= width) break;
                                newHeatLoss += cityBlocks[stepX][stepY];
                            }

                            if (newHeatLoss < dp[newRow][newCol][i][step]) {
                                pq.offer(new State(newRow, newCol, i, step, newHeatLoss));
                                dp[newRow][newCol][i][step] = newHeatLoss;
                            }
                        }
                    }

                }
            }
        }

        return -1; // No valid path found (not possible with the given input)
    }


    public static void main(String[] args) {
        File input = new File("2023/input.txt");
        String line;
        int[][] cityBlocks = new int[141][141];
        int idx = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            while ((line = reader.readLine()) != null){
                for (int i = 0; i<line.length(); i++){
                    cityBlocks[idx][i] = line.charAt(i) - '0';
                }
                idx++;
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        System.out.println("task a: " + findMinimumHeatLoss(cityBlocks, true));
        System.out.println("task b: " + findMinimumHeatLoss(cityBlocks, false));
    }
}
