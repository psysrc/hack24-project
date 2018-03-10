package nagg

import com.calendarfx.model.Entry
import com.calendarfx.model.Interval
import com.calendarfx.view.CalendarView
import com.calendarfx.view.DateControl
import com.calendarfx.view.Messages
import com.calendarfx.view.VirtualGrid
import javafx.scene.control.Alert

import java.text.MessageFormat
import java.time.*

class NaggingCalendarView extends CalendarView{

    int entryCounter = 1
    List<Entry> events = new ArrayList<Entry>()

    NaggingCalendarView() {
        setEntryFactory{ param ->
            DateControl control = param.getDateControl();

            VirtualGrid grid = control.getVirtualGrid();
            println param.getZonedDateTime()
            ZonedDateTime time = param.getZonedDateTime();
            DayOfWeek firstDayOfWeek = getFirstDayOfWeek();
            ZonedDateTime lowerTime = grid.adjustTime(time, false, firstDayOfWeek);
            ZonedDateTime upperTime = grid.adjustTime(time, true, firstDayOfWeek);

            if (Duration.between(time, lowerTime).abs().minus(Duration.between(time, upperTime).abs()).isNegative()) {
                time = lowerTime;
            } else {
                time = upperTime;
            }

            Entry deadline = new Entry(MessageFormat.format("New Deadline {0}", entryCounter++)); //$NON-NLS-1$
            Interval interval = new Interval(time.toLocalDateTime(), time.toLocalDateTime().plusHours(1));
            deadline.setInterval(interval);


//            if (control instanceof AllDayView) {
//                deadline.setFullDay(true);
//            }

            Entry<Object> entry2 = new Entry<>(MessageFormat.format(Messages.getString("DateControl.DEFAULT_ENTRY_TITLE"), entryCounter++)); //$NON-NLS-1$
            def bob = LocalDateTime.of(2018, Month.MARCH,9,0,0)
            Interval interval2 = new Interval(bob, bob.plusHours(1));
            entry2.setInterval(interval2);
//
//
//            if (control instanceof AllDayView) {
//                entry.setFullDay(true);
//            }
//
            events.add(entry2)

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
