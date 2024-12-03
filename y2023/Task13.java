import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Task13 {

    private static boolean isDifferentByOne(String str1, String str2){
        int differingCount = 0;
        for (int i = 0; i < str1.length(); i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                differingCount++;
                if (differingCount > 1) {
                    return false;
                }
            }
        }
        return true;
    }
    private static int findMirror(ArrayList<String> pattern, boolean isTaskB){
        int patSize = pattern.size();
        ArrayList<Integer> possibleMirrorLocation = new ArrayList<>();
        for (int i = 0; i<patSize-1; i++){
            if (pattern.get(i).equals(pattern.get(i+1)) || (isDifferentByOne(pattern.get(i), pattern.get(i+1)) && isTaskB)){
                possibleMirrorLocation.add(i);
            }
        }

        if (possibleMirrorLocation.size() > 0){
            for (int i = 0; i < possibleMirrorLocation.size(); i++){
                boolean isDifferenceByOneAllowed = true;
                int cur1 = possibleMirrorLocation.get(i);
                int cur2 = possibleMirrorLocation.get(i) + 1;
                boolean isMirror = true;
                while(cur1 >= 0 && cur2 < patSize && isMirror){
                    if (pattern.get(cur1).equals(pattern.get(cur2))){
                        cur1--;
                        cur2++;
                    } else if (isDifferentByOne(pattern.get(cur1), pattern.get(cur2)) && isDifferenceByOneAllowed && isTaskB) {
                        cur1--;
                        cur2++;
                        isDifferenceByOneAllowed = false;
                    } else {
                        isMirror = false;
                        break;
                    }
                }
                if ((isMirror && !isTaskB) || (isMirror && isTaskB && !isDifferenceByOneAllowed)) {
                    return possibleMirrorLocation.get(i) + 1;
                }
            }
        }
        return -1;
    }

    private static int findVerticalMirror(ArrayList<String> pattern, boolean isTaskB){
        int lineLength = pattern.get(0).length();
        ArrayList<String> transformedLines = new ArrayList<>();

        for(int i = 0; i<lineLength; i++){
            StringBuilder builder = new StringBuilder();
            for (String p : pattern){
                builder.append(p.charAt(i));
            }
            transformedLines.add(builder.toString());
        }
        return findMirror(transformedLines, isTaskB);
    }

    private static int findMirrorLocation(ArrayList<String> pattern, boolean isTaskB){
        int mirrorLocation = findVerticalMirror(pattern, isTaskB);
        if (mirrorLocation == -1) {
            mirrorLocation = findMirror(pattern, isTaskB)*100;
        }
        return mirrorLocation;
    }

    public static void main(String[] args) {
        File input = new File("/input.txt");
        String line;
        int totalTaskA = 0;
        int totalTaskB = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            ArrayList<String> pattern = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                // solve current pattern after all strings are scanned
                if (line.isEmpty()){
                    totalTaskA += findMirrorLocation(pattern, false);
                    totalTaskB += findMirrorLocation(pattern, true);
                    pattern.clear();
                } else {
                    pattern.add(line);
                }
            }

            totalTaskA += findMirrorLocation(pattern, false);
            totalTaskB += findMirrorLocation(pattern, true);

            System.out.println("task 13a: " + totalTaskA);
            System.out.println("task 13b: " + totalTaskB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
