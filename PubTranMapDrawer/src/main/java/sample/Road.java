package sample;

import javafx.geometry.Point2D;

import java.awt.*;

public class Road {
    private Node initNode;
    private Node termNode;
    private double capacity;
    private int number;

    public Road(int number, Node initNode, Node termNode, double capacity) {
        setNumber(number);
        setInitNode(initNode);
        setTermNode(termNode);
        setCapacity(capacity);
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
}
