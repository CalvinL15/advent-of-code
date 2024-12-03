package y2024;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Task2b {

    public static boolean isValid(int[] numbers) {
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
            return true;
        }
        return false;
    }

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
                // check if the array is valid without removal
                if (isValid(numbers)) {
                    totalReports++;
                    continue;
                }

                for (int i = 0; i < numbers.length; i++) {
                    // Create a new array without the i-th element
                    int[] newArray = new int[numbers.length - 1];
                    for (int j = 0, k = 0; j < numbers.length; j++) {
                        if (j != i) {
                            newArray[k++] = numbers[j];
                        }
                    }

                    // Check if removing this element makes the array valid
                    if (isValid(newArray)) {
                        totalReports++;
                        break;
                    }
                }
            }
            System.out.println(totalReports);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
