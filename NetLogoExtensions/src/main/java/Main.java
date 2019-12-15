import org.nlogo.api.*;
import org.nlogo.core.LogoList;
import org.nlogo.core.Token;

import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ReadOrigins readOrigins = new ReadOrigins();
        Argument[] arguments = new Argument[2];

        arguments[0] = new Argument() {
            @Override
            public Object get() throws ExtensionException, LogoException {
                return null;
            }

            @Override
            public AgentSet getAgentSet() throws ExtensionException, LogoException {
                return null;
            }

            @Override
            public Agent getAgent() throws ExtensionException, LogoException {
                return null;
            }

            @Override
            public Boolean getBoolean() throws ExtensionException, LogoException {
                return null;
            }

            @Override
            public boolean getBooleanValue() throws ExtensionException, LogoException {
                return false;
            }

            @Override
            public int getIntValue() throws ExtensionException, LogoException {
                return 0;
            }

            @Override
            public double getDoubleValue() throws ExtensionException, LogoException {
                return 0;
            }

            @Override
            public LogoList getList() throws ExtensionException, LogoException {
                return null;
            }

            @Override
            public Patch getPatch() throws ExtensionException, LogoException {
                return null;
            }

            @Override
            public String getString() throws ExtensionException, LogoException {
                return "/Users/roie/Desktop/Research Assistance/Data Sets/" +
                        "TransportationNetworks-master/General-Grid/General_Grid_trips.tntp";
            }

            @Override
            public Turtle getTurtle() throws ExtensionException, LogoException {
                return null;
            }

            @Override
            public Link getLink() throws ExtensionException, LogoException {
                return null;
            }

            @Override
            public AnonymousReporter getReporter() throws ExtensionException, LogoException {
                return null;
            }

            @Override
            public AnonymousCommand getCommand() throws ExtensionException, LogoException {
                return null;
            }

            @Override
            public List<Token> getCode() throws ExtensionException, LogoException {
                return null;
            }

            @Override
            public Token getSymbol() throws ExtensionException, LogoException {
                return null;
            }
        };
        arguments[1] = new Argument() {
            @Override
            public Object get() throws ExtensionException, LogoException {
                return null;
            }

            @Override
            public AgentSet getAgentSet() throws ExtensionException, LogoException {
                return null;
            }

            @Override
            public Agent getAgent() throws ExtensionException, LogoException {
                return null;
            }

            @Override
            public Boolean getBoolean() throws ExtensionException, LogoException {
                return null;
            }

            @Override
            public boolean getBooleanValue() throws ExtensionException, LogoException {
                return false;
            }

            @Override
            public int getIntValue() throws ExtensionException, LogoException {
                return 10;
            }

            @Override
            public double getDoubleValue() throws ExtensionException, LogoException {
                return 0;
            }

            @Override
            public LogoList getList() throws ExtensionException, LogoException {
                return null;
            }

            @Override
            public Patch getPatch() throws ExtensionException, LogoException {
                return null;
            }

            @Override
            public String getString() throws ExtensionException, LogoException {
                return null;
            }

            @Override
            public Turtle getTurtle() throws ExtensionException, LogoException {
                return null;
            }

            @Override
            public Link getLink() throws ExtensionException, LogoException {
                return null;
            }

            @Override
            public AnonymousReporter getReporter() throws ExtensionException, LogoException {
                return null;
            }

            @Override
            public AnonymousCommand getCommand() throws ExtensionException, LogoException {
                return null;
            }

            @Override
            public List<Token> getCode() throws ExtensionException, LogoException {
                return null;
            }

            @Override
            public Token getSymbol() throws ExtensionException, LogoException {
                return null;
            }
        };
        long start = System.currentTimeMillis();
        try {
            LogoList logoList = (LogoList) readOrigins.report(arguments, new Context() {
                @Override
                public Agent getAgent() {
                    return null;
                }

                @Override
                public World world() {
                    return null;
                }

                @Override
                public BufferedImage getDrawing() {
                    return null;
                }

                @Override
                public void importPcolors(BufferedImage image, boolean asNetLogoColors) {

                }

                @Override
                public String attachCurrentDirectory(String path) throws MalformedURLException {
                    return null;
                }

                @Override
                public MersenneTwisterFast getRNG() {
                    return null;
                }

                @Override
                public Activation activation() {
                    return null;
                }

                @Override
                public Workspace workspace() {
                    return null;
                }
            });
            System.out.println("Total Time:" + (System.currentTimeMillis() - start)/1000.0 + "seconds");
            System.out.println("Number of origins: " + logoList.size());
            System.out.println(logoList);
//            for (int i = 0; i < logoList.size(); i++) {
//
//                System.out.println(logoList.get(i));
//            }
        } catch (ExtensionException e) {
            e.printStackTrace();
        }
    }
}
