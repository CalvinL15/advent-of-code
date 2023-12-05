import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Task5b {
    public static void main(String[] args) {
        File input = new File("2023/input");
        long lowestLocation = Long.MAX_VALUE;
        ArrayList<ConversionMap> conversionMaps = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            String line;
            line = reader.readLine();
            String seedsString = line.split(":")[1].trim();
            String[] seedNumbers = seedsString.split(" ");
            ArrayList<Long> seeds = new ArrayList<>();
            ArrayList<Long> seedsRange = new ArrayList<>();

            for (int i = 0; i < seedNumbers.length; i++){
                if (i % 2 == 0) {
                    seeds.add(Long.parseLong(seedNumbers[i]));
                } else {
                    seedsRange.add(Long.parseLong(seedNumbers[i]));
                }
            }

            String[] mapTitle = { "seed-to-soil map:", "soil-to-fertilizer map:", "fertilizer-to-water map:", "water-to-light map:",
                    "light-to-temperature map:", "temperature-to-humidity map:", "humidity-to-location map:", ""};
            int mapIdx = 0;
            ArrayList<Long> destinations = new ArrayList<>();
            ArrayList<Long> sources = new ArrayList<>();
            ArrayList<Long> ranges = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                if(line.equals(mapTitle[mapIdx])){
                    conversionMaps.add(new ConversionMap(destinations, sources, ranges));
                    mapIdx++;
                    destinations = new ArrayList<>();
                    sources = new ArrayList<>();
                    ranges = new ArrayList<>();
                } else if (!line.isEmpty()){
                    String[] getNumbers = line.split(" ");
                    destinations.add(Long.parseLong(getNumbers[0]));
                    sources.add(Long.parseLong(getNumbers[1]));
                    ranges.add(Long.parseLong(getNumbers[2]));
                }
            }
            conversionMaps.add(new ConversionMap(destinations, sources, ranges));
            // parallelize to make code run somewhat faster
            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            List<Future<Long>> results = new ArrayList<>();
            for (int i = 0; i < seeds.size(); i++) {
                final int index = i;
                Callable<Long> task = () -> {
                    long lowestLoc = Long.MAX_VALUE;
                    for (long j = seeds.get(index); j < seeds.get(index) + seedsRange.get(index); j++) {
                        long[] returnValues = returnValueAndBound(j, conversionMaps);
                        if (returnValues[0] < lowestLoc) {
                            lowestLoc = returnValues[0];
                        }
                        j += returnValues[1];
                    }
                    return lowestLoc;
                };
                results.add(executor.submit(task));
            }
            for (Future<Long> result : results) {
                try {
                    long localLowest = result.get();
                    if (localLowest < lowestLocation) {
                        lowestLocation = localLowest;
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            executor.shutdown();
            System.out.println(lowestLocation);
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    private static long[] returnValueAndBound(long val, List<ConversionMap> conversionMaps) {
        long bound = Long.MAX_VALUE;
        for (ConversionMap conversionMap : conversionMaps) {
            long[] valueAndBound = conversionMap.convert(val);
            bound = Math.min(bound, valueAndBound[1]);
            val = valueAndBound[0];
        }
        return new long[]{val, bound};
    }
}

class ConversionMap {
    ArrayList<Long> starts;
    ArrayList<Long> ends;
    ArrayList<Long> betweens;

    public ConversionMap(ArrayList<Long> destinations, ArrayList<Long> sources, ArrayList<Long> ranges) {
        starts = new ArrayList<>();
        ends = new ArrayList<>();
        betweens = new ArrayList<>();
        for (int i = 0; i<destinations.size(); i++) {
            starts.add(sources.get(i));
            ends.add(destinations.get(i));
            betweens.add(ranges.get(i));
        }
    }
    public long[] convert(long val) {
        long nextStart = Long.MAX_VALUE;
        for (int i = 0; i < starts.size(); i++) {
            if (starts.get(i) > val) {
                nextStart = Math.min(nextStart, starts.get(i) - val - 1);
            }
            if (starts.get(i) <= val && starts.get(i) + betweens.get(i) > val) {
                return new long[]{ends.get(i) + (val - starts.get(i)), betweens.get(i) - (val - starts.get(i)) - 1};
            }
        }
        return new long[]{val, nextStart == Long.MAX_VALUE ? 0 : nextStart};
    }
}
