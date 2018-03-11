package tasks

import com.calendarfx.model.Entry
import com.calendarfx.model.Interval
import nagg.Deadline

import java.text.MessageFormat
import java.time.Duration
import java.time.LocalDateTime

class Task {

    String name
    Deadline endDate
    int totalHours
    int workHoursLeft

//    final Closure<Integer> daysUntilEnd = { ChronoUnit.DAYS.between(LocalDateTime.now(), endDate.getStartDate()) as int}
//    final Closure<Integer> averageWorkHoursPerDay = { Math.ceil(workHoursLeft / daysUntilEnd.call() as double) as int }

    Task(String name, Deadline endDate, int hours) {
        this.name = name
        this.endDate = endDate
        this.workHoursLeft = totalHours = hours
    }

    List<Entry> genEntries(){
        def thisMoment = LocalDateTime.now()

        Duration duration = Duration.between(thisMoment,endDate.getStartAsLocalDateTime())
        int daysLeft = duration.toDays() as int
        daysLeft = daysLeft>0 ? daysLeft : 1
        int noEntries = Math.ceil(workHoursLeft / daysLeft as double) as int

        List<Entry> entries = new ArrayList<>()

        noEntries.times {
            Entry<Object> en = new Entry<>(MessageFormat.format("Work on {0}, day {1}", name, it));
            def when = thisMoment.plusDays(it)
            Interval interval2 = new Interval(when, when.plusHours(20))

            en.setInterval(interval2)
            en.setFullDay(true)

            entries.add(en)

        }

        return entries

    }

}



