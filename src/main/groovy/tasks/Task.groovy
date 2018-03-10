package tasks

import com.calendarfx.model.Entry

class Task {

    String name
    int daysLeftUntilDeadline
    int workHoursLeft

    Closure<BigDecimal> averageWorkHoursPerDay = { workHoursLeft/daysLeftUntilDeadline }

    List<Entry> genEntries(){
        // cool stuff happens here

    }

}



