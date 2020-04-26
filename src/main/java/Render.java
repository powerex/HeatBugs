import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Render {

    private final Field field;
    private final GraphicsContext gc;
    private Bug selectedBug = null;

    private final double minBrightness = 0.4;

    private final int cellSize = 30;
    private final double hueCoeficient;
    private final double brightnessCoeficient;

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
        gc.setFont(new Font(12));

        hueCoeficient = 210 / Field.MAXTEMP;
        double maxBrightness = 1.0;
        brightnessCoeficient = (maxBrightness - minBrightness) / Field.MAXTEMP;

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
                if (b.equals(selectedBug)) {
                    gc.setFill(Color.BLACK);
                    gc.setStroke(Color.WHITE);
                }
                else {
                    gc.setFill(Color.WHITE);
                    gc.setStroke(Color.BLACK);
                }
                gc.fillOval(b.getPosition().x*cellSize + cellSize*0.2, b.getPosition().y*cellSize + cellSize*0.2, cellSize*0.6, cellSize*0.6);
                gc.strokeText(String.valueOf(b.getId()),b.getPosition().x*cellSize + cellSize*0.37, b.getPosition().y*cellSize + cellSize*0.65);
            }
        }
        gc.setStroke(Color.WHITE);
    }

}
