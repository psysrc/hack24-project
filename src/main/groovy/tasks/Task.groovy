package tasks

class Task {

    int daysLeftUntilDeadline
    int workHoursLeft

    Closure<BigDecimal> averageWorkHoursPerDay = { workHoursLeft/daysLeftUntilDeadline }

}



