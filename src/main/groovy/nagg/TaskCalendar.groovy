package nagg

import com.calendarfx.model.Calendar
import tasks.Job

class TaskCalendar extends Calendar {

    List<Job> jobs = new ArrayList<>()

    TaskCalendar() {
        super()
    }
}
