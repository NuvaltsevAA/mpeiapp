package kekmech.ru.core.repositories

import kekmech.ru.core.dto.User

interface UserRepository {
    var savedUpdateUrl: String
    var savedUpdateDescription: String
    var appLaunchCount: Int
    var isShowedUpdateDialog: Boolean

    var mapState: Int

    var isDarkThemeEnabled: Boolean

    fun get(refresh: Boolean = false): User
}