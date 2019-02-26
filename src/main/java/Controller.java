import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Controller {

    private Field f;
    private Render render;

    public Canvas canvas;
    public Button button;
    public Button step;
    public TextArea area;

    public void go(ActionEvent actionEvent) {

        f = new Field(20, 20, 0.95, 0.95);
        render = new Render(f, canvas);
        render.repaintField();

        Bug bug1 = new Bug(0.1, 50, 70);
        Bug bug2 = new Bug(0.1, 50, 70);
        Bug bug3 = new Bug(0.1, 10, 70);
        Bug bug4 = new Bug(0.1, 50, 70);
        Bug bug5 = new Bug(0.1, 80, 70);
        f.addBug(bug1, new Position(10, 8));
        f.addBug(bug2, new Position(10, 10));
        f.addBug(bug3, new Position(11, 9));
        f.addBug(bug4, new Position(9, 9));
        f.addBug(bug5, new Position(12, 8));

        render.repaintBugs();

        step.setVisible(true);

    }

    public void step(ActionEvent actionEvent) {
        f.iterate();
        step.setText(String.valueOf(f.getH(10,8)));
        render.repaintField();
        render.repaintBugs();
    }

    public void getInfo(MouseEvent mouseEvent) {
        int posX = (int)mouseEvent.getX() / render.getCellSize();
        int posY = (int)mouseEvent.getY() / render.getCellSize();
        if (f.isTakeUp(new Position(posX, posY))) {
            area.setText(String.valueOf(f.getBugAtPosition(new Position(posX, posY)).getUnhapiness()));
            render.setSelectedBug(f.getBugAtPosition(new Position(posX, posY)));
        }
        else
            area.setText("Empty cell");
    }
}
