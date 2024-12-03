import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Task22 {
    private static void main(String[] args) {
        File input = new File("/input.txt");
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            while((line = reader.readLine()) != null) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
