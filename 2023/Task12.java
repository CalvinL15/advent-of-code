import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Task12 {
    public static long countPossibleArrangement(HashMap<String, Long> memoMap, String record, int[] contiguousGroup, int recordIndex, int groupIndex, int currentGroupSize){
        String key = recordIndex + "-" + groupIndex + "-" + currentGroupSize;

        // Return the memoized result if available
        if (memoMap.containsKey(key)) {
            return memoMap.get(key);
        }

        if (recordIndex == record.length()) {
            boolean isLastGroupCompleted = groupIndex == contiguousGroup.length - 1 && contiguousGroup[groupIndex] == currentGroupSize;
            boolean isAllGroupsProcessed = groupIndex == contiguousGroup.length && currentGroupSize == 0;
            return isLastGroupCompleted || isAllGroupsProcessed ? 1 : 0;
        }

        long totalArrangements = 0;
        char currentChar = record.charAt(recordIndex);

        // Case 1: process an operational symbol or move to the next group
        if ((currentChar == '.' || currentChar == '?') && (currentGroupSize == 0 || (groupIndex < contiguousGroup.length && currentGroupSize == contiguousGroup[groupIndex]))) {
            totalArrangements += countPossibleArrangement(memoMap, record, contiguousGroup, recordIndex+1, groupIndex < contiguousGroup.length && currentGroupSize == contiguousGroup[groupIndex] ? groupIndex + 1 : groupIndex, 0);
        }

        // Case 2: process a broken symbol
        if (currentChar == '#' || currentChar == '?') {
            totalArrangements += countPossibleArrangement(memoMap, record, contiguousGroup, recordIndex+1, groupIndex, currentGroupSize + 1);
        }

        // Store result in the memoization map
        memoMap.put(key, totalArrangements);
        return totalArrangements;
    }

    public static void main(String[] args) {
        File input = new File("2023/input.txt");
        String line;
        long totalTaskA = 0;
        long totalTaskB = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            while ((line = reader.readLine()) != null) {
                HashMap<String, Long> memoMap = new HashMap<>();
                String[] records = line.split(" ");
                String[] contiguousGroupStr = records[1].split(",");
                // task a
                int[] contiguousGroup = new int[contiguousGroupStr.length];
                for (int i = 0; i<contiguousGroupStr.length; i++){
                    contiguousGroup[i] = Integer.parseInt(contiguousGroupStr[i]);
                }

                // task b
                String unfoldedRecord = records[0] + "?" + records[0] + "?" + records[0] + "?" + records[0] + "?" + records[0];
                int[] unfoldedContiguousGroup = new int[contiguousGroupStr.length * 5];
                int originalGroupSizes = contiguousGroupStr.length;
                for (int i = 0; i<contiguousGroupStr.length; i++){
                    unfoldedContiguousGroup[i] = Integer.parseInt(contiguousGroupStr[i]);
                    unfoldedContiguousGroup[i+originalGroupSizes] = Integer.parseInt(contiguousGroupStr[i]);
                    unfoldedContiguousGroup[i+originalGroupSizes*2] = Integer.parseInt(contiguousGroupStr[i]);
                    unfoldedContiguousGroup[i+originalGroupSizes*3] = Integer.parseInt(contiguousGroupStr[i]);
                    unfoldedContiguousGroup[i+originalGroupSizes*4] = Integer.parseInt(contiguousGroupStr[i]);
                }

                // task a
                totalTaskA += countPossibleArrangement(memoMap, records[0], contiguousGroup, 0, 0, 0);

                // task b
                totalTaskB += countPossibleArrangement(memoMap, unfoldedRecord, unfoldedContiguousGroup, 0, 0, 0);
            }
            System.out.println("task a: " + totalTaskA);
            System.out.println("task b: " + totalTaskB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
