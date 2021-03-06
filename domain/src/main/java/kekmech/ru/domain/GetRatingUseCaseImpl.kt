package kekmech.ru.domain

import kekmech.ru.core.dto.AcademicScore
import kekmech.ru.core.repositories.BarsRepository
import kekmech.ru.core.usecases.GetRatingUseCase

class GetRatingUseCaseImpl constructor(
    private val barsRepository: BarsRepository
) : GetRatingUseCase {
    override suspend fun invoke(forceRefresh: Boolean): AcademicScore? {
        return barsRepository.getScoreAsync(forceRefresh)
    }
}