import java.util.Random;

public class Bug {

    private static int ID = 0;
    private Field field;
    private Position position;

    private int id;
    private double tolerance;
    private double idealTemp;
    private double probabilityRandomMove = 0.1;
    private double heatingPower;
    Random rnd = new Random();

    public Bug(double tolerance, double idealTemp, double heatingPower) {
        this.id = ++ID;
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

        if (rnd.nextDouble() < probabilityRandomMove) {
            randomMove();
//            System.out.println("Random move");
        }
        else if (getUnhapiness() > tolerance) {
            directMove();
//            System.out.println("Direct move");
        }
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
        Position goalPosition = field.getNearestIdealPoint(idealTemp, position);
        if (goalPosition != null) {
            if (id == 1) System.out.println(goalPosition);
            int dx = position.getDx(goalPosition);
            int dy = position.getDy(goalPosition);
            int tempDx = dx;

            if (field.isTakeUp(new Position(position.x+dx, position.y+dy))) {
                dx = 0;
                if (field.isTakeUp(new Position(position.x + dx, position.y + dy))) {
                    dx = tempDx;
                    dy = 0;
                    if (field.isTakeUp(new Position(position.x + dx, position.y + dy))) {
                        dx = 0;
                    }
                }
            }
            position.x += dx;
            position.y += dy;
        }
    }

    public double getMaxIdealTemp() {
        double t = idealTemp + (field.MAXTEMP * tolerance) / 2;
        return (t < field.MAXTEMP) ? t : field.MAXTEMP;
    }

    public double getMinIdealTemp() {
        double t = idealTemp - (field.MAXTEMP * tolerance) / 2;
        return (t > 0) ? t : 0;
    }

    public int getId() {
        return id;
    }
}
