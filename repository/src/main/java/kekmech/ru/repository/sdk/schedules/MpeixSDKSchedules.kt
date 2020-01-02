package kekmech.ru.repository.sdk.schedules

import kekmech.ru.core.dto.AcademicSession
import kekmech.ru.core.dto.Time
import kekmech.ru.repository.sdk.schedules.parser.ScheduleParser
import kekmech.ru.repository.sdk.schedules.parser.ParserCouple
import kekmech.ru.repository.sdk.schedules.parser.ParserSchedule
import kekmech.ru.repository.sdk.schedules.parser.SessionParser
import kotlinx.coroutines.*
import org.jsoup.Jsoup

class MpeixSDKSchedules {

    suspend fun getSemesterScheduleForGroup(groupName: String): ParserSchedule {
        // загружаем страничку и вбиваем номер группы в форму
        val inputs = Jsoup.connect("https://mpei.ru/Education/timetable/Pages/default.aspx")
            .get()
            .select("input")
        val eventValidationInput = inputs.find { it.attr("name") == "__EVENTVALIDATION" }!!
        val viewStateInput = inputs.find { it.attr("name") == "__VIEWSTATE" }!!
        val groupNameInput = inputs.find { it.attr("name").matches("ctl00\\\$ctl30.*ctl03".toRegex()) }!!
        val groupSubmitInput = inputs.find { it.attr("name").matches("ctl00\\\$ctl30.*ctl04".toRegex()) }!!

        // вычисляем первый понедельник семестра и второй понедельник семестра
        val firstMonday = Time.firstSemesterDay().gotoMonday()
        val secondMonday = firstMonday.getDayWithOffset(7)

        val href = Jsoup.connect("https://mpei.ru/Education/timetable/Pages/default.aspx")
            .data(eventValidationInput.attr("name"), eventValidationInput.attr("value"))
            .data(viewStateInput.attr("name"), viewStateInput.attr("value"))
            .data(groupNameInput.attr("name"), groupName)
            .data(groupSubmitInput.attr("name"), groupSubmitInput.attr("value"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .followRedirects(false)
            .post()
            .select("a[href]")
            .first()
            .attr("href")

        val tasks = listOf(
            async {
                // скрапим первую страничку
                val firstWeekHtml = Jsoup.connect("$href&start=${firstMonday.formattedAsYearMonthDay}")
                    .get()
                    .select("table[class*=mpei-galaktika-lessons-grid-tbl]")
                    .html()
                ScheduleParser().parse(firstWeekHtml)
            },
            async {
                // скрапим вторую страничку
                val secondWeekHtml = Jsoup.connect("$href&start=${secondMonday.formattedAsYearMonthDay}")
                    .get()
                    .select("table[class*=mpei-galaktika-lessons-grid-tbl]")
                    .html()
                ScheduleParser().parse(secondWeekHtml)
            }
        )

        tasks.awaitAll().let { it[0] to it[1] }.let {
            // объединяем результаты парсинга
            val joinedCouples = mutableListOf<ParserCouple>()
            joinedCouples.addAll(it.first.couples)
            joinedCouples.addAll(it.second.couples.onEach { couple -> couple.week = 2 })
            return ParserSchedule(
                joinedCouples,
                firstMonday.calendar
            )
        }
    }

    suspend fun getSessionScheduleForGroup(groupName: String): AcademicSession {
        val inputs = Jsoup.connect("https://mpei.ru/Education/timetable/Pages/default.aspx")
            .get()
            .select("input")
        val eventValidationInput = inputs.find { it.attr("name") == "__EVENTVALIDATION" }!!
        val viewStateInput = inputs.find { it.attr("name") == "__VIEWSTATE" }!!
        val groupNameInput = inputs.find { it.attr("name").matches("ctl00\\\$ctl30.*ctl03".toRegex()) }!!
        val groupSubmitInput = inputs.find { it.attr("name").matches("ctl00\\\$ctl30.*ctl04".toRegex()) }!!


        val href = Jsoup.connect("https://mpei.ru/Education/timetable/Pages/default.aspx")
            .data(eventValidationInput.attr("name"), eventValidationInput.attr("value"))
            .data(viewStateInput.attr("name"), viewStateInput.attr("value"))
            .data(groupNameInput.attr("name"), groupName)
            .data(groupSubmitInput.attr("name"), groupSubmitInput.attr("value"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .followRedirects(false)
            .post()
            .select("a[href]")
            .first()
            .attr("href")

        val timetable = Jsoup.connect(href)
            .post()
        val __EVENTVALIDATION = timetable.select("input[name=__EVENTVALIDATION]").attr("value")
        val __VIEWSTATE = timetable.select("input[name=__VIEWSTATE]").attr("value")
        val __EVENTARGUMENT = "0"
        val __VIEWSTATEGENERATOR = timetable.select("input[name=__VIEWSTATEGENERATOR]").attr("value")
        val __EVENTTARGET = timetable.select("div[class=mpei-tt-outer-wrap]").select("a").attr("href").let {
            ".*'(.*)'.*'.*'.*".toRegex().find(it)?.groups?.get(1)?.value ?: ""
        }
        val result = Jsoup.connect(href)
            .data("__EVENTVALIDATION", __EVENTVALIDATION)
            .data("__VIEWSTATE", __VIEWSTATE)
            .data("__EVENTARGUMENT", __EVENTARGUMENT)
            .data("__VIEWSTATEGENERATOR", __VIEWSTATEGENERATOR)
            .data("__EVENTTARGET", __EVENTTARGET)
            .followRedirects(true)
            .post()
        val scheduleTable = result.select("div[class=mpei-tt-grid-wrap]")
        return SessionParser().parse(scheduleTable)
    }

    private fun<T> async(action: suspend CoroutineScope.() -> T) = GlobalScope.async(Dispatchers.IO, block = action)
}