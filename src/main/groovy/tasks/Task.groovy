package tasks

import com.calendarfx.model.Entry
import com.calendarfx.model.Interval
import nagg.Deadline

import java.text.MessageFormat
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class Task {

    String name
    Deadline endDate
    int totalHours
    int workHoursLeft

    final Closure<Long> daysUntilEnd = { ChronoUnit.DAYS.between(LocalDateTime.now(), endDate.getStartDate()) }
    final Closure<Integer> averageWorkHoursPerDay = { (int)Math.ceil(workHoursLeft / daysUntilEnd.call() as double) }

    Task(String name, Deadline endDate, int hours) {
        this.name = name
        this.endDate = endDate
        this.workHoursLeft = totalHours = hours
    }

    List<Entry> genEntries(){
        int noEntries = averageWorkHoursPerDay.call()
        List<Entry> entries = new ArrayList<>()

        noEntries.times {
            Entry<Object> en = new Entry<>(MessageFormat.format("Work on {0}, day {1}", name, it));
            def when = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).plusDays(it)
            Interval interval2 = new Interval(when, when.plusHours(20))

            en.setInterval(interval2)
            en.setFullDay(true)

            entries.add(en)

        }

        return entries

    }

}



