package kekmech.ru.core.dto

import android.content.Context
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class Time(private val calendar: Calendar = Calendar.getInstance()) {

    constructor(date: Date) : this(Calendar.getInstance().apply { time = date })

    constructor(year: Int, month: Int, dayOfMonth: Int) : this(Calendar.getInstance().apply { set(year, month, dayOfMonth) })

    private val hoursMinutesFormatter = SimpleDateFormat("hh:mm", Locale.ENGLISH)
    private val yearMonthDayFormatter = SimpleDateFormat("yyyy.MM.dd", Locale.ENGLISH)

    val dayOfWeek by lazy { calendar.get(Calendar.DAY_OF_WEEK) }
    val dayOfMonth by lazy { calendar.get(Calendar.DAY_OF_MONTH) }
    val dayOfYear by lazy { calendar.get(Calendar.DAY_OF_YEAR) }
    val weekOfMonth by lazy { calendar.get(Calendar.WEEK_OF_MONTH) }
    val weekOfYear by lazy { calendar.get(Calendar.WEEK_OF_YEAR) }
    val month by lazy { calendar.get(Calendar.MONTH) }
    val year by lazy { calendar.get(Calendar.YEAR) }

    val semester by lazy { if (month in Calendar.FEBRUARY..Calendar.AUGUST) SemesterType.SPRING else SemesterType.FALL }
    val weekOfSemester by lazy {
        val firstDay = if (semester == SemesterType.FALL) fallSemesterFirstDay()
        else springSemesterFirstDay()
        return@lazy 1 + weekOfYear - firstDay.weekOfYear
    }
    val parity by lazy { if (weekOfSemester % 2 == 0) Parity.EVEN else Parity.ODD }
    val nextDay by lazy {
        Time(Calendar.getInstance().apply {
            timeInMillis = calendar.timeInMillis + MILLIS_IN_DAY
        })
    }
    val prevDay by lazy {
        Time(Calendar.getInstance().apply {
            timeInMillis = calendar.timeInMillis - MILLIS_IN_DAY
        })
    }

    /**
     * Получить день, который будет через [dayOffset] дней
     */
    fun getDayWithOffset(dayOffset: Int) =
        Time(Calendar.getInstance().apply {
            timeInMillis =
                calendar.timeInMillis + (MILLIS_IN_DAY * dayOffset)
        })

    /**
     * Форматирование времени к виду "Часы:Минуты"
     */
    val formattedAsHoursMinutes by lazy { hoursMinutesFormatter.format(calendar.time) }

    /**
     * Форматирование ГОД.МЕСЯЦ.ДЕНЬ
     */
    val formattedAsYearMonthDay by lazy { yearMonthDayFormatter.format(calendar.time) }

    /**
     * Форматирование к виду "День недели"
     * @param context - контекст через который можно получить доступ к strings.xml
     * @param stringArrayId - массив с названиями дней недели (ВСК, ПН, ВТ, СР ...)
     * @return [String] - соответствующее номеру названия дня недели
     * *Calendar.SUNDAY == 1 (недели начинаются с воскресенья, нумерация дней с единицы)
     */
    fun formattedAsDayName(context: Context?, stringArrayId: Int) = getStringArray(context, stringArrayId)[dayOfWeek - 1]

    /**
     * Форматирование к виду "Месяц"
     * @param context - контекст через который можно получить доступ к strings.xml
     * @param stringArrayId - массив с названиями дней недели (ЯНВАРЬ, ФЕВРАЛЬ, МАРТ, ...)
     * @return [String] - соответствующее номеру названия дня недели
     * *Calendar.JANUARY == 0 (нумерация месяцев с нуля)
     */
    fun formattedAsMonthName(context: Context?, stringArrayId: Int) = getStringArray(context, stringArrayId)[month]

    private fun getStringArray(context: Context?, stringArrayId: Int): Array<String> {
        if (context == null) {
            Log.e(this::class.java.simpleName, "Does not found string array resource")
            return emptyArray()
        }
        return context.resources.getStringArray(stringArrayId)
    }

    enum class Parity { ODD, EVEN }

    enum class SemesterType { FALL, SPRING }

    companion object {
        const val HOURS_IN_DAY = 24
        const val MINUTES_IN_DAY = HOURS_IN_DAY * 60
        const val SECONDS_IN_DAY = MINUTES_IN_DAY * 60
        const val MILLIS_IN_DAY = SECONDS_IN_DAY * 1000

        fun today() = Time(Calendar.getInstance())

        /**
         * Всегда первое сентября
         */
        fun fallSemesterFirstDay(year: Int = today().year): Time {
            val firstSep = Time(year, Calendar.SEPTEMBER, 1)
            return if (firstSep.dayOfWeek == Calendar.SUNDAY) firstSep.nextDay else firstSep
        }

        /**
         * Первый понедельник февраля
         */
        fun springSemesterFirstDay(year: Int = today().year): Time {
            var febDayNum = 1
            var day = Time(year, Calendar.FEBRUARY, febDayNum++)
            while (day.dayOfWeek != Calendar.MONDAY)
                day = Time(year, Calendar.FEBRUARY, febDayNum++)
            return day
        }
    }
}