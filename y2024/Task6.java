package y2024;

import common.Direction;
import common.Position;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Task6 {
    public static boolean isExists(HashSet<Position> hashSet, Position position){
        return hashSet.contains(position);
    }

    public static void solve(String[] map, int row, int col, Direction dir){
        int distinctPathTraversedCount = 1;
        int curRow = row, curCol = col;
        int maxRow = map.length;
        int maxCol = map[0].length();
        boolean[][] isVisited = new boolean[maxRow][maxCol];
        isVisited[curRow][curCol] = true;
        List<int[]> potentialObstructionIdx = new ArrayList();
        potentialObstructionIdx.add(new int[]{curRow, curCol});

        while (true) {
            if (dir == Direction.UP) {
                // out of bound
                if (curRow - 1 < 0) break;
                // change direction
                if (map[curRow-1].charAt(curCol) == '#') {
                    dir = Direction.RIGHT;
                    continue;
                }
                curRow -= 1;
            }
            else if (dir == Direction.RIGHT){
                // out of bound
                if (curCol + 1 >= maxCol) break;
                // change direction
                if (map[curRow].charAt(curCol+1) == '#') {
                    dir = Direction.DOWN;
                    continue;
                }
                curCol += 1;
            }
            else if (dir == Direction.DOWN){
                // out of bound
                if (curRow + 1 >= maxRow) break;
                // change direction
                if (map[curRow+1].charAt(curCol) == '#') {
                    dir = Direction.LEFT;
                    continue;
                }
                curRow += 1;
            }
            else {
                // out of bound
                if (curCol - 1 < 0) break;
                // change direction
                if (map[curRow].charAt(curCol-1) == '#') {
                    dir = Direction.UP;
                    continue;
                }
                // go on
                curCol -= 1;
            }
            if (!isVisited[curRow][curCol]) {
                isVisited[curRow][curCol] = true;
                potentialObstructionIdx.add(new int[]{curRow, curCol});
                distinctPathTraversedCount++;
            }
        }

        // Task 6a
        System.out.println("Task 6a:" + distinctPathTraversedCount);

        // Task 6b
        System.out.println("Task 6b:" + traverseWithNewObstruction(potentialObstructionIdx, map, row, col));
    }

    public static int traverseWithNewObstruction(List<int[]> potentialObstructionIndex, String[] map, int initRow, int initCol){
        int total = 0;
        int idx = map.length;
        int length = map[0].length();
        for (int[] obstructionIdx : potentialObstructionIndex) {
            int curRow = initRow;
            int curCol = initCol;
            Direction curDir = Direction.UP;
            HashSet<Position> hashSet = new HashSet();
            while (true) {
                if (curDir == Direction.UP){
                    // out of bound
                    if (curRow - 1 < 0) break;
                    Position newPosition = new Position(curRow, curCol, Direction.UP);
                    if(isExists(hashSet, newPosition)) {
                        total++;
                        break;
                    }
                    hashSet.add(newPosition);
                    // change direction
                    if (map[curRow-1].charAt(curCol) == '#' || (curRow-1 == obstructionIdx[0] && curCol == obstructionIdx[1])) {
                        curDir = Direction.RIGHT;
                        continue;
                    }
                    curRow -= 1;
                }
                else if (curDir == Direction.RIGHT){
                    // out of bound
                    if (curCol + 1 >= length) break;
                    Position newPosition = new Position(curRow, curCol, Direction.RIGHT);
                    if(isExists(hashSet, newPosition)) {
                        total++;
                        break;
                    }
                    hashSet.add(newPosition);
                    // change direction
                    if (map[curRow].charAt(curCol+1) == '#' || (curRow == obstructionIdx[0] && curCol+1 == obstructionIdx[1])) {
                        curDir = Direction.DOWN;
                        continue;
                    }
                    curCol += 1;
                }
                else if (curDir == Direction.DOWN){
                    // out of bound
                    if (curRow + 1 >= idx) break;
                    Position newPosition = new Position(curRow, curCol, Direction.DOWN);
                    if(isExists(hashSet, newPosition)) {
                        total++;
                        break;
                    }
                    hashSet.add(newPosition);
                    // change direction
                    if (map[curRow+1].charAt(curCol) == '#' || (curRow+1 == obstructionIdx[0] && curCol == obstructionIdx[1])) {
                        curDir = Direction.LEFT;
                        continue;
                    }
                    // go on
                    curRow += 1;
                }
                else {
                    // out of bound
                    if (curCol - 1 < 0) break;
                    Position newPosition = new Position(curRow, curCol, Direction.LEFT);
                    if(isExists(hashSet, newPosition)) {
                        total++;
                        break;
                    }
                    hashSet.add(newPosition);
                    // change direction
                    if (map[curRow].charAt(curCol-1) == '#' || (curRow == obstructionIdx[0] && curCol-1 == obstructionIdx[1])) {
                        curDir = Direction.UP;
                        continue;
                    }
                    // go on
                    curCol -= 1;
                }
            }
        }
        return total;
    }

    public static void main(String[] args)
    {

        File input = new File("./y2024/input.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            String line;
            int initRow = -1, initCol = -1;
            String[] map = new String[130];
            int idx = 0;
            while ((line = reader.readLine()) != null) {
                map[idx] = line;
                if (line.indexOf('^') != -1){
                    initRow = idx;
                    initCol = line.indexOf('^');
                }
                idx++;
            }
            solve(map, initRow, initCol, Direction.UP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

