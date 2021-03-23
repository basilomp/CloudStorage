package fileManager;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class paneController implements Initializable {
    @FXML
    TableView<FileInfo> catalog;

    @FXML
    ComboBox<String> diskList;

    @FXML
    TextField pathField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TableColumn<FileInfo, String> fileExtensionColumn = new TableColumn<>();
        fileExtensionColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getExtension().getName()));
        fileExtensionColumn.setPrefWidth(24);

        TableColumn<FileInfo, String> fileNameColumn = new TableColumn<>("Name");
        fileNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFileName()));
        fileNameColumn.setPrefWidth(240);

        TableColumn<FileInfo, Long> fileSizeColumn = new TableColumn<>("Size");
        fileSizeColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSize()));
        fileSizeColumn.setCellFactory(column -> {
            return new TableCell<FileInfo, Long>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if(item == null || empty) {
                        setText(null);
                        setStyle("");

                    } else {

                        String text = String.format("%,d bytes", item);
                        if(item == -1L) {
                            text=("[DIR]");
                        }
                        setText(text);
                    }
                }
            };
        });
        fileSizeColumn.setPrefWidth(120);

        TableColumn<FileInfo, String> lastModifiedColumn = new TableColumn<>("Last modified");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        lastModifiedColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLastModified().format(dtf)));
        lastModifiedColumn.setPrefWidth(120);


        catalog.getColumns().addAll(fileExtensionColumn, fileNameColumn, fileSizeColumn, lastModifiedColumn);
        catalog.getSortOrder().add(fileExtensionColumn);
        diskList.getItems().clear();
        for (Path p : FileSystems.getDefault().getRootDirectories()) {
            diskList.getItems().add(p.toString());
        }
        diskList.getSelectionModel().select(0);
        catalog.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2) {
                    Path path = Paths.get(pathField.getText()).resolve(catalog.getSelectionModel().getSelectedItem().getFileName());
                    if(Files.isDirectory(path)) {
                        updateList(path);
                    }
                }
            }
        });

        updateList(Paths.get("./testDirectory"));

    }

    public void updateList(Path path) {
        try {
            pathField.setText(path.normalize().toAbsolutePath().toString());
            catalog.getItems().clear();
            catalog.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList()));
            catalog.sort();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Failed to update file list", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void btnPathUpAction(ActionEvent actionEvent) {
        Path upperPath = Paths.get(pathField.getText()).getParent();
        if(upperPath != null) {
            updateList(upperPath);
        }
    }

    public void selectDiskAction(ActionEvent actionEvent) {
        ComboBox<String> element = (ComboBox<String>) actionEvent.getSource();
        updateList(Paths.get(element.getSelectionModel().getSelectedItem()));
    }

    public String getSelectedFileName() {
        if(catalog.isFocused()) {
            return null;
        }
        return catalog.getSelectionModel().getSelectedItem().getFileName();
    }

    public String getCurrentPath() {
        return pathField.getText();
    }
}
