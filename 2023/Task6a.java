public class Task6a {
    public static int calculateWays(int[] time, int[] distance){
        int multipliedTotalWays = 1;
        for (int i = 0; i < time.length; i++){
            int startPoint = 1;
            int endPoint = time[i] / 2;
            while (startPoint * (time[i] - startPoint) <= distance[i] && startPoint <= endPoint){
                startPoint++;
            }
            int localTotalWays = (endPoint - startPoint + 1) * 2;
            if (time[i] % 2 == 0 && localTotalWays >= 1)
                localTotalWays--;
            multipliedTotalWays *= localTotalWays;
        }
        return multipliedTotalWays;
    }

    public static void main(String[] args) {
        int[] time = new int[] {46, 80, 78, 66};
        int[] distance = new int[] {214, 1177, 1402, 1024};
        System.out.println(calculateWays(time, distance));
    }
}
