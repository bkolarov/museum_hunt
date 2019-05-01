package bg.tusofia.pmu.museumhunt.domain.usecases.level

import com.google.gson.Gson
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetUnityModuleDataUseCase @Inject constructor(private val getLevelDataUseCase: GetLevelDataUseCase,
                                                    private val gson: Gson) {

    fun getUnityModuleData(levelNum: Int) = getLevelDataUseCase.getLevelData(levelNum)
        .observeOn(Schedulers.computation())
        .map {
            gson.toJson(UnityModuleData(it.stageObstacle.hintWords))
        }

}

data class UnityModuleData(val HintWords: List<String>)