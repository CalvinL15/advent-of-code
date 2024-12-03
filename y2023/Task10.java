import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
    | is a vertical pipe connecting north and south.
    - is a horizontal pipe connecting east and west.
    L is a 90-degree bend connecting north and east.
    J is a 90-degree bend connecting north and west.
    7 is a 90-degree bend connecting south and west.
    F is a 90-degree bend connecting south and east.
 */

class Loop {
    int size;
    boolean[][] loopTilesCoordinate;

    ArrayList<Vertices> vertices;
    public Loop(int size, boolean[][] loopTilesCoordinate, ArrayList<Vertices> vertices){
        this.size = size;
        this.loopTilesCoordinate = loopTilesCoordinate;
        this.vertices = vertices;
    }
}

class Vertices {
    int x;
    int y;

    public Vertices(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

public class Task10 {
    public static Map<Character, Character> findAcceptablePipes(int dx, int dy){
        Map<Character, Character> acceptablePipes = new HashMap<>();
        if (dy == -1) {
            acceptablePipes.put('7', '7');
            acceptablePipes.put('F', 'F');
            acceptablePipes.put('|', '|');
        } else if (dy == 1){
            acceptablePipes.put('L', 'L');
            acceptablePipes.put('J', 'J');
            acceptablePipes.put('|', '|');
        } else if (dx == -1){
            acceptablePipes.put('-', '-');
            acceptablePipes.put('L', 'L');
            acceptablePipes.put('F', 'F');
        } else if (dx == 1) {
            acceptablePipes.put('-', '-');
            acceptablePipes.put('7', '7');
            acceptablePipes.put('J', 'J');
        }
        return acceptablePipes;
    }

    // Shoelace formula
    public static double calculateArea(ArrayList<Vertices> vertices) {
        double area = 0;
        int n = vertices.size();
        for (int i = 0; i < n - 1; i++) {
            area += vertices.get(i).x * vertices.get(i + 1).y;
            area -= vertices.get(i).y * vertices.get(i + 1).x;
        }

        area += vertices.get(n - 1).x * vertices.get(0).y;
        area -= vertices.get(n - 1).y * vertices.get(0).x;
        return Math.abs(area / 2);
    }

    public static Loop findLoop(Character[][] tiles, int cPX, int cPY, int dx, int dy) {
        Map<Character, Character> acceptablePipes;
        int loopLength = 0;
        boolean[][] loopTiles = new boolean[tiles.length][tiles[0].length];
        ArrayList<Vertices> vertices = new ArrayList<>();
        Loop loopData = new Loop(loopLength, loopTiles, vertices);
        while (true) {
            acceptablePipes = findAcceptablePipes(dx, dy);
            // Check if next position is within the bounds of the tiles
            if (cPY + dy < 0 || cPY + dy >= tiles.length || cPX + dx < 0 || cPX + dx >= tiles[0].length) {
                loopData.size = -1;
                return loopData;
            }

            Character nextPipe = tiles[cPY + dy][cPX + dx];

            if (nextPipe == 'S') {
                loopData.size = loopLength + 1;
                loopTiles[cPY+dy][cPX+dx] = true;
                loopData.loopTilesCoordinate = loopTiles;
                loopData.vertices = vertices;
                return loopData;
            } else if (nextPipe == '.' || acceptablePipes.get(nextPipe) == null) {
                loopData.size = -1;
                return loopData;
            }

            int ndx = dx, ndy = dy;
            if (nextPipe == 'L') {
                if (dy != 0) {
                    ndx = 1;
                    ndy = 0;
                } else {
                    ndx = 0;
                    ndy = -1;
                }
            } else if (nextPipe == 'J') {
                if (dy != 0) {
                    ndx = -1;
                    ndy = 0;
                } else {
                    ndx = 0;
                    ndy = -1;
                }
            } else if (nextPipe == '7') {
                if (dy != 0) {
                    ndy = 0;
                    ndx = -1;
                } else {
                    ndy = 1;
                    ndx = 0;
                }
            } else if (nextPipe == 'F') {
                if (dy != 0) {
                    ndy = 0;
                    ndx = 1;
                } else {
                    ndy = 1;
                    ndx = 0;
                }
            }
            cPX += dx;
            cPY += dy;
            loopTiles[cPY][cPX] = true;
            vertices.add(new Vertices(cPX, cPY));
            dx = ndx;
            dy = ndy;
            loopLength++;
        }
    }

    public static void main(String[] args) {
        File input = new File("/input.txt");
        String line;
        Character[][] tiles = new Character[140][140];
        int startingPositionX = 0;
        int startingPositionY = 0;
        int idx = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            while ((line = reader.readLine()) != null) {
                int lineLength = line.length();
                for (int i = 0; i<lineLength; i++){
                    tiles[idx][i] = line.charAt(i);
                    if (tiles[idx][i] == 'S') {
                        startingPositionX = i;
                        startingPositionY = idx;
                    }
                }
                idx++;
            }
            // start with going north, and then: south, west, east
            Loop loopData = findLoop(tiles, startingPositionX, startingPositionY, 0, -1);
            if (loopData.size == -1) {
                loopData = findLoop(tiles, startingPositionX, startingPositionY, 0, 1);
            }
            if (loopData.size == -1) {
                loopData = findLoop(tiles, startingPositionX, startingPositionY, -1, 0);
            }
            if (loopData.size == -1) {
                loopData = findLoop(tiles, startingPositionX, startingPositionY, 1, 0);
            }

            // task a
            System.out.println(loopData.size/2);

            // task b
            double polygonArea = calculateArea(loopData.vertices);
            System.out.print("One of these two numbers is the correct answer: ");
            System.out.print((int) (polygonArea - loopData.size/2 + 1) + " " + (Math.round(polygonArea) - loopData.size/2 + 1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
