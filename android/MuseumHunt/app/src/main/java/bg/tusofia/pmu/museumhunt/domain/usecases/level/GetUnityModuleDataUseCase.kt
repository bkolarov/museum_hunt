package bg.tusofia.pmu.museumhunt.domain.usecases.level

import bg.tusofia.pmu.museumhunt.domain.usecases.game.GetLevelProgressDataUseCase
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetUnityModuleDataUseCase @Inject constructor(
    private val getLevelDataUseCase: GetLevelDataUseCase,
    private val getLevelProgressDataUseCase: GetLevelProgressDataUseCase,
    private val gson: Gson
) {

    fun getUnityModuleData(levelId: Long) =
        getLevelProgressDataUseCase.getLevelProgressDataUseCase(levelId)
            .observeOn(Schedulers.computation())
            .flatMap {
                getLevelDataUseCase.getLevelData(it.number)
            }
            .map {
                gson.toJson(UnityModuleData(it.stageObstacle.hintWords))
            }

}

data class UnityModuleData(@SerializedName("HintWords") val hintWords: List<String>)