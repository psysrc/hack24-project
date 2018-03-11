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
        int workLength = Math.ceil(workHoursLeft / daysLeft as double) as int

        List<Entry> entries = new ArrayList<>()

        daysLeft.times {
            def when = thisMoment.plusDays(it)
            Entry<Object> en = new Entry<>(MessageFormat.format("Work on {0}, for {1} hours, day #{2}", name, workLength, it+1));
            Interval interval2 = new Interval(when, when.plusHours(20))

            en.setInterval(interval2)
            en.setFullDay(true)

            entries.add(en)

        }

        return entries

    }

}



