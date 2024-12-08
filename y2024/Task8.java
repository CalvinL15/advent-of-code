package y2024;

import common.Coordinate;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Task8 {

    private static boolean isCoordinateInBound(int maxRow, int maxCol, Coordinate coordinate) {
        return coordinate.row < maxRow && coordinate.row >= 0 && coordinate.col < maxCol && coordinate.col >= 0;
    }

    private static int updateCount(HashSet<Coordinate> antiNodesLocation, int maxRow, int maxCol, int res, Coordinate possibleCoord1, Coordinate possibleCoord2) {
        if (isCoordinateInBound(maxRow, maxCol, possibleCoord1) && !antiNodesLocation.contains(possibleCoord1)){
            res++;
            antiNodesLocation.add(possibleCoord1);
        }
        if (isCoordinateInBound(maxRow, maxCol, possibleCoord2) && !antiNodesLocation.contains(possibleCoord2)){
            res++;
            antiNodesLocation.add(possibleCoord2);
        }
        return res;
    }

    public static int count(List<Coordinate> coordinateList, HashSet<Coordinate> antiNodesLocation, int maxRow, int maxCol, int i, int j, boolean is8a){
        int count = 0;
        for (Coordinate c : coordinateList){
            if (is8a) {
                Coordinate possibleCoord1 = new Coordinate(2*i-c.row, 2*j-c.col);
                Coordinate possibleCoord2 = new Coordinate(2*c.row-i, 2*c.col-j);
                count = updateCount(antiNodesLocation, maxRow, maxCol, count, possibleCoord1, possibleCoord2);
            } else {
                int mult = 0;
                int difX1 = i - c.row, difY1 = j-c.col;
                int difX2 = c.row - i, difY2 = c.col - j;
                while (true) {
                    Coordinate possibleCoord1 = new Coordinate(mult*difX1+i, mult*difY1+j);
                    Coordinate possibleCoord2 = new Coordinate(mult*difX2+c.row, mult*difY2+c.col);
                    count = updateCount(antiNodesLocation, maxRow, maxCol, count, possibleCoord1, possibleCoord2);
                    if (!isCoordinateInBound(maxRow, maxCol, possibleCoord1) && !isCoordinateInBound(maxRow, maxCol, possibleCoord2))
                        break;
                    mult++;
                }
            }
        }
        return count;
    }
    public static void main(String[] args)
    {
        File input = new File("./y2024/input.txt");
        long total8a = 0;
        long total8b = 0;
        String[] map = new String[50];
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null) {
                map[index] = line;
                index++;
            }
            HashSet<Coordinate> antiNodesLocation8a = new HashSet();
            HashSet<Coordinate> antiNodesLocation8b = new HashSet();

            int maxCol = map[0].length();
            HashMap<Character, List<Coordinate>> hashMap = new HashMap();
            for (int i = 0; i<index; i++){
                for (int j = 0; j<maxCol; j++){
                    if(map[i].charAt(j) == '.') continue;
                    Coordinate coordinate = new Coordinate(i, j);
                    Character frequency = map[i].charAt(j);
                    List<Coordinate> coordinateList;
                    if (!hashMap.containsKey(frequency)){
                        coordinateList = new ArrayList();
                        coordinateList.add(coordinate);
                        hashMap.put(frequency, coordinateList);
                    } else {
                        coordinateList = hashMap.get(frequency);
                        // solve for 8a
                        total8a += count(coordinateList, antiNodesLocation8a, index, maxCol, i, j, true);
                        // solve for 8b
                        total8b += count(coordinateList, antiNodesLocation8b, index, maxCol, i, j, false);
                        coordinateList.add(coordinate);
                    }
                }
            }
            System.out.println("8a: " + total8a);
            System.out.println("8b: " + total8b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
