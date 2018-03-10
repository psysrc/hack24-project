package tasks

class Task {

    String name
    int daysLeftUntilDeadline
    int workHoursLeft

    Closure<BigDecimal> averageWorkHoursPerDay = { workHoursLeft/daysLeftUntilDeadline }

}



