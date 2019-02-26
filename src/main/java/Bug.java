import java.util.Random;

public class Bug {

    private Field field;
    private Position position;

    private double tolerance;
    private double idealTemp;
    private double probabilityRandomMove = 0.5;
    private double heatingPower;
    Random rnd = new Random();

    public Bug(double tolerance, double idealTemp, double heatingPower) {
        this.tolerance = tolerance;
        this.idealTemp = idealTemp;
        this.heatingPower = heatingPower;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public double getUnhapiness() {
        return Math.abs( field.getH(position) - idealTemp ) / field.MAXTEMP;
    }

    public double getHeatingPower() {
        return heatingPower;
    }

    public Position getPosition() {
        return position;
    }

    public void refresh() {
        if (getUnhapiness() < tolerance)
            directMove();
        else if (rnd.nextDouble() < probabilityRandomMove)
            randomMove();
    }

    private void randomMove() {
        int dx = rnd.nextInt(3) - 1;
        int dy = rnd.nextInt(3) - 1;
        if ( position.x + dx < field.getWidth() && position.x + dx >= 0 &&
             position.y + dy < field.getHeight() && position.y + dy >= 0 &&
             !field.isTakeUp(new Position(position.x+dx, position.y+dy))   ) {
            position.x += dx;
            position.y += dy;
        }
    }

    private void directMove() {
    }

    public double getMaxIdealTemp() {
        double t = idealTemp + (field.MAXTEMP * tolerance) / 2;
        return (t < field.MAXTEMP) ? t : field.MAXTEMP;
    }

    public double getMinIdealTemp() {
        double t = idealTemp - (field.MAXTEMP * tolerance) / 2;
        return (t > 0) ? t : 0;
    }
}
