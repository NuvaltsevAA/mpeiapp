package kekmech.ru.repository

import kekmech.ru.core.gateways.UserCacheGateway
import kekmech.ru.core.repositories.UserRepository
import kekmech.ru.core.dto.User

class UserRepositoryImpl(
    val cacheGateway: UserCacheGateway
) : UserRepository {

    override fun get(refrash: Boolean): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

