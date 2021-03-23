package fileManager;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Controller {
    @FXML
    VBox leftPane, rightPane;



    public void btnExitAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void copyButtonAction(ActionEvent actionEvent) {
        paneController leftPC = (paneController) leftPane.getProperties().get("ctrl");
        paneController rightPC = (paneController) rightPane.getProperties().get("ctrl");


        if(leftPC.getSelectedFileName() == null && rightPC.getSelectedFileName() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Select a file");
            alert.showAndWait();
            return;
        }

        paneController srcPC = null, dstPC = null;
        if(leftPC.getSelectedFileName() != null) {
            srcPC = leftPC;
            dstPC = rightPC;
        }
        if(rightPC.getSelectedFileName() != null) {
            srcPC = rightPC;
            dstPC = leftPC;
    }
        Path srcPath = Paths.get(srcPC.getCurrentPath(), srcPC.getSelectedFileName());
        Path dstPath = Paths.get(dstPC.getCurrentPath()).resolve(srcPath.getFileName());

        try {
            Files.copy(srcPath, dstPath);
            dstPC.updateList(Paths.get(dstPC.getCurrentPath()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to copy selected file");
            alert.showAndWait();
        }
    }
}
