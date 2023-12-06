public class Task6b {
    public static long calculateWays(long time, long distance){
        long totalWays;
        long startPoint = 1;
        long endPoint = time / 2;
        while (startPoint * (time - startPoint) <= distance && startPoint <= endPoint){
            startPoint++;
        }
        totalWays = (endPoint - startPoint + 1) * 2;
        if (time % 2 == 0 && totalWays >= 1)
            totalWays--;
        return totalWays;
    }

    public static void main(String[] args) {
        long time = 46807866;
        long distance = 214117714021024L;
        System.out.println(calculateWays(time, distance));
    }
}
