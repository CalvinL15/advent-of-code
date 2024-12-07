package y2024;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Task2 {

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

        return isValid;
    }

    public static void main(String[] args)
    {
        int total2a = 0;
        int total2b = 0;
        File input = new File("./y2024/input.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int[] numbers = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();
                if (numbers.length == 2 && Math.abs(numbers[1] - numbers[0]) <= 3) {
                    total2a++;
                    total2b++;
                    continue;
                }
                // check if the array is valid without removal
                if (isValid(numbers)) {
                    total2a++;
                    total2b++;
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
                        total2b++;
                        break;
                    }
                }
            }
            System.out.println("2a: " + total2a);
            System.out.println("2b: " + total2b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
