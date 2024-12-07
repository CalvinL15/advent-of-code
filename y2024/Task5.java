package y2024;

import common.HashMapWithSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Task5 {

    public static int solve(List<String> rules, HashMapWithSet hashMap, int[] numbers, boolean is5a){
        Set hashSet = new HashSet();
        int length = numbers.length;
        boolean isValid = true;
        for (int number : numbers) {
            HashSet setRules = hashMap.get(number);
            if (!Collections.disjoint(hashSet, setRules)) {
                isValid = false;
                break;
            }
            hashSet.add(number);
        }

        if (is5a && isValid) {
            return numbers[length / 2];
        }

        if (!is5a && !isValid) {
            reorderArray(rules, numbers);
            return numbers[length / 2];
        }
        return 0;
    }

    public static void reorderArray(List<String> rules, int[] array) {
        Map<Integer, Set<Integer>> dependencies = new HashMap<>();

        for (String rule : rules) {
            String[] parts = rule.split("\\|");
            int from = Integer.parseInt(parts[0]);
            int to = Integer.parseInt(parts[1]);
            dependencies.computeIfAbsent(from, k -> new HashSet<>()).add(to);
        }

        // Reorder the array
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = i + 1; j < array.length; j++) {
                if (dependencies.containsKey(array[i]) &&
                        dependencies.get(array[i]).contains(array[j])) {
                    // Swap to satisfy the rule
                    int temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
        }
    }

    public static void main(String[] args)
    {
        int total5a = 0;
        int total5b = 0;
        HashMapWithSet hashMap = new HashMapWithSet();
        List<String> rules = new ArrayList<>();

        File input = new File("./y2024/input.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            String line;
            // read rules
            while ((line = reader.readLine()) != null) {
                if (line.equals("")) break;
                rules.add(line);
                String[] rule = line.split("\\|");
                int num1 = Integer.parseInt(rule[0]);
                int num2 = Integer.parseInt(rule[1]);
                hashMap.put(num1, num2);
            }

            while ((line = reader.readLine()) != null) {
                total5a += solve(rules, hashMap, Arrays.stream(line.split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray(), true);
                total5b += solve(rules, hashMap, Arrays.stream(line.split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray(), false);
            }
            System.out.println("5a: " + total5a);
            System.out.println("5b: " + total5b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
