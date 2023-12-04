import java.io.*;

public class Task4a {
  public static boolean isStringNumeric(String str){
    try {
      Integer.parseInt(str);
      return true;
    } catch (NumberFormatException e){
      return false;
    }
  }

  public static void main(String[] args)
  {
    File input = new File("input");
    try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
      String line;
      int total = 0;
      while ((line = reader.readLine()) != null) {
        boolean[] isWinningNumbers = new boolean[100];
        int roundPoints = 0;
        String card = line.substring(line.lastIndexOf(':'));
        String[] numbers = card.split("\\|");
        numbers[0] = numbers[0].replace(":", "");
        String[] winningNumbers = numbers[0].split(" ");
        String[] lotteryNumbers = numbers[1].split(" ");
        for (String winningNumber : winningNumbers) {
          if (isStringNumeric(winningNumber))
            isWinningNumbers[Integer.parseInt(winningNumber)] = true;
        }
        for (String lotteryNumber : lotteryNumbers) {
          if (isStringNumeric(lotteryNumber) && isWinningNumbers[Integer.parseInt(lotteryNumber)]) {
            if (roundPoints == 0) {
              roundPoints = 1;
            } else {
              roundPoints *= 2;
            }
          }
        }
        total += roundPoints;
      }
      System.out.println(total);
    }
    catch (IOException e){
       e.printStackTrace();
    }
  }
}