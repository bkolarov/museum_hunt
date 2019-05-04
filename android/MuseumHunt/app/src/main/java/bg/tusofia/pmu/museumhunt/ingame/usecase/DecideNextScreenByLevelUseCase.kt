package bg.tusofia.pmu.museumhunt.ingame.usecase

import androidx.lifecycle.LiveData
import bg.tusofia.pmu.museumhunt.domain.db.entity.LevelStage
import bg.tusofia.pmu.museumhunt.domain.usecases.game.GetLevelProgressDataUseCase
import com.hadilq.liveevent.LiveEvent
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class DecideNextScreenByLevelUseCase @Inject constructor(private val getLevelProgressDataUseCase: GetLevelProgressDataUseCase) {
    private val _openUnityModuleEvent = LiveEvent<NextScreenArgs>()
    private val _openRiddleScreenEvent = LiveEvent<NextScreenArgs>()

    val openUnityModuleEvent: LiveData<NextScreenArgs> = _openUnityModuleEvent
    val openRiddleScreenEvent: LiveData<NextScreenArgs> = _openRiddleScreenEvent

    fun decideNextScreenUseCase(levelId: Long): Completable {
        return getLevelProgressDataUseCase.getLevelProgressDataUseCase(levelId)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { levelProgress ->
                when (levelProgress.stage) {
                    LevelStage.INIT -> _openUnityModuleEvent.postValue(NextScreenArgs(levelId))
                    LevelStage.OBSTACLE_PASSED -> _openRiddleScreenEvent.postValue(NextScreenArgs(levelId))
                }
            }
            .ignoreElement()
    }
}

data class NextScreenArgs(val levelId: Long)