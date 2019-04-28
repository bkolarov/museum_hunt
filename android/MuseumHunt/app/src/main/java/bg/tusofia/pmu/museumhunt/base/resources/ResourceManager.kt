package bg.tusofia.pmu.museumhunt.base.resources

import android.content.Context
import androidx.annotation.StringRes

interface ResourceManager {

    fun getString(@StringRes resId: Int): String

    fun getString(@StringRes resId: Int, vararg args: Any): String
}

class ResourceManagerImpl(private val context: Context) : ResourceManager {

    override fun getString(@StringRes resId: Int): String = context.getString(resId)

    override fun getString(resId: Int, vararg args: Any): String = context.getString(resId, args)

}