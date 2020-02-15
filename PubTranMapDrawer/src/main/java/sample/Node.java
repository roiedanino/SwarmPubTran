package sample;


import javafx.geometry.Point2D;

public class Node {
    private int number;
    private Point2D coordinates;

    public Node(int number, Point2D coordinates) {
        setNumber(number);
        setCoordinates(coordinates);
    }

    public Node() {
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Point2D getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Point2D coordinates) {
        this.coordinates = coordinates;
    }

    public int getX(){
        return (int) coordinates.getX();
    }

    public int getY(){
        return (int) coordinates.getY();
    }

}
