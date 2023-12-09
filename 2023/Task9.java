import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Task9 {
    private static int extrapolateForward(int numbersLength, int[] numbers){
        int lastDifNum = 0;
        int curDifLength = numbersLength - 1;
        while (true){
            boolean isAllNumSame = true;
            for (int i = 0; i<curDifLength; i++){
                numbers[i] = numbers[i+1] - numbers[i];
                if (i > 0 && numbers[i] != numbers[i-1])
                    isAllNumSame = false;
            }

            lastDifNum+=numbers[curDifLength-1];

            if (isAllNumSame){
                break;
            } else {
                curDifLength--;
            }
        }
        return lastDifNum;
    }

    private static int extrapolateBackward(int numbersLength, int[] numbers) {
        int curDifLength = numbersLength - 1;
        ArrayList<Integer> firstNumbers = new ArrayList<>();
        while (true){
            boolean isAllNumSame = true;
            for (int i = 0; i<curDifLength; i++){
                numbers[i] = numbers[i+1] - numbers[i];
                if (i < curDifLength-1 && numbers[i+1] != numbers[i])
                    isAllNumSame = false;
            }
            if (isAllNumSame){
                break;
            } else {
                curDifLength--;
            }
            firstNumbers.add(numbers[0]);
        }
        int listSize = firstNumbers.size();
        int ans = 0;
        for (int i = listSize - 1; i>0; i--){
            if (i == listSize - 1) {
                ans = firstNumbers.get(i-1) - firstNumbers.get(i);
            } else {
                ans = firstNumbers.get(i-1) - ans;
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        File input = new File("2023/input.txt");
        String line;
        int total = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            while ((line = reader.readLine()) != null) {
                String[] stringNumbers = line.split(" ");
                int numbersLength = stringNumbers.length;
                int[] numbers = new int[numbersLength];
                for (int i = 0; i<numbersLength;i++){
                    numbers[i] = Integer.parseInt(stringNumbers[i]);
                }

                // use extrapolateForward for task9a, extrapolateBackward for task9b
                // total += numbers[numbersLength-1] + extrapolateForward(numbersLength, numbers);
                total += (numbers[0] - extrapolateBackward(numbersLength, numbers));
            }
            System.out.println(total);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
