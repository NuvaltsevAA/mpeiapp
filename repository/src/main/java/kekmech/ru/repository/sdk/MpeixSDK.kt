package kekmech.ru.repository.sdk

import kekmech.ru.repository.sdk.bars.MpeixSDKBars
import kekmech.ru.repository.sdk.schedules.MpeixSDKSchedules

class MpeixSDK private constructor() {
    companion object {
        val schedules by lazy { MpeixSDKSchedules() }
        val bars by lazy { MpeixSDKBars() }
    }
}