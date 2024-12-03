package y2024;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Task2a {
    public static void main(String[] args)
    {
        int totalReports = 0;
        File input = new File("./y2024/input.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                int[] numbers = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();
                if (numbers.length == 2 && Math.abs(numbers[1] - numbers[0]) <= 3) {
                    totalReports++;
                    continue;
                }
                boolean isUnknown = true;
                boolean isIncreasing = false;
                boolean isDecreasing = false;
                boolean isValid = true;

                for (int i = 1; i<numbers.length; i++){
                    if (Math.abs(numbers[i] - numbers[i-1]) > 3 || numbers[i] == numbers[i-1]){
                        isValid = false;
                        break;
                    }
                    if (numbers[i] > numbers[i-1] && isUnknown) {
                        isIncreasing = true;
                        isUnknown = false;
                        continue;
                    }
                    if (numbers[i] < numbers[i-1] && isUnknown) {
                        isDecreasing = true;
                        isUnknown = false;
                        continue;
                    }
                    if (numbers[i] > numbers[i-1] && isDecreasing || numbers[i] < numbers[i-1] && isIncreasing) {
                        isValid = false;
                        break;
                    }
                }

                if (isValid) {
                    totalReports++;
                }
            }
            System.out.println(totalReports);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
