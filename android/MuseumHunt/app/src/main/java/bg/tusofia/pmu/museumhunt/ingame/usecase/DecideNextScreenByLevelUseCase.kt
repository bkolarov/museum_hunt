package bg.tusofia.pmu.museumhunt.ingame.usecase

import androidx.lifecycle.LiveData
import bg.tusofia.pmu.museumhunt.domain.db.entity.LevelProgress
import bg.tusofia.pmu.museumhunt.domain.db.entity.LevelStage
import bg.tusofia.pmu.museumhunt.domain.repository.GameRepository
import bg.tusofia.pmu.museumhunt.domain.usecases.game.GetLevelProgressDataUseCase
import bg.tusofia.pmu.museumhunt.domain.usecases.level.GetLevelDataUseCase
import bg.tusofia.pmu.museumhunt.ingame.IngameArgs
import com.hadilq.liveevent.LiveEvent
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class DecideNextScreenByLevelUseCase @Inject constructor(
    private val getLevelProgressDataUseCase: GetLevelProgressDataUseCase,
    private val getLevelDataUseCase: GetLevelDataUseCase,
    private val gameRepository: GameRepository) {

    private val _openUnityModuleEvent = LiveEvent<IngameArgs>()
    private val _openRiddleScreenEvent = LiveEvent<IngameArgs>()
    private val _openMapScreenEvent = LiveEvent<IngameArgs>()

    val openUnityModuleEvent: LiveData<IngameArgs> = _openUnityModuleEvent
    val openRiddleScreenEvent: LiveData<IngameArgs> = _openRiddleScreenEvent
    val openMapScreenEvent: LiveData<IngameArgs> = _openMapScreenEvent

    fun decideNextScreenUseCase(ingameArgs: IngameArgs): Completable {
        return getLevelProgressDataUseCase.getLevelProgressDataUseCase(ingameArgs.levelId)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { levelProgress ->
                when (levelProgress.stage) {
                    LevelStage.INIT -> _openUnityModuleEvent.postValue(ingameArgs)
                    LevelStage.OBSTACLE_PASSED -> _openRiddleScreenEvent.postValue(ingameArgs)
                    LevelStage.RIDDLE_PASSED -> _openMapScreenEvent.postValue(ingameArgs)
                    LevelStage.COMPLETED -> {
                        getLevelDataUseCase.getLevelData(levelProgress.number)
                            .flatMapCompletable { levelData ->
                                if (levelData.isLast) {
                                    Completable.complete()
                                } else {
                                    val newProgress = LevelProgress(id = ingameArgs.levelId, number = levelProgress.number + 1, stage = LevelStage.INIT)
                                    gameRepository.updateLevelProgress(newProgress)
                                        .doOnComplete {
                                            _openUnityModuleEvent.postValue(ingameArgs)
                                        }
                                }
                            }
                            .subscribe()
                    }
                }
            }
            .ignoreElement()
    }
}
