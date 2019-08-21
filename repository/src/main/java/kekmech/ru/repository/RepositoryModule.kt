package kekmech.ru.repository

import dagger.Binds
import dagger.Module
import dagger.Provides
import io.realm.Realm
import kekmech.ru.core.gateways.ScheduleCacheGateway
import kekmech.ru.core.gateways.ScheduleRemoteGateway
import kekmech.ru.core.gateways.UserCacheGateway
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.repositories.UserRepository
import kekmech.ru.repository.gateways.ScheduleCacheGatewayImpl
import kekmech.ru.repository.gateways.ScheduleRemoteGatewayImpl
import kekmech.ru.repository.gateways.UserCacheGatewayImpl
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun provideScheduleRepository(repoImpl: ScheduleRepositoryImpl): ScheduleRepository

    @Binds
    abstract fun provideUserRepository(repoImpl: UserRepositoryImpl): UserRepository

}