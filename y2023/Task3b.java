import java.util.*;
import java.util.ArrayList;
import java.io.*;

public class Task3b {
  
  public static boolean isGear(Character character){
      return character.equals('*');
  }

  private static int findStartingCol(String line, int col) {
      while (col > 0 && isNumeric(line.charAt(col - 1))) {
          col--;
      }
      return col;
  }

  private static boolean isNumeric(char c) {
      return c >= '0' && c <= '9';
  }

  private static List<Integer> findAdjacentNumbers(ArrayList<String> input, int row, int col) {
    List<Integer> numbers = new ArrayList<>();
    Set<String> processedStartPositions = new HashSet<>();
    int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

    for (int[] dir : directions) {
        int newRow = row + dir[0];
        int newCol = col + dir[1];

        // Check if the new position is within the grid and if this start position has not been processed before
        if (newRow >= 0 && newRow < input.size() && newCol >= 0 && newCol < input.get(newRow).length() 
            && isNumeric(input.get(newRow).charAt(newCol))) {

            int startingCol = findStartingCol(input.get(newRow), newCol);
            String startPositionKey = newRow + "," + startingCol;

            if (!processedStartPositions.contains(startPositionKey)) {
                int number = extractNumber(input, newRow, startingCol, startingCol <= newCol ? 1 : -1);

                // Add number to the list and mark this start position as processed
                numbers.add(number);
                processedStartPositions.add(startPositionKey);
            }
        }
    }
    return numbers;
  }

  private static int extractNumber(ArrayList<String> input, int row, int col, int deltaCol) {
      StringBuilder number = new StringBuilder();
      while (col >= 0 && col < input.get(row).length() && isNumeric(input.get(row).charAt(col))) {
          number.append(input.get(row).charAt(col));
          col += deltaCol;
      }
      if (deltaCol == -1) {
        number.reverse();
      }
      return Integer.parseInt(number.toString());
  }

  public static void main(String[] args)
  {
      File input = new File("input");
      try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
          String line;
          int total = 0, idx = 0;
          ArrayList<String> lines = new ArrayList<>();
          ArrayList<Integer> gearXCoordinates = new ArrayList<>();
          ArrayList<Integer> gearYCoordinates = new ArrayList<>();
          while ((line = reader.readLine()) != null) {
            lines.add(line);
            int lineLength = line.length();
            for (int i = 0; i<lineLength; i++){
              if (isGear(line.charAt(i))){
                gearXCoordinates.add(i);
                gearYCoordinates.add(idx);
              }
            }
            idx++;
          }

          for (int i = 0; i<gearXCoordinates.size(); i++){
            List<Integer> adjacentNumbers = findAdjacentNumbers(lines, gearXCoordinates.get(i), gearYCoordinates.get(i));
            if (adjacentNumbers.size() == 2) {
              total += adjacentNumbers.get(0) * adjacentNumbers.get(1);
            }
          }

          System.out.println(total);
        }
    catch (IOException e){
       e.printStackTrace();
    }
  }
}