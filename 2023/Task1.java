import java.io.*;

public class Task1
{
	public static String convertString(String input) {
        String[] digitWords = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
        String result = input;

        for (int i = 0; i < digitWords.length; i++) {          
            String word = digitWords[i];
            String replacement = "" + word.charAt(0) + i + word.charAt(word.length() - 1);
            result = result.replaceAll(word, replacement);        
        }

        return result;
    }
	
	public static void main(String[] args)
	{
		int total = 0;
        File input = new File("input");
		try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // convert string for task 1b, use original string for task 1a.
            	String convertedString = convertString(line);
            	int firstNum = 0;
				int secondNum = 0;
                int lineLength = convertedString.length();
                for (int i = 0; i<lineLength; i++){
                	if (Character.isDigit(convertedString.charAt(i))) {
                		firstNum = Character.getNumericValue(convertedString.charAt(i));
                		break;
                	}
                }
                for (int j = lineLength-1; j>=0; j--) {
                	if (Character.isDigit(convertedString.charAt(j))) {
                		secondNum = Character.getNumericValue(convertedString.charAt(j));
                		break;
                	}
                }
                int combinedNum = firstNum * 10 + secondNum;
                total += combinedNum;
            }
            
            System.out.println(total);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}