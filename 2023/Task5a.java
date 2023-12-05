import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Task5a {

    public static void main(String[] args) {
        File input = new File("2023/input");
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            String line;
            long lowestLocation =  Long.MAX_VALUE;
            // get seeds
            line = reader.readLine();
            String seedsString = line.split(":")[1].trim();
            String[] seedNumbers = seedsString.split(" ");
            ArrayList<Long> seeds = new ArrayList<>();
            for (String seedNumber : seedNumbers) {
                seeds.add(Long.parseLong(seedNumber));
            }
            String[] mapTitle = { "seed-to-soil map:", "soil-to-fertilizer map:", "fertilizer-to-water map:", "water-to-light map:",
            "light-to-temperature map:", "temperature-to-humidity map:", "humidity-to-location map:", ""};
            int mapIdx = 0;
            boolean[] isTransformed = new boolean[seeds.size()];
            while ((line = reader.readLine()) != null) {
                if (line.equals(mapTitle[mapIdx])) {
                    mapIdx++;
                    isTransformed = new boolean[seeds.size()];
                }
                else if (!line.isEmpty()){
                    String[] getNumbers = line.split(" ");
                    long destinationRange = Long.parseLong(getNumbers[0]);
                    long sourceRange = Long.parseLong(getNumbers[1]);
                    long rangeLength = Long.parseLong(getNumbers[2]);
                    for (Long seed : seeds) {
                        if (seed >= sourceRange && seed <= sourceRange+rangeLength) {
                            int indexOfSeed = seeds.indexOf(seed);
                            if (!isTransformed[indexOfSeed]) {
                                seeds.set(indexOfSeed, seed - sourceRange + destinationRange);
                                isTransformed[indexOfSeed] = true;
                            }
                        }
                    }
                }
            }
            for (Long seed : seeds) {
                if (seed < lowestLocation) {
                    lowestLocation = seed;
                }
            }
            System.out.println(lowestLocation);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
