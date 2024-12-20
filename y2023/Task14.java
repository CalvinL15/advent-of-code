import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Task14 {
    private static ArrayList<String> transformMap(ArrayList<String> map, boolean isPushRocks, boolean needRealign, String direction){
        int mapLength = map.get(0).length();
        ArrayList<String> transformedMap = new ArrayList<>();

        if (needRealign) {
            for(int i = 0; i<mapLength; i++){
                StringBuilder builder = new StringBuilder();
                for (String l : map){
                    builder.append(l.charAt(i));
                }
                if (isPushRocks) {
                    transformedMap.add(pushRocks(builder.toString(), direction));
                }
                else transformedMap.add(builder.toString());
            }
        } else {
            for (String l : map){
                if (isPushRocks) {
                    transformedMap.add(pushRocks(l, direction));
                }
            }
        }

        return transformedMap;
    }

    private static String pushRocks(String input, String direction) {
        char[] chars = input.toCharArray();

        if (direction.equals("left")) {
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == 'O') {
                    for (int j = i; j > 0; j--) {
                        if (chars[j - 1] == '#') break;
                        char temp = chars[j];
                        chars[j] = chars[j - 1];
                        chars[j - 1] = temp;
                    }
                }
            }
        } else if (direction.equals("right")) {
            for (int i = chars.length - 1; i >= 0; i--) {
                if (chars[i] == 'O') {
                    for (int j = i; j < chars.length - 1; j++) {
                        if (chars[j + 1] == '#') break;
                        char temp = chars[j];
                        chars[j] = chars[j + 1];
                        chars[j + 1] = temp;
                    }
                }
            }
        }

        return new String(chars);
    }

    public static void main(String[] args){
        File input = new File("/input.txt");
        String line;
        int totalTaskA = 0;
        int totalTaskB = 0;
        ArrayList<String> map = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            while ((line = reader.readLine()) != null) {
                map.add(line);
            }

            // task a
            ArrayList<String> transformedMap = transformMap(map, true, true,"left");
            transformedMap = transformMap(transformedMap, false, true,"left");

            // task b
            ArrayList<ArrayList<String>> maps = new ArrayList<>();

            int curIdx = 0;
            int cycleStartIdx = -1;
            int cycleEndIdx = -1;

            while (true) {

                // do transformation
                maps.add(transformMap(curIdx == 0 ? map : maps.get(curIdx - 1), true, true,"left"));
                maps.set(curIdx, transformMap(maps.get(curIdx), false, true,"left"));
                maps.set(curIdx, transformMap(maps.get(curIdx), true, false, "left"));
                maps.set(curIdx, transformMap(maps.get(curIdx), true, true,"right"));
                maps.set(curIdx, transformMap(maps.get(curIdx), false, true,"right"));
                maps.set(curIdx, transformMap(maps.get(curIdx), true, false,"right"));

                // do comparison
                boolean isAllEqual = false;
                for (int i = 0; i < curIdx; i++){
                    boolean isAllEqualLocal = true;
                    for (int j = 0; j < maps.get(i).size(); j++){
                        if(!maps.get(i).get(j).equals(maps.get(curIdx).get(j))){
                            isAllEqualLocal = false;
                            break;
                        }
                    }
                    if (isAllEqualLocal) {
                        cycleStartIdx = i;
                        cycleEndIdx = curIdx;
                        isAllEqual = true;
                    }
                }

                if (isAllEqual) {
                    break;
                }

                curIdx++;
            }

            ArrayList<String> correctMap = maps.get(cycleStartIdx + (1000000000 - cycleStartIdx - 1) % (cycleEndIdx - cycleStartIdx));

            // task a
            totalTaskA = getTotalTask(totalTaskA, transformedMap);

            // task b
            totalTaskB = getTotalTask(totalTaskB, correctMap);

            System.out.println("task A: " + totalTaskA);
            System.out.println("task B: " + totalTaskB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static int getTotalTask(int totalTask, ArrayList<String> correctMap) {
        for (int i = 0; i<correctMap.size(); i++){
            String curRow = correctMap.get(i);
            int amountOfRocks = 0;
            for (int j = 0; j<curRow.length(); j++){
                if (curRow.charAt(j) == 'O'){
                    amountOfRocks++;
                }
            }
            totalTask += amountOfRocks*(correctMap.size()-i);
        }
        return totalTask;
    }
}
