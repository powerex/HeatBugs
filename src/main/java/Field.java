import java.util.LinkedList;
import java.util.List;

public class Field {

    private static final double EPS = 1e-5;
    private double[][] t;
    private List<Bug> bugs;
    private final int width;
    private final int height;
    private double intensityEvaporate;
    private double spreadRate;

    public Field(int width, int height, double intensityEvaporate, double spreadRate) {
        MAXTEMP = 100;

        this.width = width;
        this.height = height;
        this.intensityEvaporate = intensityEvaporate;
        this.spreadRate = spreadRate;

        t = new double[height][width];

        for (int h=0; h<height; h++) {
            for (int w=0; w<width; w++) {
                t[h][w] = ((double)h+w) / (height + width) * MAXTEMP;
            }
        }
    }

    public void addBug(Bug bug, Position position) {
        if (bugs == null) {
            bugs = new LinkedList<Bug>();
        }
        bugs.add(bug);
        bug.setField(this);
        bug.setPosition(position);

    }

    public List<Bug> getBugs() {
        return bugs;
    }

    public static double MAXTEMP;

    public double getH(Position position) {
        return t[position.height][position.width];
    }

    public double getH(int width, int height) {
        return t[height][width];
    }

    public double getAverageTemperatureEnvironment() {
        double sum = 0.0;
        for (int h=0; h<height; ++h) {
            for (int w=0; w<width; w++) {
                sum += t[h][w];
            }
        }
        return sum / (height*width);
    }

    private double getAverageTemp(int h, int w) {

        double sum = .0;
        int c = 0;

        if ( w>1 ) {
            if ( h>1 ) {
                sum += t[h - 1][w - 1];
                c++;
            }
            if ( h < height-1) {
                sum += t[h + 1][w - 1];
                c++;
            }
            sum += t[h][w-1];
            c++;
        }

        if ( h>1 ) {
            sum += t[h - 1][w];
            c++;
        }
        if ( h < height-1) {
            sum += t[h + 1][w];
            c++;
        }

        if ( w < width-1 ) {
            if ( h>1 ) {
                sum += t[h - 1][w + 1];
                c++;
            }
            if ( h < height-1) {
                sum += t[h + 1][w + 1];
                c++;
            }
            sum += t[h][w + 1];
            c++;
        }

        return sum / c;
    }

    public void iterate() {
        if (bugs != null) {

                for (Bug b : bugs) {
                    try {
                        t[b.getPosition().height][b.getPosition().width] += b.getHeatingPower();
                    }
                    catch (ArrayIndexOutOfBoundsException e) {
                        System.err.println("X: " + b.getPosition().width);
                        System.err.println("Y: " + b.getPosition().height);
                    }
                }
        }

        double tt[][] =  new double[height][width];

        for (int h=0; h<height; h++) {
            for (int w=0; w<width; w++) {
                tt[h][w] = intensityEvaporate * ( t[h][w] +  spreadRate * ( getAverageTemp(h, w) - t[h][w] ));
                if (tt[h][w] > MAXTEMP)
                    tt[h][w] = MAXTEMP;
                if (tt[h][w] < 0)
                    tt[h][w] = 0;
            }
        }

        t = tt;

        if (bugs != null)
        for (Bug b: bugs) {
            b.refresh();
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getTempPosition(int x, int y) {
        return t[y][x];
    }

    public synchronized boolean isTakeUp(Position position) {
        for (Bug bug: bugs) {
            if (bug.getPosition().equals(position))
                return true;
        }
        return false;
    }

    public Bug getBugAtPosition(Position position) {
        for (Bug bug: bugs) {
            if (bug.getPosition().equals(position))
                return bug;
        }
        return null;
    }

    public Position getNearestIdealPoint(double idealTemperature, Position currentPoint) {
        List<Position> nearestPointList = new LinkedList<Position>();
        double minDifference = Double.MAX_VALUE;

        for (int h=0; h<height; h++) {
            for (int w=0; w<width; w++) {
                double difference = Math.abs(t[h][w] - idealTemperature);
                if (difference < minDifference) {
                    minDifference = difference;
                    nearestPointList.clear();
                    nearestPointList.add(new Position(w, h));
                }
                else if (Math.abs(difference - minDifference) < EPS) {
                    nearestPointList.add(new Position(w, h));
                }
            }
        }

        if (nearestPointList.size() == 0)
            return null;
        if (nearestPointList.size() == 1)
            return nearestPointList.get(0);

        Position nearestPoint = nearestPointList.get(0);
        double distance = Double.MAX_VALUE;
        for (Position point: nearestPointList) {
            double d = Math.pow((currentPoint.width - point.width),2) + Math.pow((nearestPoint.height - point.height),2);
            if (d < distance) {
                distance = d;
                nearestPoint = point;
            }

        }
        return nearestPoint;
    }

    public void setIntensityEvaporate(double intensityEvaporate) {
        this.intensityEvaporate = intensityEvaporate;
    }

    public void setSpreadRate(double spreadRate) {
        this.spreadRate = spreadRate;
    }
}
