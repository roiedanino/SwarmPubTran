package sample;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RoadPopularityConverter {
    static final String ROADS_POPULARITY_PATH = "/Users/roie/Desktop/Research Assistance/GitRepos/" +
            "Applying-Swarm-Intelligence-For-Public-Transportation/Data Sets/TransportationNetworks-master/" +
            "SiouxFalls/roads_popularity.txt";
    static final String MAPPING_ROADS_PATH = "/Users/roie/Desktop/Research Assistance/GitRepos/Applying-Swarm-" +
            "Intelligence-For-Public-Transportation/Data Sets/TransportationNetworks-master/" +
            "SiouxFalls/map_roads_numbers.txt";

    private static Map<Integer, Integer> readRoadMapping() throws FileNotFoundException {
        Map<Integer,Integer> map = new HashMap<>();
        Scanner scanner = new Scanner(new File(MAPPING_ROADS_PATH));
        scanner.nextLine();

        while(scanner.hasNext()){
            int greenMapIndex = scanner.nextInt();
            int googleMapIndex = scanner.nextInt();
            map.put(googleMapIndex, greenMapIndex);
            if(scanner.hasNextLine())
                scanner.nextLine();
            System.out.println(greenMapIndex + " : " + googleMapIndex);
         }
        scanner.close();
        return map;
    }

    public static List<BusLine> readBusLines(List<Road> roads) throws FileNotFoundException {
        Map<Integer, Integer> roadMap = readRoadMapping();
        final int LINE = 0, ROADS = 1, FREQ = 2;
        Scanner scanner = new Scanner(new File(ROADS_POPULARITY_PATH));
        scanner.nextLine();
        List<BusLine> lines = new ArrayList<>();

        while (scanner.hasNext()){
            String[] parts = scanner.nextLine().split("\\|");
            int lineNumber = Integer.parseInt(parts[LINE].trim());
            int frequency = Integer.parseInt(parts[FREQ].trim());
            System.out.println(lineNumber + " - " + frequency);
            List<Road> lineRoads = Stream.of(parts[ROADS].trim().split(" ")).map(Integer::parseInt)
                    .map(roadMap::get).map(roads::get)
                    .collect(Collectors.toList());
            lines.add(new BusLine(lineNumber, lineRoads, frequency));
        }

        scanner.close();
        return lines;
    }

}
