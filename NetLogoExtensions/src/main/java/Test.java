import org.nlogo.api.*;
import org.nlogo.core.Syntax;
import org.nlogo.core.SyntaxJ;

public class Test implements Reporter {
    @Override
    public Object report(Argument[] args, Context context) throws ExtensionException {
        String stringVal = args[0].getString();
        Integer intVal = args[1].getIntValue();
        LogoListBuilder logoListBuilder = new LogoListBuilder();

        logoListBuilder.add(stringVal);
        logoListBuilder.add(intVal.toString());

        return logoListBuilder.toLogoList();
    }

    @Override
    public Syntax getSyntax() {
        return SyntaxJ.reporterSyntax(new int[]{Syntax.StringType(), Syntax.NumberType()}, Syntax.ListType());
    }
}
