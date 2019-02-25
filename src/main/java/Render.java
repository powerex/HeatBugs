import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Render {

    private Field field;
    private GraphicsContext gc;

    private final double minBrightness = 0.4;
    private final double maxBrightness = 1.0;

    private int cellSize = 20;
    private double hueCoeficient;
    private double brightnessCoeficient;

    public Render(Field field, Canvas canvas) {
        this.field = field;
        this.gc = canvas.getGraphicsContext2D();

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(1);

        hueCoeficient = 210 / field.MAXTEMP;
        brightnessCoeficient = (maxBrightness - minBrightness) / field.MAXTEMP;

    }

    private void drawCell(int x, int y) {
        double t = field.getH(x, y);
        gc.setFill(Color.hsb(210 - hueCoeficient * t, 1.0, brightnessCoeficient * t + minBrightness));
        gc.fillRect((x-1)*cellSize, (y-1)*cellSize, cellSize, cellSize);
        gc.strokeRect((x-1)*cellSize, (y-1)*cellSize, cellSize, cellSize);
    }

    public void repaintField() {
        for (int i=1; i<=field.getHeight(); i++) {
            for (int j=1; j<=field.getWidth(); j++) {
                drawCell(i, j);
            }
        }
    }

}
