import java.util.Random;

public class Bug {

    private static int ID = 0;
    private Field field;
    private Position position;

    private final int id;
    private final double tolerance;
    private final double idealTemp;
    private final double heatingPower;
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
        return Math.abs( field.getH(position) - idealTemp ) / Field.MAXTEMP;
    }

    public double getHeatingPower() {
        return heatingPower;
    }

    public Position getPosition() {
        return position;
    }

    public void refresh() {

        double probabilityRandomMove = 0.1;
        if (rnd.nextDouble() < probabilityRandomMove) {
            randomMove();
        }
        else if (getUnhapiness() > tolerance) {
            directMove();
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
            if ( position.x + dx < field.getWidth() && position.x + dx >= 0) {
                position.x += dx;
            }
            if (position.y + dy < field.getHeight() && position.y + dy >= 0)
                position.y += dy;
        }
    }

    public int getId() {
        return id;
    }
}
