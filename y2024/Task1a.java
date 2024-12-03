package y2024;

import java.io.*;
import java.util.Arrays;

public class Task1a
{
    public static void main(String[] args)
    {
        int total = 0;
        int arr1[] = new int[1000];
        int arr2[] = new int[1000];
        int idx = 0;
        File input = new File("./y2024/input.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] numbers = line.split(" ");
                numbers = Arrays.stream(numbers)
                        .filter(value -> !value.isEmpty())
                        .toArray(String[]::new);
                arr1[idx] = Integer.parseInt(numbers[0]);
                arr2[idx] = Integer.parseInt(numbers[1]);
                idx++;
            }
            Arrays.sort(arr1);
            Arrays.sort(arr2);
            int arrLength = arr1.length;
            for (int i = 0; i< arrLength; i++){
                int distance = Math.abs(arr1[i] - arr2[i]);
                total += distance;
            }
            System.out.println(total);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}