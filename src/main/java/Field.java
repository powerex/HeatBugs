import java.util.LinkedList;
import java.util.List;

public class Field {

    private static final double EPS = 1e-5;
    private double[][] t;
    private List<Bug> bugs;
    private int width;
    private int height;
    private double intensityEvaporate;
    private double spreadRate;

    public Field(int width, int height, double intensityEvaporate, double spreadRate) {
        MAXTEMP = 100;

        this.width = height;
        this.height = width;
        this.intensityEvaporate = intensityEvaporate;
        this.spreadRate = spreadRate;

        t = new double[width][height];

        for (int i=0; i<width; i++) {
            for (int j=0; j<height; j++) {
                t[i][j] = ((double)i+j) / (height + width) * MAXTEMP;
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
        return t[position.x][position.y];
    }

    public double getH(int x, int y) {
        return t[x][y];
    }

    private double getAverageTemp(int i, int j) {

        double sum = .0;
        int c = 0;

        if ( j>1 ) {
            if ( i>1 ) {
                sum += t[i - 1][j - 1];
                c++;
            }
            if ( i < height-1) {
                sum += t[i + 1][j - 1];
                c++;
            }
            sum += t[i][j-1];
            c++;
        }

        if ( i>1 ) {
            sum += t[i - 1][j];
            c++;
        }
        if ( i < height-1) {
            sum += t[i + 1][j];
            c++;
        }

        if ( j < width-1 ) {
            if ( i>1 ) {
                sum += t[i - 1][j + 1];
                c++;
            }
            if ( i < height-1) {
                sum += t[i + 1][j + 1];
                c++;
            }
            sum += t[i][j + 1];
            c++;
        }

        return sum / c;
    }

    public void iterate() {
        if (bugs != null)
        for (Bug b: bugs) {
            t[b.getPosition().x][b.getPosition().y] += b.getHeatingPower();
        }

        double tt[][] =  new double[height][width];

        for (int i=0; i<height; i++) {
            for (int j=0; j<width; j++) {
                tt[i][j] = intensityEvaporate * ( t[i][j] +  spreadRate * ( getAverageTemp(i, j) - t[i][j] ));
                if (tt[i][j] > MAXTEMP)
                    tt[i][j] = MAXTEMP;
                if (tt[i][j] < 0)
                    tt[i][j] = 0;
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
        return t[x][y];
    }

    public boolean isTakeUp(Position position) {
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

        for (int j=0; j<width; j++) {
            for (int i=0; i<height; i++) {
                double difference = Math.abs(t[i][j] - idealTemperature);
                if (difference < minDifference) {
                    minDifference = difference;
                    nearestPointList.clear();
                    nearestPointList.add(new Position(i,j));
                }
                else if (Math.abs(difference - minDifference) < EPS) {
                    nearestPointList.add(new Position(i,j));
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
            double d = Math.pow((currentPoint.x - point.x),2) + Math.pow((nearestPoint.y - point.y),2);
            if (d < distance) {
                distance = d;
                nearestPoint = point;
            }

        }
//        System.out.println("("+nearestPoint.x+";"+nearestPoint.y+")");
        return nearestPoint;
    }
}
