import com.opencsv.CSVReader;
import org.nlogo.api.*;
import org.nlogo.core.Syntax;
import org.nlogo.core.SyntaxJ;
import scala.Int;

import java.io.FileReader;
import java.util.Arrays;
import java.util.Iterator;

public class ReadRegionsCoordinates  implements Reporter {

    /**
     * receives path to coordinates file and max number of regions to read and returns list of coordinates [ x y ]
     */

    @Override
    public Object report(Argument[] args, Context context) throws ExtensionException {
        final int PATH_ARG = 0, MAX_ARG = 1, HEADER_LINES = 1, X = 1, Y = 2;
        String path = args[PATH_ARG].getString();
        int max = args[MAX_ARG].getIntValue();
        CSVReader reader = null;
        LogoListBuilder coordinatesListBuilder = new LogoListBuilder();

        try {
            reader = new CSVReader(new FileReader(path), ';');
            reader.skip(HEADER_LINES);

            Iterator<String[]> linesIterator = reader.iterator();

            for (int i = 0; linesIterator.hasNext() && i < max ; i++) {
                String[] row = linesIterator.next();
                row = row[0].split("[\t ]+");
                LogoListBuilder coordinateBuilder = new LogoListBuilder();
                coordinateBuilder.add(Double.parseDouble(row[X]));
                coordinateBuilder.add(Double.parseDouble(row[Y]));
                coordinatesListBuilder.add(coordinateBuilder.toLogoList());

            }

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return coordinatesListBuilder.toLogoList();
    }

    @Override
    public Syntax getSyntax() {
        return SyntaxJ.reporterSyntax(new int[]{Syntax.StringType(), Syntax.NumberType()}, Syntax.ListType());
    }
}
