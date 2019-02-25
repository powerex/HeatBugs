import java.util.List;

public class Field {

    private double[][] t;
    private List<Bug> bugs;
    private int width;
    private int height;
    private double intensityEvaporate;
    private double spreadRate;

    public Field(int width, int height, double intensityEvaporate, double spreadRate) {
        MAXTEMP = 100;

        this.width = width;
        this.height = height;
        this.intensityEvaporate = intensityEvaporate;
        this.spreadRate = spreadRate;

        t = new double[height+2][width+2];

        /*for (int i=0; i<height+2; i++) {
            for (int j=0; j<width+2; j++) {
                t[i][j] = ((double)i+j) / (width + height) * MAXTEMP;
            }
        }*/
    }

    public static double MAXTEMP;

    public double getH(Position position) {
        return t[position.x][position.y];
    }

    public double getH(int x, int y) {
        return t[x][y];
    }

    private double getAverageTemp(int i, int j) {
        return
        ( t[i-1][j-1]
        + t[i][j-1]
        + t[i+1][j-1]
        + t[i-1][j]
        + t[i+1][j]
        + t[i-1][j+1]
        + t[i][j+1]
        + t[i+1][j+1] ) / 8;
    }

    public void iterate() {
        for (Bug b: bugs) {
            t[b.getPosition().x][b.getPosition().y] += b.getHeatingPower();
        }

        for (int i=1; i<=height; i++) {
            for (int j=1; j<=width; j++) {
                t[i][j] = intensityEvaporate * ( t[i][j] *  spreadRate * ( getAverageTemp(i, j) - t[i][j] ));
            }
        }

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
}
