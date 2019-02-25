public class Bug {

    private Field field;
    private Position position;

    private double tolerance;
    private double idealTemp;
    private double probabilityRandomMove;
    private double heatingPower;

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
        getUnhapiness();
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
