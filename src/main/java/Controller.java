import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.RotateEvent;
import sun.awt.image.ImageWatched;

import java.util.LinkedList;
import java.util.List;

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

    public Label eLabel;
    public Slider eSlider;
    public Label kLabel;
    public Slider kSlider;

    public TableView tableStat;
    public TableColumn<String, BugDecoder> idColumn;
    public TableColumn<Double, Bug> tempColumn;
    public TableColumn<Double, Bug> idealTempColumn;
    public TableColumn<Bug, Double> happyColumn;

    private Task<Void> task;
    private Thread modeling;

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idealTempColumn.setCellValueFactory(new PropertyValueFactory<>("idealTemp"));
        happyColumn.setCellValueFactory(new PropertyValueFactory<>("happyColumn"));
        tempColumn.setCellValueFactory(new PropertyValueFactory<>("tempColumn"));


        eSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double e = newValue.intValue() / 100.0;
            f.setIntensityEvaporate(e);
            eLabel.setText("Коефіцієнт випаровування, е: " + String.format("%.2f",e));

        });

        kSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double k = newValue.intValue() / 100.0;
            f.setSpreadRate(k);
            kLabel.setText("Коефіцієнт розповсюдження, k: " + String.format("%.2f",k));

        });
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
                            tableStat.refresh();
                        }

                        Runnable step(long i) {
                            this.step = i;
                            return this;
                        }

                    }.step(s));

                    try {
                        Thread.sleep(150);
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

    public void startModeling() {
        if (task != null && task.isRunning()) {
            task.cancel();
        }

        task = createTask();
        modeling = new Thread(task);
        modeling.setDaemon(true);
        modeling.start();

        labelStep.textProperty().bind(task.messageProperty());
        buttonStart.disableProperty().bind(task.runningProperty());
        buttonStop.disableProperty().bind(task.runningProperty().not());
    }

    public void stopModeling() {
        if (task != null)
            task.cancel();
    }

    public void go() {

        Bug.resetId();
        f = new Field(32, 24, 0.95, 0.21);
        render = new Render(f, canvas);
        render.repaintField();
        buttonStart.setDisable(false);

        double tolerance = 0.2;
        Bug bug1 = new Bug(tolerance, 99, 110);
        Bug bug2 = new Bug(tolerance, 99, 10);
        Bug bug3 = new Bug(tolerance, 50, 70);
        Bug bug4 = new Bug(tolerance, 79, 40);
        Bug bug5 = new Bug(tolerance, 15, 100);
        Bug bug6 = new Bug(tolerance, 70, 100);
        Bug bug7 = new Bug(tolerance, 40, 70);
        Bug bug8 = new Bug(tolerance, 0, 1);
        Bug bug9 = new Bug(tolerance, 80, -50);
        f.addBug(bug1, new Position(6, 5));
        f.addBug(bug2, new Position(5, 5));
        f.addBug(bug3, new Position(19, 1));
        f.addBug(bug4, new Position(1, 19));
        f.addBug(bug5, new Position(10, 10));
        f.addBug(bug6, new Position(12, 10));
        f.addBug(bug7, new Position(2, 15));
        f.addBug(bug8, new Position(3, 15));
        f.addBug(bug9, new Position(5, 18));

        List<BugDecoder> bugDecoderList = new LinkedList<>();
        for (Bug bug: f.getBugs())
            bugDecoderList.add(new BugDecoder(bug));

        tableStat.getItems().addAll(bugDecoderList);

        render.repaintBugs();
        buttonStart.setVisible(true);

    }

    public void start() {
        startModeling();
    }

    public void getInfo(MouseEvent mouseEvent) {
        int posX = (int)mouseEvent.getX() / render.getCellSize();
        int posY = (int)mouseEvent.getY() / render.getCellSize();
        if (f.isTakeUp(new Position(posX, posY))) {
            area.setText(String.valueOf(f.getBugAtPosition(new Position(posX, posY)).getUnhappiness()));
            render.setSelectedBug(f.getBugAtPosition(new Position(posX, posY)));
        }
        else
            area.setText("Empty cell");
    }
}
