package bg.tusofia.pmu.museumhunt.domain.usecases.game

import bg.tusofia.pmu.museumhunt.domain.repository.GameRepository
import javax.inject.Inject

class GetLevelProgressDataUseCase @Inject constructor(private val gameRepository: GameRepository) {

    fun getLevelProgressDataUseCase(levelId: Long) = gameRepository.getLevelProgressById(levelId)

}