import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Render {

    private Field field;
    private GraphicsContext gc;
    private Bug selectedBug = null;

    private final double minBrightness = 0.4;
    private final double maxBrightness = 1.0;

    private int cellSize = 20;
    private double hueCoeficient;
    private double brightnessCoeficient;

    public Bug getSelectedBug() {
        return selectedBug;
    }

    public void setSelectedBug(Bug selectedBug) {
        this.selectedBug = selectedBug;
    }

    public int getCellSize() {
        return cellSize;
    }

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
        gc.fillRect(x*cellSize, y*cellSize, cellSize, cellSize);
        gc.strokeRect(x*cellSize, y*cellSize, cellSize, cellSize);
    }

    public void repaintField() {
        for (int i=0; i<field.getHeight(); i++) {
            for (int j=0; j<field.getWidth(); j++) {
                drawCell(i, j);
            }
        }
    }

    public void repaintBugs() {
        if (field.getBugs() != null) {
            for (Bug b: field.getBugs()) {
                if (b.equals(selectedBug))
                    gc.setFill(Color.BLACK);
                else
                    gc.setFill(Color.WHITE);
                gc.fillOval(b.getPosition().x*cellSize + cellSize*0.2, b.getPosition().y*cellSize + cellSize*0.2, cellSize*0.6, cellSize*0.6);
            }
        }
    }

}
