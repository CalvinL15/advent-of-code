import java.io.*;

public class Task2a
{
  public static boolean areCubesEnough(String[] result) {
    int num = Integer.parseInt(result[0]);
    return (!result[1].equals("red") || num <= 12) &&
            (!result[1].equals("green") || num <= 13) &&
            (!result[1].equals("blue") || num <= 14);
  }

  public static void main(String[] args)
  {
    File input = new File("input");
    try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
      String line;
      int total = 0;
      int idx = 1;
      while ((line = reader.readLine()) != null) {
        boolean isPossible = true;
        String gamesConfig = line.substring(line.lastIndexOf(':'));
        String[] sets = gamesConfig.split(";");
        sets[0] = sets[0].replace(":", "");
        for (String set : sets) {
          String[] cubes = set.split(",");
          for (String cube : cubes) {
            String[] result = cube.trim().split(" ");
            if (!areCubesEnough(result))
              isPossible = false;
          }
        }
        if (isPossible) {
          total += idx;
        }
        idx++; 
      }
      System.out.println(total);
    }
    catch (IOException e){
       e.printStackTrace();
    }
  }
}