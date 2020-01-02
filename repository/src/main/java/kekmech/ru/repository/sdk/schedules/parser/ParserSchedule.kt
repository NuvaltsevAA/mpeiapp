package kekmech.ru.repository.sdk.schedules.parser

import kekmech.ru.repository.sdk.schedules.parser.ParserCouple
import java.util.*

data class ParserSchedule(
    val couples: List<ParserCouple>,
    val firstCoupleDay: Calendar
)