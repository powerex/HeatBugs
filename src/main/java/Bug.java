import javafx.beans.property.DoubleProperty;

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

    public Double getUnhappiness() {
        return Math.abs( field.getH(position) - idealTemp ) / Field.MAXTEMP;
    }

    public double getHeatingPower() {
        return heatingPower;
    }

    public Position getPosition() {
        return position;
    }

    public void refresh() {

        double probabilityRandomMove = 0.0;
        if (rnd.nextDouble() < probabilityRandomMove) {
            randomMove();
        }
        else if (getUnhappiness() > tolerance) {
            directMove();
        }
    }

    private void randomMove() {
        System.out.print("Random move from " + position);
        int dx = rnd.nextInt(3) - 1;
        int dy = rnd.nextInt(3) - 1;
        if ( position.width + dx < field.getWidth() && position.width + dx >= 0 &&
             position.height + dy < field.getHeight() && position.height + dy >= 0 &&
             !field.isTakeUp(new Position(position.width +dx, position.height +dy))   ) {
            position.width += dx;
            position.height += dy;
            System.out.println(position + " at " + field.getHeight());
        }

        System.out.println(" to " + position);
    }

    private void directMove() {
//        System.out.print("Direct move from " + position);
        Position goalPosition = field.getNearestIdealPoint(idealTemp, position);
        //System.out.println("Goal position " + goalPosition + " with temp " + field.getH(goalPosition));
        if (goalPosition != null) {
            int dx = position.getDx(goalPosition);
            int dy = position.getDy(goalPosition);
            int tempDx = dx;

            if (field.isTakeUp(new Position(position.width +dx, position.height +dy))) {
                dx = 0;
                if (field.isTakeUp(new Position(position.width + dx, position.height + dy))) {
                    dx = tempDx;
                    dy = 0;
                    if (field.isTakeUp(new Position(position.width + dx, position.height + dy))) {
                        dx = 0;
                    }
                }
            }
            if ( position.width + dx < field.getWidth() && position.width + dx >= 0) {
                position.width += dx;
            }
            if (position.height + dy < field.getHeight() && position.height + dy >= 0)
                position.height += dy;
        }
//        System.out.println(" to " + position);
    }

    public int getId() {
        return id;
    }

    public static void resetId() {
        ID = 0;
    }

    public double getIdealTemp() {
        return idealTemp;
    }

    public double getCurrentTemp() {
        return field.getH(position);
    }
}
