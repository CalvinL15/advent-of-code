package y2024;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task3b {

    public static void main(String[] args)
    {
        long total = 0;
        File input = new File("./y2024/input.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            String line = "";
            boolean isEnabled = true;
            while ((line = reader.readLine()) != null) {
                String regex = "mul\\((\\d{1,3}),(\\d{1,3})\\)|((don't|do)\\(\\))";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    if(matcher.group(1) == null){
                        String instruction = matcher.group();
                        if (instruction.equals("don't()")) isEnabled = false;
                        else isEnabled = true;
                    } else if (isEnabled){
                        total += Integer.parseInt(matcher.group(1)) * Integer.parseInt(matcher.group(2));
                    }
                }
            }
            System.out.println(total);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

