package sample;

import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Controller implements Initializable {

    private final int WIDTH = 1000;
    private final int HEIGHT = 1000;

    private final int FIXED = 350;

    private final static Color ROAD_COLOR = Color.rgb(102, 204, 80);
    private final static Color OFF_ROAD_COLOR = Color.rgb(141, 141, 141);

    private final static String PATH_TO_DATASET = "/Users/roie/Desktop/Research Assistance/Data Sets/TransportationNetworks-master";

    private BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

    @FXML
    private TextField sizeInput;

    @FXML
    private Button coordsButton;

    @FXML
    private TextField fixedInput;

    @FXML
    private BorderPane borderPane;

    @FXML
    private ImageView imageView;


    @FXML
    private ComboBox<File> cityDataCombo;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cityDataCombo.setItems(FXCollections.observableArrayList(new File(PATH_TO_DATASET).listFiles()));
        cityDataCombo.setConverter(new StringConverter<File>() {
            @Override
            public String toString(File file) {
                return file.getName();
            }

            @Override
            public File fromString(String fileName) {
                return new File(PATH_TO_DATASET + "/" + fileName);
            }
        });
    }


    private String[] handleInputDirectory(){
        String dirPath = cityDataCombo.getValue().getAbsolutePath();
        File[] coordsFiles = new File(dirPath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().toLowerCase().endsWith("node.tntp");
            }
        });

        if(coordsFiles == null || coordsFiles.length < 1){
            new Alert(Alert.AlertType.ERROR, "No node.tntp file was found").showAndWait();
            return null;
        }

        File[] roadsFiles = new File(cityDataCombo.getValue().getAbsolutePath()).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().toLowerCase().endsWith("net.tntp");
            }
        });

        if(roadsFiles == null || roadsFiles.length < 1){
            new Alert(Alert.AlertType.ERROR, "No net.tntp file was found").showAndWait();
            return null;
        }

        return new String[]{dirPath, coordsFiles[0].getAbsolutePath(), roadsFiles[0].getAbsolutePath()};
    }



    @FXML
    public void onCoordsButtonClicked() throws FileNotFoundException {
        String[] paths = handleInputDirectory();
        if(paths == null)
            return;

        String dirPath = paths[0];
        String pathToCoords = paths[1];
        String pathToRoads = paths[2];

        int size;
        double fix;
        Point2D minCoor;

        try {
            size = Integer.parseInt(sizeInput.getText().trim());
        }catch (Exception ex){
            size = WIDTH;
        }

        try {
            fix = Integer.parseInt(fixedInput.getText().trim());
        }catch (Exception ex){
            fix = FIXED;
        }

        WritableImage writableImage = new WritableImage(size, size);
        ArrayList<Point2D> coords = loadCoordinates(pathToCoords);
        minCoor = findMinCoor(coords);
        coords = subtractMinPoint(coords, minCoor);
        fix = findOptimalFixForSize(size, coords);
        coords = fixCoordinates(fix, coords);

        List<Map<String, Double>> roads = RoadsReader.readFrom(pathToRoads);
        writeFixedCoords(coords, dirPath, size, fix);
        drawMap(writableImage.getPixelWriter(), coords, size);

        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
        drawRoads((Graphics2D) bufferedImage.getGraphics(), coords, roads);

        writableImage = SwingFXUtils.toFXImage(bufferedImage, null);
        imageView.setImage(writableImage);

        borderPane.setCenter(imageView);
        File outputFile = new File(dirPath + "/" + "map-" + size + "x" + size + "x" + fix +".png");
        try {
            ImageIO.write(bufferedImage, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Point2D findMinCoor(ArrayList<Point2D> coords) {

        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;

        for (Point2D coord: coords) {
            if(coord.getX() < minX){
                minX = coord.getX();
            }
            if(coord.getY() < minY){
                minY = coord.getY();
            }
        }

        return new Point2D(minX, minY);
    }


    private void writeFixedCoords(ArrayList<Point2D> fixedCoords, String dirPath, int size, double fix){
        try(PrintWriter pw = new PrintWriter(new File(dirPath + "/FixedCoords-"+ size + "x" + size +"x" + fix+".tntp"))){
            pw.println("Node \tX \tY \t;");
            for (int i = 0; i < fixedCoords.size(); i++) {
                Point2D point2D = fixedCoords.get(i);
                pw.printf("%d\t%d\t%d\t;\n", i + 1, (int)point2D.getX(), (int)point2D.getY());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private double findOptimalFixForSize(int size, ArrayList<Point2D> coords){
        double max = 0;

        for (Point2D point : coords){
           max = Double.max(point.getX(), max);
           max = Double.max(point.getY(), max);
        }

        double fix = ((size - 1) / max);

        System.out.println(" Max: " + max +  " Size: " + size + " Fix: " + fix);
        return fix;
    }

    private ArrayList<Point2D> loadCoordinates(String path) throws FileNotFoundException {
        ArrayList<Point2D> coords = new ArrayList<>();
        Scanner scanner = new Scanner(new File(path));
        scanner.nextLine();

        while(scanner.hasNextLine()) {
            scanner.nextInt();
            double x = scanner.nextDouble();
            double y = scanner.nextDouble();
            scanner.nextLine();
            coords.add(new Point2D(x, y));
        }

        return coords;
    }

    private ArrayList<Point2D> subtractMinPoint(ArrayList<Point2D> coords, Point2D minPoint){
        ArrayList<Point2D> subCoords = new ArrayList<>();
        for (Point2D coord: coords){
            subCoords.add(coord.subtract(minPoint));
        }
        return subCoords;
    }
    private ArrayList<Point2D> fixCoordinates(double fix, ArrayList<Point2D> coords){
        ArrayList<Point2D> fixedCoords = new ArrayList<>();
        for (Point2D point : coords) {
            fixedCoords.add(point.multiply(fix));
        }
        return fixedCoords;
    }

    private void drawRoads(Graphics2D graphics2D,  ArrayList<Point2D> coords, List<Map<String, Double>> roads){
        graphics2D.setColor(new java.awt.Color(102, 204, 80));
        for (Map<String, Double> road : roads) {
            int initNode = road.get(RoadsReader.K_INIT_NODE).intValue();
            int termNode = road.get(RoadsReader.K_TERM_NODE).intValue();
            Point2D initPoint = coords.get(initNode - 1);
            Point2D termPoint = coords.get(termNode - 1);

            int xInit = (int)initPoint.getX();
            int yInit = (int)initPoint.getY();
            int xFinal = (int)termPoint.getX();
            int yFinal = (int)termPoint.getY();
            graphics2D.drawLine(xInit, yInit, xFinal, yFinal);
        }
    }

    private void drawMap(PixelWriter pixelWriter, ArrayList<Point2D> coords, int size){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                pixelWriter.setColor(i, j, OFF_ROAD_COLOR);
            }
        }

        for (Point2D point: coords){
            int x = (int) point.getX();
            int y = (int) point.getY();
            pixelWriter.setColor(x, y, ROAD_COLOR);
        }
    }



}
