package bg.tusofia.pmu.museumhunt.domain.usecases.game

import bg.tusofia.pmu.museumhunt.domain.db.entity.LevelStage
import bg.tusofia.pmu.museumhunt.domain.repository.GameRepository
import bg.tusofia.pmu.museumhunt.util.rx.toSingle
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SetObstaclesPassedUseCase @Inject constructor(
    private val gameRepository: GameRepository,
    private val getLevelProgressDataUseCase: GetLevelProgressDataUseCase,
    private val updateLevelStageUseCase: UpdateLevelStageUseCase
) {

    fun setObstaclesPassedUseCase(levelId: Long, foundWords: List<String>): Completable =
        updateLevelStageUseCase.updateLevelStage(levelId, LevelStage.OBSTACLE_PASSED)
            .observeOn(Schedulers.computation())
            .toSingle()
            .flatMap {
                getLevelProgressDataUseCase.getLevelProgressDataUseCase(levelId)
            }
            .flatMap {
                it.hintWords.clear()
                it.hintWords.addAll(foundWords)
                gameRepository.updateLevelProgress(it).toSingle()
            }
            .ignoreElement()
}

class SetRiddleAnsweredUseCase @Inject constructor(
    private val updateLevelStageUseCase: UpdateLevelStageUseCase
) {

    fun setRiddleAnswered(levelId: Long): Completable {
        return updateLevelStageUseCase.updateLevelStage(levelId, LevelStage.RIDDLE_PASSED)
            .observeOn(Schedulers.computation())
            .toSingle()
            .ignoreElement()
    }

}

class SetDestinationReachedUseCase @Inject constructor(
    private val updateLevelStageUseCase: UpdateLevelStageUseCase
) {

    fun setDestinationReached(levelId: Long): Completable {
        return updateLevelStageUseCase.updateLevelStage(levelId, LevelStage.COMPLETED)
            .observeOn(Schedulers.computation())
            .toSingle()
            .ignoreElement()

    }

}