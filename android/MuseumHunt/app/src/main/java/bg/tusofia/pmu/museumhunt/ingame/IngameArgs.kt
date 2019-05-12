package bg.tusofia.pmu.museumhunt.ingame

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IngameArgs(val gameId: Long, val levelId: Long) : Parcelable