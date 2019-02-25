import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class Controller {

    public Canvas canvas;

    public Button button;

    public void go(ActionEvent actionEvent) {

        Field f = new Field(20, 20, 0.1, 0.25);
        Render render = new Render(f, canvas);
        render.repaintField();

    }
}
