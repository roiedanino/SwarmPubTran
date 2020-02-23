package sample;

import javafx.geometry.Point2D;

import java.awt.*;

public class Road {
    public final static Color ROAD_COLOR = new Color(102, 204, 80);
    public final static Color OFF_ROAD_COLOR = new Color(141, 141, 141);

    private Node initNode;
    private Node termNode;
    private double capacity;
    private int number;
    private int frequency;

    public Road(int number, Node initNode, Node termNode, double capacity) {
        setNumber(number);
        setInitNode(initNode);
        setTermNode(termNode);
        setCapacity(capacity);
        setFrequency(0);
    }

    public Road() {
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Node getInitNode() {
        return initNode;
    }

    public void setInitNode(Node initNode) {
        this.initNode = initNode;
    }

    public Node getTermNode() {
        return termNode;
    }

    public void setTermNode(Node termNode) {
        this.termNode = termNode;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public void draw(Graphics2D graphics2D){
        draw(graphics2D, false);
    }

    public void draw(Graphics2D graphics2D, boolean withRoadNumber){
        int xInit = initNode.getX();
        int yInit = initNode.getY();
        int xFinal = termNode.getX();
        int yFinal = termNode.getY();
        int frequency = this.frequency > 140 ? 140: this.frequency;
        Color roadColor = new Color(ROAD_COLOR.getRed() / 256f, ROAD_COLOR.getGreen() / 256f ,
                ROAD_COLOR.getBlue() / 256f,
                1f - frequency / 140f);
        System.out.println("Freq: " + frequency);
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawLine(xInit, yInit, xFinal, yFinal);
        graphics2D.setColor(roadColor);
//        graphics2D.setStroke(new BasicStroke(frequency));
        graphics2D.drawLine(xInit, yInit, xFinal, yFinal);

        if(withRoadNumber) {
            Point2D midPoint = termNode.getCoordinates()
                    .midpoint(initNode.getCoordinates());
            int midX = (int) midPoint.getX();
            int midY = (int) midPoint.getY();
            if (midY < 10) {
                midY += 20;
            }
            graphics2D.drawString(number + "", midX, midY);
        }
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void addFrequency(int frequency){
        this.frequency += frequency;
    }
}
