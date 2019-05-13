package bg.tusofia.pmu.museumhunt.domain.usecases.level

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import javax.inject.Inject

class ConvertUnityDataUseCase @Inject constructor(private val gson: Gson) {

    fun convertUnityData(unityResultData: String): List<String> {
        return gson.fromJson<UnityModuleRsultData>(unityResultData, UnityModuleRsultData::class.java).foundWords
    }

}

data class UnityModuleRsultData(@SerializedName("FoundWords") val foundWords: List<String>)