import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class Task16 {

    private static int countEnergizedTiles(String[] map, int row, int col, boolean[][] lightMap, HashSet<String> visited, String direction) {
        String curState = row + "," + col + "," + direction;

        if (row < 0 || row > map.length - 1  || col < 0 || col > map[0].length() - 1 || visited.contains(curState)) {
            return 0;
        }

        // for cycle detection
        visited.add(curState);

        int taskB = 0;
        if (!lightMap[row][col]) {
            lightMap[row][col] = true;
            taskB = 1;
        }
        char tile = map[row].charAt(col);
        String newDirection = direction;
        boolean shouldContinue = true;
        switch (tile) {
            case '|':
                if (direction.equals("right") || direction.equals("left")) {
                    taskB += countEnergizedTiles(map, row - 1, col, lightMap, visited,"up");
                    taskB += countEnergizedTiles(map, row + 1, col, lightMap, visited,"down");
                    shouldContinue = false;
                }
                break;
            case '-':
                if (direction.equals("up") || direction.equals("down")) {
                    taskB += countEnergizedTiles(map, row, col + 1, lightMap, visited,"right");
                    taskB += countEnergizedTiles(map, row, col - 1, lightMap, visited,"left");
                    shouldContinue = false;
                }
                break;
            case '/':
                newDirection = switch (direction) {
                    case "up" -> "right";
                    case "down" -> "left";
                    case "left" -> "down";
                    default -> "up";
                };
                break;
            case '\\':
                newDirection = switch (direction) {
                    case "up" -> "left";
                    case "down" -> "right";
                    case "left" -> "up";
                    default -> "down";
                };
                break;
        }

        if (shouldContinue) {
            if (newDirection.equals("right") && col < map[0].length() - 1) {
                taskB += countEnergizedTiles(map, row, col + 1, lightMap, visited, newDirection);
            } else if (newDirection.equals("left") && col > 0) {
                taskB += countEnergizedTiles(map, row, col - 1, lightMap, visited, newDirection);
            } else if (newDirection.equals("down") && row < map.length - 1) {
                taskB += countEnergizedTiles(map,row + 1, col, lightMap, visited, newDirection);
            } else if (newDirection.equals("up") && row > 0) {
                taskB += countEnergizedTiles(map,row - 1, col, lightMap, visited, newDirection);
            }
        }
        return taskB;
    }

    public static void main(String[] args) {
        File input = new File("2023/input.txt");
        String[] map = new String[110];
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            int idx = 0;
            while ((line = reader.readLine()) != null) {
                map[idx] = line;
                idx++;
            }
            int taskA = countEnergizedTiles(map, 0, 0, new boolean[110][110], new HashSet<>(), "right");
            System.out.println("task a: " + taskA);
            int taskB = -1;
            for (int i = 0; i<110; i++){
                int topRowConfigValue = countEnergizedTiles(map, 0, i, new boolean[110][110], new HashSet<>(),"down");
                int bottomRowConfigValue = countEnergizedTiles(map, 109, i, new boolean[110][110], new HashSet<>(), "up");
                int leftMostColumnConfigValue = countEnergizedTiles(map, i, 0, new boolean[110][110], new HashSet<>(), "right");
                int rightMostColumnConfigValue = countEnergizedTiles(map, i, 109, new boolean[110][110], new HashSet<>(), "left");
                if (taskB < topRowConfigValue){
                    taskB = topRowConfigValue;
                }
                if (taskB < bottomRowConfigValue){
                    taskB = bottomRowConfigValue;
                }
                if (taskB < leftMostColumnConfigValue){
                    taskB = leftMostColumnConfigValue;
                }
                if (taskB < rightMostColumnConfigValue){
                    taskB = rightMostColumnConfigValue;
                }
            }
            System.out.println("task b: " + taskB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
