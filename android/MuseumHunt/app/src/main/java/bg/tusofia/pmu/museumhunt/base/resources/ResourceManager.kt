package bg.tusofia.pmu.museumhunt.base.resources

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes

interface ResourceManager {

    fun getString(@StringRes resId: Int): String

    fun getString(@StringRes resId: Int, vararg args: Any): String

    fun getStringArray(@ArrayRes resId: Int): Array<String>
}

class ResourceManagerImpl(private val context: Context) : ResourceManager {

    override fun getString(@StringRes resId: Int): String = context.getString(resId)

    override fun getString(@StringRes resId: Int, vararg args: Any): String = context.getString(resId, args)

    override fun getStringArray(@ArrayRes resId: Int): Array<String> = context.resources.getStringArray(resId)
}