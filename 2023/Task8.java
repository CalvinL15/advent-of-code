import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Task8 {

    public static long gcd(long number1, long number2) {
        if (number1 == 0 || number2 == 0) {
            return number1 + number2;
        } else {
            long absNumber1 = Math.abs(number1);
            long absNumber2 = Math.abs(number2);
            long biggerValue = Math.max(absNumber1, absNumber2);
            long smallerValue = Math.min(absNumber1, absNumber2);
            return gcd(biggerValue % smallerValue, smallerValue);
        }
    }

    public static long lcm(long number1, long number2) {
        if (number1 == 0 || number2 == 0)
            return 0;
        else {
            long gcd = gcd(number1, number2);
            return Math.abs(number1 * number2) / gcd;
        }
    }



    public static int solve8a(String instruction, Map<String, Node> nodes) {
        int instructionLength = instruction.length();
        String curLocation = "AAA";
        int steps = 0;
        while(!curLocation.equals("ZZZ")) {
            if(instruction.charAt(steps % instructionLength) == 'L') {
                curLocation = nodes.get(curLocation).leftNode;
            } else {
                curLocation = nodes.get(curLocation).rightNode;
            }
            steps++;
        }
        return steps;
    }

    public static long solve8b(String instruction, List<String> startingNodes, Map<String, Node> nodes) {
        int instructionLength = instruction.length();
        int howManyNodes = startingNodes.size();
        int steps = 0;
        int[][] first2TimesReachZ = new int[howManyNodes][100];
        int[] startingPoints = new int[howManyNodes];
        int[] difs = new int[howManyNodes];
        for (int i = 0; i<howManyNodes; i++){
            int timesReachZ = 0;
            while (timesReachZ < 2) {
                if(instruction.charAt(steps % instructionLength) == 'L') {
                    startingNodes.set(i, nodes.get(startingNodes.get(i)).leftNode);
                } else {
                    startingNodes.set(i, nodes.get(startingNodes.get(i)).rightNode);
                }
                steps++;
                if(startingNodes.get(i).charAt(2) == 'Z') {
                    first2TimesReachZ[i][timesReachZ] = steps;
                    timesReachZ++;
                }
            }
        }
        for (int i = 0; i<howManyNodes; i++){
            startingPoints[i] = first2TimesReachZ[i][0];
            difs[i] = first2TimesReachZ[i][1] - first2TimesReachZ[i][0];
        }

        long curLCM = lcm(difs[0],difs[1]);
        for (int i = 2; i<howManyNodes; i++){
            curLCM = lcm(curLCM, difs[i]);
        }

        long lcmMultiple = 0;
        for (long startingPoint : startingPoints) {
            long multiple = ((startingPoint + curLCM - 1) / curLCM) * curLCM;
            if (multiple > lcmMultiple) {
                lcmMultiple = multiple;
            }
        }
        // 13939
        // 1405209 + 11309
        // 2545577 + 20777
        // 4618017 + 15517
        // 6171821 + 17621
        // 7953646 + 18673
        return lcmMultiple;
    }

    public static void main(String[] args) {
        File input = new File("2023/input.txt");
        String instruction;
        Map<String, Node> nodes = new HashMap<>();
        List<String> startingNodes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            String line;
            instruction = reader.readLine();
            reader.readLine(); // empty line in between instruction and nodes
            while ((line = reader.readLine()) != null) {
                String[] elements = line.split("=");
                String originNode = elements[0].trim();
                if (originNode.charAt(originNode.length() - 1) == 'A') {
                    startingNodes.add(originNode);
                }
                String[] connectingNodes = elements[1].trim().split(", ");
                String leftNode = connectingNodes[0].substring(1,4);
                String rightNode = connectingNodes[1].substring(0,3);
                nodes.put(originNode, new Node(leftNode, rightNode));
            }
            // System.out.println(solve8a(instruction, nodes));
            System.out.println(solve8b(instruction, startingNodes, nodes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Node {
    String leftNode;
    String rightNode;
    public Node(String leftNode, String rightNode){
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }
}
