package sample;

import java.awt.*;
import java.util.List;

public class BusLine {
    private int lineNumber;
    private List<Road> roads;
    private int freqPerDay;

    public BusLine(int lineNumber, List<Road> roads, int freqPerDay) {
        setLineNumber(lineNumber);
        setRoads(roads);
        setFreqPerDay(freqPerDay);
    }

    public BusLine() {
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public List<Road> getRoads() {
        return roads;
    }

    public void setRoads(List<Road> roads) {
        this.roads = roads;
    }

    public int getFreqPerDay() {
        return freqPerDay;
    }

    public void setFreqPerDay(int freqPerDay) {
        this.freqPerDay = freqPerDay;
    }

    public void draw(Graphics2D graphics2D, int size, int enumeration){
        for (Road road: roads){
            Node initPoint = road.getInitNode();
            Node termPoint = road.getTermNode();

            int xInit = initPoint.getX();
            int yInit = initPoint.getY();
            int xFinal = termPoint.getX();
            int yFinal = termPoint.getY();

            if(yFinal < 10){
                yFinal += 10;
                yInit += 10;
            }
            else if (size - yFinal < 10){
                yFinal -= 10;
                yInit -= 10;
            }

//            graphics2D.setStroke(new BasicStroke(road.getFrequency() / 15));

            graphics2D.drawString(lineNumber + "",  size - 40,
                    30 + 30 * (enumeration + 10));

            graphics2D.drawLine(xInit + enumeration * 3, yInit + enumeration,
                            xFinal + enumeration * 3, yFinal + enumeration);

        }
    }

    @Override
    public String toString() {
        return "BusLine{" +
                "lineNumber=" + lineNumber +
                ", roads=" + roads +
                ", freqPerDay=" + freqPerDay +
                '}';
    }
}
