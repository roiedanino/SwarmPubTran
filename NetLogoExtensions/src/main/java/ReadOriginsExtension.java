import org.nlogo.api.*;

public class ReadOriginsExtension extends DefaultClassManager {

    @Override
    public void load(PrimitiveManager primManager) throws ExtensionException {
        primManager.addPrimitive("read-flow-mat", new ReadOrigins());
        primManager.addPrimitive("test", new Test());
        primManager.addPrimitive("read-origin-dest", new ReadOriginDestination());
        primManager.addPrimitive("read-regions-coordinates", new ReadRegionsCoordinates());
    }
}
