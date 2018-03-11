package nagg

import com.calendarfx.model.Entry
import com.calendarfx.model.Interval
import com.calendarfx.view.CalendarView
import com.calendarfx.view.DateControl
import com.calendarfx.view.VirtualGrid
import javafx.scene.control.Alert

import java.text.MessageFormat
import java.time.DayOfWeek
import java.time.Duration
import java.time.ZonedDateTime

class NaggingCalendarView extends CalendarView{

    int entryCounter = 1
    List<Entry> events = new ArrayList<Entry>()
    TaskCalendar tc = new TaskCalendar()

    NaggingCalendarView() {

        calendars.add(tc)

        setEntryFactory{ param ->
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


            new Alert(Alert.AlertType.INFORMATION).showAndWait()

//            println events
            addLastEntry()

            return deadline
        }
    }

    def addLastEntry(){
        def cal = calendars.first()
        cal.addEntry(events.last())
//        cal.fireEvent(new CalendarEvent(CALENDAR_CHANGED, cal))
    }



}
