package nagg

import com.calendarfx.model.Entry
import com.calendarfx.model.Interval
import com.calendarfx.view.AllDayView
import com.calendarfx.view.CalendarView
import com.calendarfx.view.DateControl
import com.calendarfx.view.Messages
import com.calendarfx.view.VirtualGrid
import javafx.scene.control.Alert

import java.text.MessageFormat
import java.time.DayOfWeek
import java.time.Duration
import java.time.ZonedDateTime

class NaggingCalendarView extends CalendarView{

    int entryCounter = 1
    List<Entry> events = new ArrayList<Entry>()

    NaggingCalendarView() {
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

            Entry<Object> entry = new Entry<>(MessageFormat.format(Messages.getString("DateControl.DEFAULT_ENTRY_TITLE"), entryCounter++)); //$NON-NLS-1$
            Interval interval = new Interval(time.toLocalDateTime(), time.toLocalDateTime().plusHours(1));
            entry.setInterval(interval);

            if (control instanceof AllDayView) {
                entry.setFullDay(true);
            }

            events.add(entry)

            new Alert(Alert.AlertType.INFORMATION).showAndWait()

            println events


            return entry
        }
    }



}
