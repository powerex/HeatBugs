import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

public class Controller {

    private Field f;
    private Render render;

    public Canvas canvas;
    public Button button;
    public Button buttonStart;
    public Button buttonStop;
    public TextArea area;
    public Label labelStep;
    public Label averageTemperature;

    private Task<Void> task;

    @FXML
    private void initialize() {
    }

    private Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    draw();
                } catch (Exception ex) {
                    updateMessage(ex.getMessage());
                }
                return null;
            }

            private void draw() {
                long s = 0;
                updateMessage("Початок моделювання");
                while(true) {
                    ++s;
                    if (isCancelled()) {
                        updateMessage("Моделювання зупинено");
                        return;
                    }

                    Platform.runLater(new Runnable() {
                        long step = 0;

                        @Override
                        public void run() {
                            f.iterate();
                            render.repaintField();
                            render.repaintBugs();
                            updateMessage("Крок моделювання " + (step++));
                            averageTemperature.setText("Середня температура: " + String.format("%.2f",f.getAverageTemperatureEnvironment()));
                        }

                        Runnable step(long i) {
                            this.step = i;
                            return this;
                        }

                    }.step(s));

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException interrupted) {
                        if (isCancelled()) {
                            updateMessage("Моделювання завершено");
                            return;
                        }
                    }
                }
            }

            @Override
            protected void updateMessage(String message) {
                System.out.println(message);
                super.updateMessage(message);
            }
        };
    }

    public void startModeling(ActionEvent event) {
        if (task != null && task.isRunning()) {
            task.cancel();
        }

        task = createTask();
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        labelStep.textProperty().bind(task.messageProperty());
        buttonStart.visibleProperty().bind(task.runningProperty().not());
//        buttonStart.disableProperty()
        buttonStop.visibleProperty().bind(task.runningProperty());
//        averageTemperature.textProperty().bind("Середня температура: " + String.format("%.2f",(Integer)task.getValue()));
    }

    public void stopModeling(ActionEvent event) {
        if (task != null)
            task.cancel();
    }

    public void go(ActionEvent actionEvent) {

        f = new Field(40, 30, 0.99, 0.2);
        render = new Render(f, canvas);
        render.repaintField();

        Bug bug1 = new Bug(0.1, 10, 30);
        Bug bug2 = new Bug(0.1, 50, 30);
        Bug bug3 = new Bug(0.1, 50, 30);
        Bug bug4 = new Bug(0.1, 80, 0);
        Bug bug5 = new Bug(0.1, 5, 80);
        f.addBug(bug1, new Position(19, 18));
        f.addBug(bug2, new Position(1, 1));
        f.addBug(bug3, new Position(19, 1));
        f.addBug(bug4, new Position(1, 19));
        f.addBug(bug5, new Position(10, 10));

        render.repaintBugs();
        buttonStart.setVisible(true);

    }

    public void start(ActionEvent actionEvent) {
        startModeling(null);
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
