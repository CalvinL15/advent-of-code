package y2024;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Task4 {

    public static int countXmas4a(int row, int col, String[] lines){
        int count = 0;
        int length = lines[row].length();
        int size = lines.length;
        // case 1, vertical right
        if (col+3 < length && lines[row].charAt(col+1) == 'M' && lines[row].charAt(col+2) == 'A' && lines[row].charAt(col+3) == 'S'){
            count++;
        }
        // case 2, vertical left
        if (col-3 >= 0 && lines[row].charAt(col-1) == 'M' && lines[row].charAt(col-2) == 'A' && lines[row].charAt(col-3) == 'S'){
            count++;
        }
        // case 3, horizontal down
        if (row+3 < size && lines[row+1].charAt(col) == 'M' && lines[row+2].charAt(col) == 'A' && lines[row+3].charAt(col) == 'S'){
            count++;
        }
        // case 4, horizontal top
        if (row-3 >= 0 && lines[row-1].charAt(col) == 'M' && lines[row-2].charAt(col) == 'A' && lines[row-3].charAt(col) == 'S'){
            count++;
        }

        // case 5, diagonal up left
        if (row-3 >= 0 && col-3 >= 0 && lines[row-1].charAt(col-1) == 'M' && lines[row-2].charAt(col-2) == 'A' && lines[row-3].charAt(col-3) == 'S'){
            count++;
        }

        // case 5, diagonal up right
        if (row-3 >= 0 && col+3 < length && lines[row-1].charAt(col+1) == 'M' && lines[row-2].charAt(col+2) == 'A' && lines[row-3].charAt(col+3) == 'S'){
            count++;
        }

        // case 7, diagonal down right
        if (row+3 < size && col+3 < length && lines[row+1].charAt(col+1) == 'M' && lines[row+2].charAt(col+2) == 'A' && lines[row+3].charAt(col+3) == 'S'){
            count++;
        }

        // case 8, diagonal down left
        if (row+3 < size && col-3 >= 0 && lines[row+1].charAt(col-1) == 'M' && lines[row+2].charAt(col-2) == 'A' && lines[row+3].charAt(col-3) == 'S'){
            count++;
        }

        return count;
    }
    public static int countXmas4b(int row, int col, String[] lines){
        int count = 0;
        int length = lines[row].length();
        int size = lines.length;

        // boundary checking
        if (col -1 < 0 || row-1 < 0 || col + 1 >= length || row+1 >= size)
            return 0;

        // case 1: MAS, MAS
        if (lines[row-1].charAt(col-1) == 'M' && lines[row+1].charAt(col+1) == 'S' && lines[row-1].charAt(col+1) == 'M' && lines[row+1].charAt(col-1) == 'S')
            count++;

        // case 2: MAS, SAM
        if (lines[row-1].charAt(col-1) == 'M' && lines[row+1].charAt(col+1) == 'S' && lines[row-1].charAt(col+1) == 'S' && lines[row+1].charAt(col-1) == 'M')
            count++;

        // case 3: SAM, SAM
        if (lines[row-1].charAt(col-1) == 'S' && lines[row+1].charAt(col+1) == 'M' && lines[row-1].charAt(col+1) == 'S' && lines[row+1].charAt(col-1) == 'M')
            count++;

        // case 4: SAM, MAS
        if (lines[row-1].charAt(col-1) == 'S' && lines[row+1].charAt(col+1) == 'M' && lines[row-1].charAt(col+1) == 'M' && lines[row+1].charAt(col-1) == 'S')
            count++;

        return count;
    }

    public static void main(String[] args)
    {
        int total4a = 0;
        int total4b = 0;
        File input = new File("./y2024/input.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            String line;
            String[] lines = new String[140];
            int arrLength = 0;
            while ((line = reader.readLine()) != null) {
                lines[arrLength] = line;
                arrLength++;
            }
            int length = lines[0].length();
            for (int i = 0; i<arrLength; i++){
                for (int j = 0; j<length; j++){
                    if (lines[i].charAt(j) == 'X')
                        total4a += countXmas4a(i, j, lines);
                    if (lines[i].charAt(j) == 'A')
                        total4b += countXmas4b(i, j, lines);
                }
            }
            System.out.println("4a: " + total4a);
            System.out.println("4b: " + total4b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
