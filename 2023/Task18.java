import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class Instruction {
    Character direction;
    long value;

    public Instruction(Character direction, long value) {
        this.direction = direction;
        this.value = value;
    }
}
class Point {
    int x, y;
    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

public class Task18 {

    public static List<Point> generateVertices(List<Instruction> plan){
        List<Point> vertices = new ArrayList<>();
        int x = 0, y = 0;

        vertices.add(new Point(x, y));

        for (Instruction ins : plan) {
            switch (ins.direction) {
                case 'R' -> x += ins.value;
                case 'L' -> x -= ins.value;
                case 'U' -> y -= ins.value;
                case 'D' -> y += ins.value;
            }
            vertices.add(new Point(x, y));
        }

        return vertices;
    }

    public static long calculatePositiveArea(List<Point> vertices) {
        long area = calculateArea(vertices);
        if (area >= 0) {
            return area;
        }
        Collections.reverse(vertices);
        return calculateArea(vertices);
    }

    public static long calculateArea(List<Point> vertices) {
        long area = 0;
        int n = vertices.size();
        for (int i = 0; i < n - 1; i++) {
            area += (long) vertices.get(i).x * vertices.get(i + 1).y;
            area -= (long) vertices.get(i).y * vertices.get(i + 1).x;
        }

        area += (long) vertices.get(n - 1).x * vertices.get(0).y;
        area -= (long) vertices.get(n - 1).y * vertices.get(0).x;
        return Math.abs(area / 2);
    }

    public static long computePointsInsidePolygon(long area, long numEdges) {
        return area - numEdges / 2 + 1;
    }

    public static void main(String[] args){
        File input = new File("2023/input.txt");
        String line;
        List<Instruction> plansA = new ArrayList<>();
        List<Instruction> plansB = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            while((line = reader.readLine()) != null) {
                String[] information = line.split(" ");
                String hexCode = information[2];
                char direction = 'R';
                char d = hexCode.charAt(hexCode.length() - 2);
                if (d == '1') direction = 'D';
                else if (d == '2') direction = 'L';
                else if (d == '3') direction = 'U';
                plansA.add(new Instruction(information[0].charAt(0), Long.parseLong(information[1])));
                plansB.add(new Instruction(direction, Long.parseLong(hexCode.substring(2,7), 16)));
            }
            List<Point> verticesA = generateVertices(plansA);
            List<Point> verticesB = generateVertices(plansB);
            long lengthA = 0;
            for (Instruction plan : plansA) {
                lengthA += plan.value;
            }
            long lengthB = 0;
            for (Instruction plan : plansB) {
                lengthB += plan.value;
            }
            System.out.println("task A: " + (computePointsInsidePolygon(calculatePositiveArea(verticesA), lengthA) + lengthA));
            System.out.println("task B: " + (computePointsInsidePolygon(calculatePositiveArea(verticesB), lengthB) + lengthB));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
