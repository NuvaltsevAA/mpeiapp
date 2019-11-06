package kekmech.ru.bars.main.model

import kekmech.ru.core.dto.AcademicDiscipline
import kekmech.ru.core.dto.AcademicScore
import kekmech.ru.core.usecases.*
import javax.inject.Inject

class BarsFragmentModelImpl @Inject constructor(
    private val isLoggedInBarsUseCase: IsLoggedInBarsUseCase,
    private val saveUserSecretsUseCase: SaveUserSecretsUseCase,
    private val getRatingUseCase: GetRatingUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val setDetailsDisciplineUseCase: SetDetailsDisciplineUseCase
) : BarsFragmentModel {

    override val isLoggedIn: Boolean
        get() = isLoggedInBarsUseCase()

    override fun saveUserSecrets(login: String, pass: String) {
        saveUserSecretsUseCase(login, pass)
    }

    override suspend fun getAcademicScore(refresh: Boolean): AcademicScore? {
        return getRatingUseCase(refresh)
    }

    override suspend fun getAcademicScoreAsync(refresh: Boolean, onRatingUpdatesListener: (AcademicScore?) -> Unit) {
        if (!refresh) onRatingUpdatesListener(getRatingUseCase())
        onRatingUpdatesListener(getRatingUseCase(true))
    }

    override fun clearUserSecrets() {
        logOutUseCase()
    }

    override fun setCurrentDiscipline(discipline: AcademicDiscipline) {
        setDetailsDisciplineUseCase(discipline)
    }
}