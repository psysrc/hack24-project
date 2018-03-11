package nagg

import com.calendarfx.model.Entry
import com.calendarfx.model.Interval
import com.calendarfx.view.CalendarView
import com.calendarfx.view.DateControl
import com.calendarfx.view.VirtualGrid
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

import java.text.MessageFormat
import java.time.DayOfWeek
import java.time.Duration
import java.time.ZonedDateTime

class NaggingCalendarView extends CalendarView{

    int entryCounter = 1
    List<Entry> events = new ArrayList<Entry>()
    TaskCalendar tc = new TaskCalendar()

    NaggingCalendarView() {

//        calendars.add(tc)

        setEntryFactory{ param ->

            // TODO: Add a dialog box here to get information about the new task to be added
            def Dialog dialog = genTaskDialog()

            // Show the dialog box
            Optional<Pair<String, String>> result = dialog.showAndWait();

            // If we return a good result, do stuff
            result.ifPresent{pair ->
                System.out.println("Name=" + pair.getKey() + ", Hours=" + pair.getValue());
            };


            DateControl control = param.getDateControl();

            VirtualGrid grid = control.getVirtualGrid();
            ZonedDateTime time = param.getZonedDateTime();
            DayOfWeek firstDayOfWeek = getFirstDayOfWeek();
            ZonedDateTime lowerTime = grid.adjustTime(time, false, firstDayOfWeek);
            ZonedDateTime upperTime = grid.adjustTime(time, true, firstDayOfWeek);

            if (Duration.between(time, lowerTime).abs().minus(Duration.between(time, upperTime).abs()).isNegative()) {
                time = lowerTime;
            } else {
                time = upperTime;
            }

            Deadline deadline = new Deadline(MessageFormat.format("New Deadline {0}", entryCounter++)); //$NON-NLS-1$
            Interval interval = new Interval(time.toLocalDateTime(), time.toLocalDateTime().plusHours(1));
            deadline.setInterval(interval);


//            println events
//            addLastEntry()
//          TODO: return null and generate new Calendar
            return deadline
        }
    }

    def addLastEntry(){
        def cal = calendars.first()
        cal.addEntry(events.last())
//        cal.fireEvent(new CalendarEvent(CALENDAR_CHANGED, cal))
    }

    def genTaskDialog(){
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
            gridPane.add(new Label("Task Title: "), 0, 0);
            gridPane.add(name, 1, 0);
            gridPane.add(new Label("Hours of Work: "), 0, 1);
            gridPane.add(hours, 1, 1);

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
