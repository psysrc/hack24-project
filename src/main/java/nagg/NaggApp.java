package nagg;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import gui.WorkInputEntryDialog;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;

public class NaggApp extends Application {

        @Override
        public void start(Stage primaryStage) throws Exception {

            NaggingCalendarView calendarView = new NaggingCalendarView();

            // TODO: Add a new popover to calendarView
            // Line below adds the default popover thing. Don't know how. But it works.
//            calendarView.setEntryDetailsPopOverContentCallback(param -> new EntryPopOverContentPane(param.getPopOver(), param.getDateControl(), param.getEntry()));
            calendarView.setEntryDetailsCallback(param -> {
                WorkInputEntryDialog.MakeDialog(param.getDateControl(), param.getEntry()).showAndWait();
                return true;
            });
//            calendarView.setEntryDetailsCallback(param -> {return true;});

            Calendar cal = new Calendar("Schedule");

            cal.setStyle(Calendar.Style.STYLE2);

            CalendarSource myCalendarSource = new CalendarSource("Calendars");
            myCalendarSource.getCalendars().addAll(cal);

            calendarView.getCalendarSources().addAll(myCalendarSource);

            calendarView.setRequestedTime(LocalTime.now());

            Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
                @Override
                public void run() {
                    while (true) {
                        Platform.runLater(() -> {
                            calendarView.setToday(LocalDate.now());
                            calendarView.setTime(LocalTime.now());
                        });

                        try {
                            // update every 10 seconds
                            sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            };

            updateTimeThread.setPriority(Thread.MIN_PRIORITY);
            updateTimeThread.setDaemon(true);
            updateTimeThread.start();

            MenuBar mb = new MenuBar();
            Menu mFile = new Menu("File");
            MenuItem saveButton = new MenuItem("Save");
            MenuItem loadButton = new MenuItem("Load");

            saveButton.setOnAction(e -> {
                FileChooser fc = new FileChooser();
                fc.setTitle("Open Resource File");
                File selectedFile = fc.showOpenDialog(primaryStage);
                if (selectedFile != null){
                    calendarView.save(selectedFile);
                }
            });

            loadButton.setOnAction(e -> {
                FileChooser fc = new FileChooser();
                fc.setTitle("Open Resource File");
                File selectedFile = fc.showOpenDialog(primaryStage);
                if (selectedFile != null){
                    calendarView.load(selectedFile);
                }
            });


            mFile.getItems().add(saveButton);
            mFile.getItems().add(loadButton);
            mb.getMenus().add(mFile);

            BorderPane bp = new BorderPane();
            bp.setCenter(calendarView);
            bp.setTop(mb);

            Scene scene = new Scene(bp);
            primaryStage.setTitle("Calendar");
            primaryStage.setScene(scene);


//            primaryStage.setWidth(1300);
//            primaryStage.setHeight(1000);

            primaryStage.centerOnScreen();
            primaryStage.show();
        }

        public static void main(String[] args) {
                launch(args);
        }
}