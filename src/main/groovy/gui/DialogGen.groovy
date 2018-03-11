package gui

import impl.com.calendarfx.view.NumericTextField
import javafx.application.Platform
import javafx.geometry.Insets
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.util.Pair

class DialogGen {

    static Dialog<Pair<String, Integer>> genTaskDialog(){
        // vbox, hbox, label, text field, button

        // Create the dialog box
        Dialog<Pair<String, Integer>> dialog = new Dialog<>();
        dialog.setTitle("Task Input");

        // Add the buttons
        ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);

        // Set up a new gridpane
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        // Set up new text fields
        TextField name = new TextField();
        name.setPromptText("Title");
        NumericTextField hours = new NumericTextField();
        hours.setPromptText("Hours of Work");

        // Add text fields and labels to gridpane
        def dial = gridPane.&add
        dial new Label("Task Title: "), 0, 0
        dial name, 1, 0
        dial new Label("Hours of Work: "), 0, 1
        dial hours, 1, 1

        // Set the dialog box contents to that of the gridpane
        dialog.getDialogPane().setContent(gridPane);

        // Request focus on the name field by default.
        Platform.runLater{ name.requestFocus()}

        // Convert the result to a name-hours-pair when the OK button is clicked.
        dialog.setResultConverter {dialogButton ->
            if (dialogButton == ok) {
                return new Pair<>(name.getText(), hours.getText());
            }
            return null;
        };

        return dialog
    }
}
