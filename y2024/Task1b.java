package y2024;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class Task1b {
    public static void main(String[] args)
    {
        int total = 0;
        int leftList[] = new int[1000];
        int idx = 0;
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        File input = new File("./y2024/input.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] numbers = line.split(" ");
                numbers = Arrays.stream(numbers)
                        .filter(value -> !value.isEmpty())
                        .toArray(String[]::new);
                leftList[idx] = Integer.parseInt(numbers[0]);
                int key = Integer.parseInt(numbers[1]);
                if (hashMap.containsKey(key)){
                    hashMap.put(key, hashMap.get(key) + 1);
                } else hashMap.put(key, 1);
                idx++;
            }

            int arrLength = leftList.length;
            for (int i = 0; i< arrLength; i++){
                if(hashMap.containsKey(leftList[i])){
                    int similarity = leftList[i]*hashMap.get(leftList[i]);
                    total += similarity;
                }
            }
            System.out.println(total);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
