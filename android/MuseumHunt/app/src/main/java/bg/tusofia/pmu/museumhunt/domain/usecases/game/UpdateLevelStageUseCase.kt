package bg.tusofia.pmu.museumhunt.domain.usecases.game

import bg.tusofia.pmu.museumhunt.domain.db.entity.LevelStage
import bg.tusofia.pmu.museumhunt.domain.repository.GameRepository
import javax.inject.Inject

class UpdateLevelStageUseCase @Inject constructor(private val gameRepository: GameRepository) {

    fun updateLevelStage(levelId: Long, levelStage: LevelStage) = gameRepository.setLevelStage(levelId, levelStage)

}