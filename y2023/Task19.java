import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

class Range {
    int start;
    int end;
    public Range(int start, int end) {
        this.start = start;
        this.end = end;
    }
    long count() {
        return (long) end - start + 1;
    }
}

class Sequence {
    char origin;
    String destination;
    char operator;
    int associatedNumber;
    public Sequence(Character origin, char operator, int associatedNumber, String destination){
        this.origin = origin;
        this.operator = operator;
        this.associatedNumber = associatedNumber;
        this.destination = destination;
    }
}
class Rule {
    ArrayList<Sequence> rules;
    String fallbackDestination;
    public Rule(ArrayList<Sequence> rules, String fallbackDestination){
        this.rules = rules;
        this.fallbackDestination = fallbackDestination;
    }
}

public class Task19 {
    static int MAX_RATING = 4000;
    private static long calculateTotalAccepted(HashMap<String, Rule> workflows) {
        HashMap<Character, Range> initialRanges = new HashMap<>();
        initialRanges.put('x', new Range(1, MAX_RATING));
        initialRanges.put('m', new Range(1, MAX_RATING));
        initialRanges.put('a', new Range(1, MAX_RATING));
        initialRanges.put('s', new Range(1, MAX_RATING));
        return processWorkflows("in", workflows, initialRanges);
    }

    private static long calculateTotalCombinations(HashMap<Character, Range> ranges) {
        long totalCombinations = 1;

        for (Range range : ranges.values()) {
            long rangeCount = range.count();
            totalCombinations *= rangeCount;
        }

        return totalCombinations;
    }

    private static long processWorkflows(String workflowName, HashMap<String, Rule> workflows, HashMap<Character, Range> ranges){
        if (workflowName.equals("R")) return 0;
        if (workflowName.equals("A")) {
            return calculateTotalCombinations(ranges);
        }
        Rule rule = workflows.get(workflowName);
        long totalCount = 0;

        for (Sequence sequence : rule.rules) {
            HashMap<Character, Range> newRanges = new HashMap<>(ranges);
            Range currentRange = newRanges.get(sequence.origin);

            if (sequence.origin != ' ' && currentRange != null) {
                if (sequence.operator == '<') {
                    newRanges.put(sequence.origin, new Range(currentRange.start, sequence.associatedNumber - 1));
                    ranges.put(sequence.origin, new Range(sequence.associatedNumber, currentRange.end));
                } else if (sequence.operator == '>') {
                    newRanges.put(sequence.origin, new Range(sequence.associatedNumber + 1, currentRange.end));
                    ranges.put(sequence.origin, new Range(currentRange.start, sequence.associatedNumber));
                }
            }
            totalCount += processWorkflows(sequence.destination, workflows, newRanges);
        }

        // Handle the fallback destination
        totalCount += processWorkflows(rule.fallbackDestination, workflows, ranges);

        return totalCount;

    }

    public static void main(String[] args){
        File input = new File("/input.txt");
        String line;
        HashMap<String, Rule> workflows = new HashMap<>();
        int total = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            while(!(line = reader.readLine()).isEmpty()){
                String[] keysAndRules = line.substring(0, line.length()-1).split("\\{");
                ArrayList<Sequence> rules = new ArrayList();
                String[] rulesStr = keysAndRules[1].split(",");
                for (int i = 0; i<rulesStr.length-1; i++){
                    String[] ruleAndDest = rulesStr[i].split(":");
                    if (ruleAndDest[0].charAt(1) == '>'){
                        String[] val = ruleAndDest[0].split(">");
                        rules.add(new Sequence(val[0].charAt(0),'>', Integer.parseInt(val[1]), ruleAndDest[1]));
                    } else {
                        String[] val = ruleAndDest[0].split("<");
                        rules.add(new Sequence(val[0].charAt(0),'<',Integer.parseInt(val[1]), ruleAndDest[1]));
                    }
                }
                workflows.put(keysAndRules[0], new Rule(rules, rulesStr[rulesStr.length-1]));
            }

            // task A
            while((line = reader.readLine()) != null){
                String[] rules = line.substring(1, line.length() - 1).split(",");
                int x = Integer.parseInt(rules[0].split("=")[1]);
                int m = Integer.parseInt(rules[1].split("=")[1]);
                int a = Integer.parseInt(rules[2].split("=")[1]);
                int s = Integer.parseInt(rules[3].split("=")[1]);
                String curKey = "in";
                boolean isSearching = true;
                while (isSearching) {
                    if (curKey.equals("A")) {
                        total += x + m + a + s;
                        break;
                    } else if (curKey.equals("R")) {
                        break;
                    }
                    Rule seq = workflows.get(curKey);
                    ArrayList<Sequence> relevantRules = seq.rules;
                    String oldKey = curKey;
                    for (Sequence r : relevantRules) {
                        if (r.origin == 'x'){
                            if (r.operator == '<' && r.associatedNumber > x) {
                                curKey = r.destination;
                            } else if (r.operator == '>' && r.associatedNumber < x) {
                                curKey = r.destination;
                            }
                        } else if (r.origin == 'm') {
                            if (r.operator == '<' && r.associatedNumber > m) {
                                curKey = r.destination;
                            } else if (r.operator == '>' && r.associatedNumber < m) {
                                curKey = r.destination;
                            }
                        } else if (r.origin == 'a') {
                            if (r.operator == '<' && r.associatedNumber > a) {
                                curKey = r.destination;
                            } else if (r.operator == '>' && r.associatedNumber < a) {
                                curKey = r.destination;
                            }
                        } else {
                            if (r.operator == '<' && r.associatedNumber > s) {
                                curKey = r.destination;
                            } else if (r.operator == '>' && r.associatedNumber < s) {
                                curKey = r.destination;
                            }
                        }
                        if (!curKey.equals(oldKey))
                            break;
                    }
                    if (curKey.equals(oldKey)){
                        curKey = seq.fallbackDestination;
                    }
                }
            }

            System.out.println("task A: " + total);

            // task b
            System.out.println("task B: " + calculateTotalAccepted(workflows));

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
