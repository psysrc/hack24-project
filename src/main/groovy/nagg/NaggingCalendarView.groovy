package nagg

import com.calendarfx.model.Interval
import com.calendarfx.view.CalendarView
import com.calendarfx.view.DateControl
import com.calendarfx.view.VirtualGrid
import javafx.scene.control.Dialog
import javafx.util.Pair
import tasks.Task

import java.text.MessageFormat
import java.time.DayOfWeek
import java.time.Duration
import java.time.ZonedDateTime

import static gui.DialogGen.genTaskDialog

class NaggingCalendarView extends CalendarView{

    int entryCounter = 1
    List<TaskCalendar> tasks = new ArrayList<TaskCalendar>()

    NaggingCalendarView() {

//        calendars.add(tc)

        setEntryFactory{ param ->

            Dialog dialog = genTaskDialog()

            // Show the dialog box
            Optional<Pair<String, Integer>> result = dialog.showAndWait();

            // If we return a good result, do stuff
            result.ifPresent{ Pair <String, Integer> pair ->
                System.out.println("Name=" + pair.getKey() + ", Hours=" + pair.getValue());


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


                tasks.add(new TaskCalendar(pair.key, new Task(pair.key,deadline,pair.value as int)))
                tasks.last().updateEntries()
                addLastTask()

//                println aggregateNagg()

            };


            return null
        }
    }

    def addLastTask(){
        getCalendarSources().first().getCalendars().add(tasks.last())
    }

    List<String> extractNaggs(){
        tasks.parallelStream().map{"On ${it.task.name}, for ${it.task.averageWorkHoursPerDay.call()} hours, \n"} as List<String>
    }

    String aggregateNagg(){
        """Today, we are working on:
            ${extractNaggs()}
           Better get going! """
    }
}
