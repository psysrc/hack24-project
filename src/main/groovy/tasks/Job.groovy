package tasks

import com.calendarfx.model.Entry

class Job {

    Task task
    List<Entry> entries

    Job(Task task) {
        this.task = task
        entries = task.genEntries()
    }

    void updateEntries(){
        entries.each {
            it.setCalendar(null)
        }
        entries = task.genEntries()

    }
}
