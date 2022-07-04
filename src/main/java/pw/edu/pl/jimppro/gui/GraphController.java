package pw.edu.pl.jimppro.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import pw.edu.pl.jimppro.graph.Graph;
import pw.edu.pl.jimppro.graph.GraphGenerator;
import pw.edu.pl.jimppro.graph.Path;
import pw.edu.pl.jimppro.graph.Vertex;

import java.io.File;
import java.io.IOException;

public class GraphController {
    private Graph graph = new Graph();
    private Logger log = Logger.getInstance();
    public static ObservableList<Path> data = FXCollections.observableArrayList();
    public static double[] offsets;
    public static double[] offsetsRight;
    public static double[] offsetsLeft;
    public static double[] offsetsDown;
    public static double[] offsetsUp;

    @FXML
    private Canvas canvas;

    @FXML
    private Logger logger = Logger.getInstance();

    @FXML
    private TextField openPath;

    @FXML
    private TextField fromVertex;

    @FXML
    private TextField toVertex;

    @FXML
    private TextField rowsField;

    @FXML
    private TextField columnsField;

    @FXML
    private TextField lowerLimitField;

    @FXML
    private TextField upperLimitField;

    @FXML
    private RadioButton allEdgesMode;

    @FXML
    private RadioButton connectedMode;

    @FXML
    private RadioButton randomMode;

    @FXML
    private TextField saveToPath;

    @FXML
    private TableView<Path> pathTable;

    @FXML
    private TableColumn<Path, String> showPathColumn;

    @FXML
    private TableColumn<Path, String> whichPathColumn;

    @FXML
    private TableColumn<Path, String> removePathColumn;

    @FXML
    private TableColumn<Path, String> showWeightsColumn;

    @FXML
    private TableColumn<Path, String> doNotShowWeightsColumn;

