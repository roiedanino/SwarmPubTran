package sample;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.util.*;

public class RoadsReader {

    public static final String
            K_INIT_NODE = "origin", K_TERM_NODE = "dest",
            K_CAPACITY = "capacity", K_LENGTH = "length",
            K_FREE_FLOW_TIME = "free-flow-time", K_B = "b",
            K_POWER = "power", K_SPEED_LIMIT = "speed-limit",
            K_TOLL = "toll", K_TYPE = "type";

    /**
     *
     * @param path - String path to the file
     * @return List of tables containing rows from origin-destination csv data
     *
     * ** Each row contains (separated by tab):
     *      Init Node       0
     *      Term Node       1
     *      Capacity        2
     *      Length          3
     *      Free Flow Time  4
     *      B               5
     *      Power           6
     *      Speed Limit     7
     *      Toll            8
     *      Type            9
     *      ;               10
     */

    public static List<Map<String, Double>> readFrom(String path) {
        final int HEADER_LINES = 9;

        String keys[] = new String[]{K_INIT_NODE, K_TERM_NODE, K_CAPACITY, K_LENGTH,
                K_FREE_FLOW_TIME, K_B, K_POWER, K_SPEED_LIMIT, K_TOLL, K_TYPE};

        CSVReader reader = null;
        List<Map<String, Double>> tablesList = new ArrayList<>();
        try {

            reader = new CSVReader(new FileReader(path), '\t');
            reader.skip(HEADER_LINES);
            Iterator<String[]> linesIterator = reader.iterator();

            for (int i = 0; linesIterator.hasNext() ; i++) {
                Map<String, Double> rowContentTable = new HashMap<>();
                String[] row = linesIterator.next();
                if(row[0].startsWith("~")){
                    continue;
                }
                for (int j = 0; j < keys.length; j++) {
                    rowContentTable.put(keys[j], Double.parseDouble(row[j + 1]));
                }
                tablesList.add(rowContentTable);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return tablesList;
    }
}
