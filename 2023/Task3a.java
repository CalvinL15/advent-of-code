import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class Task3a {
  public static boolean isCharSymbol(Character character){
    return !character.equals('.') && !Character.isDigit(character);
  }

  public static void main(String[] args)
  {
    File input = new File("input");
    try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
      String line;
      int total = 0, idx = 0;
      int[][] acceptedCoordinates = new int[140][140];
      ArrayList<String> lines = new ArrayList<>();
      while ((line = reader.readLine()) != null) {
        lines.add(line);
        int lineLength = line.length();
        for (int i = 0; i<lineLength; i++){
          if (isCharSymbol(line.charAt(i))){
            for (int di = -1; di <= 1; di++) {
              for (int dj = -1; dj <= 1; dj++) {
                int newI = i + di;
                int newJ = idx + dj;
                if (newI >= 0 && newI < 140 && newJ >= 0 && newJ < 140) {
                  acceptedCoordinates[newI][newJ] = 1;
                }
              }
            }
          }
        }
        idx++;
      }

      Pattern pattern = Pattern.compile("\\d+");
    
      for (int l = 0; l<lines.size(); l++){
        Matcher matcher = pattern.matcher(lines.get(l));

        while (matcher.find()) {
          String number = matcher.group();
          int indexOfNum = matcher.start();

          boolean isNumberAcceptable = false;
          for (int j = indexOfNum; j < indexOfNum + number.length(); j++) {
              if (acceptedCoordinates[j][l] == 1) {
                  isNumberAcceptable = true;
                  break;
              }
          }
          if (isNumberAcceptable) {
              total += Integer.parseInt(number);
          }
        }
      }
      System.out.println(total);
    }
    catch (IOException e){
       e.printStackTrace();
    }
  }
}