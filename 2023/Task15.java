import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Lens {
    int focusLength;
    int index;

    public Lens (int focusLength, int index){
        this.focusLength = focusLength;
        this.index = index;
    }
}

public class Task15 {
    private static int calculateValue(String label){
        int value = 0;
        for (int i = 0; i<label.length(); i++){
            value += (int) label.charAt(i);
            value *= 17;
            value %= 256;
        }
        return value;
    }

    public static void main(String[] args){
        File input = new File("2023/input.txt");
        String sequence;
        long totalTaskA = 0;
        long totalTaskB = 0;
        ArrayList<HashMap<String, Lens>> boxes = new ArrayList();

        // init box, task B
        for (int i = 0; i<256; i++){
            boxes.add(new HashMap<>());
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            sequence = reader.readLine();
            String[] steps = sequence.split(",");

            for (String step : steps) {
                Character operator = '=';
                if (step.indexOf('-') != -1){
                    operator = '-';
                }
                String[] labelAndFLength = step.split(String.valueOf(operator));
                String label = labelAndFLength[0];
                int boxNum = calculateValue(label);
                HashMap<String, Lens> currentBox = boxes.get(boxNum);
                if (operator == '-'){
                    if (currentBox.containsKey(label)) {
                        int idx = currentBox.get(label).index;
                        for (Map.Entry<String, Lens> entry : currentBox.entrySet()) {
                            String key = entry.getKey();
                            Lens value = entry.getValue();
                            if (value.index > idx) {
                                currentBox.get(key).index -= 1;
                            }
                        }
                        currentBox.remove(label);
                    }
                } else {
                    int focusLength = Integer.parseInt(labelAndFLength[1]);
                    if (currentBox.containsKey(label)) {
                        currentBox.put(label, new Lens(focusLength, boxes.get(boxNum).get(label).index));
                    } else {
                        currentBox.put(label, new Lens(focusLength, boxes.get(boxNum).size()));
                    }
                }
                // task A
                totalTaskA += calculateValue(step);
            }

            // task B
            for (int i = 0; i<boxes.size(); i++){
                HashMap<String, Lens> currentBox = boxes.get(i);
                for (Map.Entry<String, Lens> entry : currentBox.entrySet()) {
                    Lens value = entry.getValue();
                    totalTaskB += (i+1)*(value.index+1)*(value.focusLength);
                }
            }

            System.out.println("task a: " + totalTaskA);
            System.out.println("task b: " + totalTaskB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
