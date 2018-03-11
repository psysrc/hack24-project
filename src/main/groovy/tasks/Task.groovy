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

//    final Closure<Integer> daysUntilEnd = {
//        def z = Duration.between(LocalDateTime.now(),endDate.getStartAsLocalDateTime()).toDays()
//        z = z<0?z:1
//        return z as int}
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
            Entry<Object> en = new Entry<>(MessageFormat.format("Work on {0}, for {1} hours, day #{2}", name, workLength, it));
            Interval interval2 = new Interval(when, when)

            en.setInterval(interval2)
            en.setFullDay(true)

            entries.add(en)
//            println "added ${en}"

        }

        return entries

    }

}



