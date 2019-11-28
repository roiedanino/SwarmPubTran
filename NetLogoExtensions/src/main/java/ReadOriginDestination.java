import com.opencsv.CSVReader;
import org.nlogo.api.*;

import org.nlogo.core.Syntax;
import org.nlogo.core.SyntaxJ;
import org.nlogo.extensions.table.TableExtension.*;

import java.io.FileReader;
import java.util.Iterator;


public class ReadOriginDestination implements Reporter {

    public static int PATH_ARG = 0;
    public static int NUM_OF_LINES_ARG = 1;

    public static final String
            K_INIT_NODE = "origin", K_TERM_NODE = "dest",
            K_CAPACITY = "capacity", K_LENGTH = "length",
            K_FREE_FLOW_TIME = "free-flow-time", K_B = "b",
            K_POWER = "power", K_SPEED_LIMIT = "speed-limit",
            K_TOLL = "toll", K_TYPE = "type";

    /**
     *
     * @param args - Path:String NumOfLines:Int
     * @param context
     * @return List of tables containing rows from origin-destination csv data
     * @throws ExtensionException
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
    @Override
    public Object report(Argument[] args, Context context) throws ExtensionException {
        final int HEADER_LINES = 8;

        String keys[] = new String[]{K_INIT_NODE, K_TERM_NODE, K_CAPACITY, K_LENGTH,
                K_FREE_FLOW_TIME, K_B, K_POWER, K_SPEED_LIMIT, K_TOLL, K_TYPE};

        String path = args[PATH_ARG].getString();
        int numberOfLines = args[NUM_OF_LINES_ARG].getIntValue();

        LogoListBuilder tablesListBuilder = new LogoListBuilder();
        CSVReader reader = null;
        try {

            reader = new CSVReader(new FileReader(path), '\t');
            reader.skip(HEADER_LINES);
            Iterator<String[]> linesIterator = reader.iterator();

            for (int i = 0; i < numberOfLines && linesIterator.hasNext() ; i++) {

                Table rowContentTable = new Table();
                String[] row = linesIterator.next();

                for (int j = 0; j < keys.length; j++) {
                    rowContentTable.put(keys[j], row[j + 1]);
                }
                tablesListBuilder.add(rowContentTable);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return tablesListBuilder.toLogoList();
    }

    @Override
    public Syntax getSyntax() {
        return SyntaxJ.reporterSyntax(new int[]{Syntax.StringType(), Syntax.NumberType()}, Syntax.ListType());
    }
}
