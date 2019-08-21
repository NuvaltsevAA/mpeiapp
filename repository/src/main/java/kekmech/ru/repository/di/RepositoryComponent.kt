package kekmech.ru.repository.di

import dagger.Component
import kekmech.ru.core.ContextProvider
import kekmech.ru.core.RepositoryProvider
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RealmModule::class,
        RepositoryModule::class,
        GatewayModule::class
    ],
    dependencies = [
        ContextProvider::class
    ]
)
interface RepositoryComponent: RepositoryProvider {
    class Initializer private constructor() {
        companion object {
            fun init(contextProvider: ContextProvider): RepositoryComponent {
                return DaggerRepositoryComponent.builder()
                    .contextProvider(contextProvider)
                    .build()
            }
        }
    }
}