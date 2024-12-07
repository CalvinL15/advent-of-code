package y2024;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Task7 {

    public static boolean containsTarget(long[] arr, long target, boolean is7b) {
        Set<Long> results = new HashSet<>();
        generateValues(arr, 0, arr[0], results, is7b);
        return results.contains(target);
    }

    private static void generateValues(long[] arr, int index, long current, Set<Long> results, boolean is7b) {
        if (index == arr.length - 1) {
            results.add(current);
            return;
        }

        generateValues(arr, index + 1, current + arr[index + 1], results, is7b);
        generateValues(arr, index + 1, current * arr[index + 1], results, is7b);
        // for task7b
        if (is7b) {
            String concatString = current + Long.toString(arr[index+1]);
            generateValues(arr, index + 1, Long.parseUnsignedLong(concatString), results, true);
        }
    }

    private static long solve7a(long[] numbers, long target){
        long result = 0;
        if (containsTarget(numbers, target, false)){
            result += target;
        }
        return result;
    }

    private static long solve7b(long[] numbers, long target){
        long result = 0;
        if (containsTarget(numbers, target, true)){
            result += target;
        }
        return result;
    }

    public static void main(String[] args)
    {
        File input = new File("./y2024/input.txt");
        long total7a = 0;
        long total7b = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] equation  = line.split(": ");
                long target = Long.parseLong(equation[0]);
                long[] numbers = Arrays.stream(equation[1].split(" "))
                        .mapToLong(Long::parseLong)
                        .toArray();
                total7a += solve7a(numbers, target);
                total7b += solve7b(numbers, target);
            }
            System.out.println(total7a);
            System.out.println(total7b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
