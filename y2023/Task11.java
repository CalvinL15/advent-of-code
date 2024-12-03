import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Coordinates {

    long originalX;
    long x;
    long y;

    public Coordinates(long x, long y){
        this.originalX = x;
        this.x = x;
        this.y = y;
    }
}

public class Task11 {
    private static long findDistance(Coordinates a, Coordinates b){
        long disX = Math.abs(a.x - b.x);
        long disY = Math.abs(a.y - b.y);
        return disX + disY;
    }

    public static void main(String[] args) {
        File input = new File("/input.txt");
        String line;
        String[] lines = new String[280];
        ArrayList<Coordinates> coordinates = new ArrayList<>();
        long curY = 0;
        int arrY = 0;
        long totalDistance = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            while ((line = reader.readLine()) != null) {
                if(line.indexOf('#') == -1){
                    lines[arrY] = line;
                    // change to +2 instead of 1000000 for task a
                    curY += 1000000;
                } else {
                    lines[arrY] = line;
                    for (int i = 0; i<line.length(); i++){
                        if(line.charAt(i) == '#'){
                            coordinates.add(new Coordinates(i, curY));
                        }
                    }
                    curY++;
                }
                arrY++;
            }
            // modify x position of coordinates
            int lineLength = lines[0].length();
            for(int i = 0; i<lineLength; i++){
                boolean isVoid = true;
                for (String l : lines){
                    if(l == null) break;
                    if (l.charAt(i) == '#'){
                        isVoid = false;
                        break;
                    }
                }
                if (isVoid) {
                    for (Coordinates coordinate : coordinates) {
                        if (coordinate.originalX > i)
                            // change to 1 instead of 999999 for task 11a
                            coordinate.x += 999999;
                    }
                }
            }
            int coordinatesNum = coordinates.size();
            for (int i = 0; i<coordinatesNum-1; i++){
                for (int j = i+1; j<coordinatesNum; j++){
                    totalDistance += findDistance(coordinates.get(i), coordinates.get(j));
                }
            }
            System.out.println(totalDistance);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
