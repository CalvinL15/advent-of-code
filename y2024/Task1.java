package y2024;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class Task1 {
    public static void main(String[] args)
    {
        int total1a = 0;
        int[] arr1 = new int[1000];
        int[] arr2 = new int[1000];

        int total1b = 0;
        int idx = 0;
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        File input = new File("./y2024/input.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] numbers = line.split(" ");
                numbers = Arrays.stream(numbers)
                        .filter(value -> !value.isEmpty())
                        .toArray(String[]::new);
                arr1[idx] = Integer.parseInt(numbers[0]);
                arr2[idx] = Integer.parseInt(numbers[1]);
                int key = Integer.parseInt(numbers[1]);
                if (hashMap.containsKey(key)){
                    hashMap.put(key, hashMap.get(key) + 1);
                } else hashMap.put(key, 1);
                idx++;
            }

            int arrLength = arr1.length;
            Arrays.sort(arr1);
            Arrays.sort(arr2);

            for (int i = 0; i< arrLength; i++){

                // solve 1a
                int distance = Math.abs(arr1[i] - arr2[i]);
                total1a += distance;

                // solve 1b
                if(hashMap.containsKey(arr1[i])){
                    int similarity = arr1[i]*hashMap.get(arr1[i]);
                    total1b += similarity;
                }
            }

            System.out.println("1a: " + total1a);
            System.out.println("1b: " + total1b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
