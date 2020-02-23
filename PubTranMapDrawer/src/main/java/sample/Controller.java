package sample;

import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
import java.util.List;
import java.util.*;

public class Controller implements Initializable {
    private final static Color ROAD_COLOR = Color.rgb(102, 204, 80);
    private final static Color OFF_ROAD_COLOR = Color.rgb(141, 141, 141);

    private final static String PATH_TO_DATASET = "/Users/roie/Desktop/Research Assistance/Data Sets/TransportationNetworks-master";


    @FXML
    private TextField sizeInput;


    @FXML
    private BorderPane borderPane;

    @FXML
    private ImageView imageView;

    @FXML
    private CheckBox flipVertically;

    @FXML
    private CheckBox enumerateNodes;

    @FXML
    private CheckBox enumerateRoads;

    @FXML
    private CheckBox colorBusLines;

    @FXML
    private ComboBox<File> cityDataCombo;

    @FXML
    private Button coordsButton;


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

    private int handleSize(){
        int size;
        try {
            size = Integer.parseInt(sizeInput.getText().trim());
        }catch (Exception ex){
            int WIDTH = 1000;
            size = WIDTH;
        }
        return size;
    }
    @FXML
    public void onCoordsButtonClicked() throws FileNotFoundException {
        String[] paths = handleInputDirectory();
        if(paths == null)
            return;
        String dirPath = paths[0];
        String pathToCoords = paths[1];
        String pathToRoads = paths[2];
        int size = handleSize();

        WritableImage writableImage = new WritableImage(size, size);
        List<Node> nodes = loadCoordinates(pathToCoords, size, dirPath);
        drawMap(writableImage.getPixelWriter(), nodes, size);
        List<Road> roads = RoadsReader.loadRoads(pathToRoads, nodes);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
        Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();
        if(enumerateNodes.isSelected())
            drawCoordsNumbers(graphics2D, nodes);

        List<BusLine> busLines = RoadPopularityConverter.readBusLines(roads);
        drawRoads(graphics2D,roads);
        if(colorBusLines.isSelected())
            drawLines(graphics2D, busLines, size);
        writableImage = SwingFXUtils.toFXImage(bufferedImage, null);
        imageView.setImage(writableImage);

        borderPane.setCenter(imageView);
        File outputFile = new File(dirPath + "/" + "map-" + size + "x" + size + ".png");
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

    private List<Node> loadCoordinates(String path, int size, String dirPath)
                                                            throws FileNotFoundException {
        ArrayList<Point2D> coords = new ArrayList<>();
        Scanner scanner = new Scanner(new File(path));
        scanner.nextLine();

        while(scanner.hasNextLine()) {
            scanner.nextInt();
            double x = scanner.nextDouble();
            double y = scanner.nextDouble();
            if(flipVertically.isSelected()){
                y = size - y + 1;
            }
            scanner.nextLine();
            coords.add(new Point2D(x, y));
        }

        Point2D minCoor = findMinCoor(coords);
        coords = subtractMinPoint(coords, minCoor);
        double fix = findOptimalFixForSize(size, coords);
        coords = fixCoordinates(fix, coords);
        writeFixedCoords(coords, dirPath, size, fix);
        return mapCoordsToNodes(coords);
    }

    private List<Node> mapCoordsToNodes(List<Point2D> coords){
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < coords.size(); i++) {
            nodes.add(new Node(i + 1, coords.get(i)));
        }
        return nodes;
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

    private java.awt.Color randomColor(){
        return new java.awt.Color((int)(255 * Math.random()), (int)(255 * Math.random()),
                (int)(255 * Math.random()));
    }

    private void drawLines(Graphics2D graphics2D, List<BusLine> busLines, int size){
        final int LINE_WIDTH = 10;
        graphics2D.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 25));
        int i = -6;
        for(BusLine busLine : busLines){
//                graphics2D.setColor(randomColor());
            graphics2D.setStroke(new BasicStroke(LINE_WIDTH));
            busLine.draw(graphics2D, size, i++);
        }

    }

    private void drawRoads(Graphics2D graphics2D, List<Road> roads){
        graphics2D.setColor(new java.awt.Color(102, 204, 80));
        graphics2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN,25));
        for (Road road : roads) {
            road.draw(graphics2D, enumerateRoads.isSelected());
        }
    }

    private void drawCoordsNumbers(Graphics2D graphics2D, List<Node> nodes) {
        graphics2D.setColor(new java.awt.Color(62, 96, 193));

        for (Node node: nodes){
            int y = node.getY();
            if(y < 10){
                y += 10;
            }
           graphics2D.drawString(node.getNumber() + "", node.getX(), y);
        }
    }

    private void drawMap(PixelWriter pixelWriter, List<Node> nodes, int size){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                pixelWriter.setColor(i, j, OFF_ROAD_COLOR);
            }
        }

        for (Node node: nodes){
            int x = node.getX();
            int y = node.getY();
            pixelWriter.setColor(x, y, ROAD_COLOR);
        }
    }



}
