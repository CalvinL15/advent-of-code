import java.io.*;

public class Task2b
{
  public static void main(String[] args)
  {
    File input = new File("input");
    try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
      String line;
      int total = 0;
      while ((line = reader.readLine()) != null) {
        int maxGreen = 0, maxRed = 0, maxBlue = 0;
        String gamesConfig = line.substring(line.lastIndexOf(':'));
        String[] sets = gamesConfig.split(";");
        sets[0] = sets[0].replace(":", "");
        for (String set : sets) {
          String[] cubes = set.split(",");
          for (String cube : cubes) {
            String[] result = cube.trim().split(" ");
            int num = Integer.parseInt(result[0]);
            if (result[1].equals("red") && num > maxRed)
              maxRed = num;
            else if (result[1].equals("green") && num > maxGreen)
              maxGreen = num;
            else if (result[1].equals("blue") && num > maxBlue)
              maxBlue = num;
          }
        }
        total += maxRed * maxGreen * maxBlue;
      }
      System.out.println(total);
    }
    catch (IOException e){
       e.printStackTrace();
    }
  }
}