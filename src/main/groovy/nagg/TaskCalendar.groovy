package nagg

import com.calendarfx.model.Calendar
import com.calendarfx.model.CalendarEvent
import com.calendarfx.model.Entry
import tasks.Task

class TaskCalendar extends Calendar {

    Task task
    List<Entry> steps = new ArrayList<>()
    Deadline deadline

    TaskCalendar(String name, Task task) {
        super(name)
        this.task = task
        deadline = task.endDate
        steps = task.genEntries()
    }

    private void addSteps(){
        addEntries(steps)
    }

    private void remSteps(){
        removeEntries(steps)
    }

    void updateEntries(){
        remSteps()
        steps = task.genEntries()
        addSteps()
        this.fireEvent(new CalendarEvent(CalendarEvent.CALENDAR_CHANGED, this))
    }

}