    public void handleOpenPath(ActionEvent event) {
        log.clear();
        log.setFlag(1);
        FileChooser fc = new FileChooser();
        String curDirectory = System.getProperty("user.dir");
        fc.setInitialDirectory(new File(curDirectory));
        File file = fc.showOpenDialog(null);
        if (file != null) {
            openPath.setText(file.getPath());
        } else {
            System.out.println("File is not valid!");
            return;
        }
        try {
            graph.read(openPath.getText());
            logger.setMessage(log.getText(), log.getFlag());
            if (log.getFlag() == 0) {
                log.setFlag(-1);
                return;
            }
            graph.display(canvas);
            createOffsets();
            logger.setMessage(log.getText() + "Graph was open successfully from file: " + file.getName(), log.getFlag());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void handleFindPath(ActionEvent event) {
        if (graph.getSize() == 0) {
            logger.setMessage("Firstly, generate or open the graph!", 0);
            return;
        }
        Vertex from, to;
        try {
            from = new Vertex(Integer.parseInt(fromVertex.getText()));
            to = new Vertex(Integer.parseInt(toVertex.getText()));
        } catch (NumberFormatException e) {
            logger.setMessage("Number of the Vertex must be a number!", 0);
            return;
        }
        if (from.getNumber() < 0 || to.getNumber() < 0 || from.getNumber() >= graph.getSize() || to.getNumber() >= graph.getSize()) {
            logger.setMessage("Number of Vertex must be positive number and less then number of all vertices!", 0);
            return;
        }
        Path path = new Path(from, to, graph);
        logger.setMessage(log.getText(), log.getFlag());
        try {
            if (pathExists(path)) {
                logger.setMessage("This path already exists!", -1);
                return;
            }
            path.setPathList(graph.findPath(from, to));
            path.getShowPath().setOnAction(this::handleShowPath);
            path.getShowWeights().setOnAction(this::handleShowPath);
            path.getDoNotShowWeights().setOnAction(this::handleShowPath);
            path.display(canvas, path.getShowWeights().isSelected());

            data.add(path);
            pathTableNewValues();
            logger.setMessage("Path from vertex " + from.getNumber() + " to " + to.getNumber() + " was found successfully!", 1);
        } catch (NullPointerException e) {
            logger.setMessage("Path from " + from.getNumber() + " to " + to.getNumber() + " doesn't exist.", -1);
        }
    }

    private boolean pathExists(Path newPath) {
        for (Path path : data) {
            if (path.equals(newPath))
                return true;
        }
        return false;
    }

    private void handleShowPath(ActionEvent event) {
        clearOffsets();
        graph.display(canvas);
        for (Path path : data) {
            if (path.getShowPath().isSelected()) {
                path.display(canvas, path.getShowWeights().isSelected());
            }
        }
    }

    private void pathTableNewValues() {
        showPathColumn.setCellValueFactory(new PropertyValueFactory<>("showPath"));
        whichPathColumn.setCellValueFactory(new PropertyValueFactory<>("fromTo"));
        removePathColumn.setCellValueFactory(new PropertyValueFactory<>("removePath"));
        showWeightsColumn.setCellValueFactory(new PropertyValueFactory<>("showWeights"));
        doNotShowWeightsColumn.setCellValueFactory(new PropertyValueFactory<>("doNotShowWeights"));
        pathTable.setItems(data);
    }

    public void handleRemovePathButton(ActionEvent event) {

        clearOffsets();
        data.removeIf(path -> path.getRemovePath().isSelected());

        graph.display(canvas);

        for (Path path : data) {
            if (path.getShowPath().isSelected()) {
                path.display(canvas, path.getShowWeights().isSelected());
            }
        }

    }

    public void handleGenerateButton(ActionEvent actionEvent) {
        data.clear();
        try {
            int rows = Integer.parseInt(rowsField.getText());
            int columns = Integer.parseInt(columnsField.getText());
            if (rows > 160 || columns > 160) {
                logger.setMessage("Number of columns and rows have the max size of 160.", -1);
                return;
            }
            try {
                double lowerLimit = Double.parseDouble(lowerLimitField.getText());
                double upperLimit = Double.parseDouble(upperLimitField.getText());
                if (lowerLimit < 0.0) {
                    logger.setMessage("Number of lower limit must be positive or equals 0.", -1);
                    return;
                }
                if (lowerLimit >= upperLimit) {
                    logger.setMessage("Number of lower limit must be lower than number of upper limit.", -1);
                    return;
                }
                if (lowerLimit > 1000) {
                    logger.setMessage("Number of lower limit must be lower than 1000.", -1);
                    return;
                }
                if (upperLimit > 1000) {
                    logger.setMessage("Number of upper limit must be lower than 1000.", -1);
                    return;
                }
                ToggleGroup tg = allEdgesMode.getToggleGroup();
                GraphGenerator generator = new GraphGenerator(rows, columns);
                if (tg.getSelectedToggle() == allEdgesMode) {
                    graph = generator.generate(1, lowerLimit, upperLimit);
                } else if (tg.getSelectedToggle() == connectedMode) {
                    graph = generator.generate(2, lowerLimit, upperLimit);
                } else if (tg.getSelectedToggle() == randomMode) {
                    graph = generator.generate(3, lowerLimit, upperLimit);
                } else {
                    logger.setMessage("Please, choose the mode of generation", -1);
                    return;

                }
                graph.display(canvas);
                createOffsets();
                logger.setMessage("Graph was generated successfully!", 1);
            } catch (NumberFormatException e) {
                logger.setMessage("Lower limit and upper limit must be floating point numbers, point should be '.' not ','", 0);
            }
        } catch (NumberFormatException e) {
            logger.setMessage("Number of rows and columns must be positive numbers", 0);
        }
    }

    public void handleSaveToFileButton(ActionEvent event) {
        if (saveToPath.getText().isEmpty()) {
            logger.setMessage("Firstly type the path to the file.", -1);
            return;
        }

        String path = saveToPath.getText();
        log.clear();
        graph.saveToFile(path);
        if (!log.getText().isEmpty()) {
            logger.setMessage(log.getText(), 0);
            return;
        }
        logger.setMessage("Graph saved to file successfully!", 1);
    }

    private void createOffsets() {
        offsets = new double[graph.getSize()];
        offsetsRight = new double[graph.getSize()];
        offsetsLeft = new double[graph.getSize()];
        offsetsUp = new double[graph.getSize()];
        offsetsDown = new double[graph.getSize()];
    }

    private void clearOffsets() {
        for (int i = 0; i < graph.getSize(); i++) {
            offsets[i] = 0;
            offsetsRight[i] = 0;
            offsetsLeft[i] = 0;
            offsetsUp[i] = 0;
            offsetsDown[i] = 0;
        }
    }


}