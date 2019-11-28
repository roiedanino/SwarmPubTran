import com.opencsv.CSVReader;
import org.nlogo.api.*;
import org.nlogo.core.Syntax;
import org.nlogo.core.SyntaxJ;

import java.io.FileReader;
import java.util.Iterator;


public class ReadOrigins implements Reporter {

    /**
        Example File:


     Origin 	1
     1 :      0.0;     2 :    100.0;     3 :    100.0;     4 :    500.0;     5 :    200.0;
     6 :    300.0;     7 :    500.0;     8 :    800.0;     9 :    500.0;    10 :   1300.0;
     11 :    500.0;    12 :    200.0;    13 :    500.0;    14 :    300.0;    15 :    500.0;
     16 :    500.0;    17 :    400.0;    18 :    100.0;    19 :    300.0;    20 :    300.0;
     21 :    100.0;    22 :    400.0;    23 :    300.0;    24 :    100.0;

     Origin 	2
     1 :    100.0;     2 :      0.0;     3 :    100.0;     4 :    200.0;     5 :    100.0;
     6 :    400.0;     7 :    200.0;     8 :    400.0;     9 :    200.0;    10 :    600.0;
     11 :    200.0;    12 :    100.0;    13 :    300.0;    14 :    100.0;    15 :    100.0;
     16 :    400.0;    17 :    200.0;    18 :      0.0;    19 :    100.0;    20 :    100.0;
     21 :      0.0;    22 :    100.0;    23 :      0.0;    24 :      0.0;

     */
    @Override
    public Object report(Argument[] args, Context context) throws ExtensionException {
        final int HEADER_LINES = 5;
        String path = args[0].getString();
        int maxOrigins = args[1].getIntValue();
        int numOfOrigins = 0;
        LogoListBuilder originsListBuilder = new LogoListBuilder();


        CSVReader reader = null;
        try {

            reader = new CSVReader(new FileReader(path),';');
            reader.skip(HEADER_LINES);

            LogoListBuilder originPairBuilder = new LogoListBuilder();
            Iterator<String[]> iterator = reader.iterator();
            while (iterator.hasNext() && numOfOrigins <= maxOrigins) {
                for (String originStr : iterator.next()){
                    String[] originPair = originStr.split(":");
                    if(originPair.length >= 2 && !originPair[0].isEmpty()) {
                        //Integer integerVal = Integer.parseInt(originPair[0].trim());
                        double doubleVal = Double.parseDouble(originPair[1].trim());
                        //LogoListBuilder pair = new LogoListBuilder();
                        //pair.add(integerVal.doubleValue());
                        //pair.add(doubleVal);
                        originPairBuilder.add(doubleVal);//pair.toLogoList()
                    }

                    if(originPair[0].startsWith("Origin")){
                        numOfOrigins++;
                        if(numOfOrigins > 1) {
                            originsListBuilder.add(originPairBuilder.toLogoList());
                        }
                        originPairBuilder = new LogoListBuilder();
                        //originPairBuilder.add(originPair[0]);
                        System.out.println(originPair[0]);
                    }
                }
            }

           // rowsEntries.forEach(strArr -> handleRow(strArr, originsListBuilder));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return originsListBuilder.toLogoList();
    }


    @Override
    public Syntax getSyntax() {
        return SyntaxJ.reporterSyntax(new int[]{Syntax.StringType(), Syntax.NumberType()}, Syntax.ListType());
    }
}
