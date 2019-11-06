package kekmech.ru.repository.di

import android.content.Context
import android.os.Build
import dagger.Module
import dagger.Provides
import kekmech.ru.repository.auth.BaseKeyStore
import kekmech.ru.repository.auth.BaseKeyStoreV21

@Module
class AuthModule {
    @Provides
    fun provideKeyStore(context: Context): BaseKeyStore {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return BaseKeyStoreV21()
//        }
    }
}