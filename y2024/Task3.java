package y2024;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ValueAndIsEnabled {
    int value;
    boolean isEnabled;
    public ValueAndIsEnabled(int value, boolean isEnabled){
        this.value = value;
        this.isEnabled = isEnabled;
    }
}
public class Task3 {
    public static int solve3a(String line){
        int value = 0;
        String regex = "mul\\((\\d{1,3}),(\\d{1,3})\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            value += Integer.parseInt(matcher.group(1)) * Integer.parseInt(matcher.group(2));
        }
        return value;
    }
    public static ValueAndIsEnabled solve3b(String line, boolean isEnabled){
        int value = 0;
        String regex = "mul\\((\\d{1,3}),(\\d{1,3})\\)|((don't|do)\\(\\))";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            if (matcher.group(1) == null) {
                String instruction = matcher.group();
                isEnabled = !instruction.equals("don't()");
            } else if (isEnabled){
                value += Integer.parseInt(matcher.group(1)) * Integer.parseInt(matcher.group(2));
            }
        }
        return new ValueAndIsEnabled(value, isEnabled);
    }
    public static void main(String[] args)
    {
        long total3a = 0;
        long total3b = 0;
        File input = new File("./y2024/input.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            String line;
            boolean isEnabled = true;
            while ((line = reader.readLine()) != null) {
                total3a += solve3a(line);
                ValueAndIsEnabled retVal = solve3b(line, isEnabled);
                total3b += retVal.value;
                isEnabled = retVal.isEnabled;
            }
            System.out.println("3a: " + total3a);
            System.out.println("3b: " + total3b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

