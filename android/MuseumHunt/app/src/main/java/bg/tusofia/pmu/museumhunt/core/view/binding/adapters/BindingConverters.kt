package bg.tusofia.pmu.museumhunt.core.view.binding.adapters

import android.view.View
import androidx.databinding.BindingConversion

object BindingConverters {

    @JvmStatic
    @BindingConversion
    fun setVisibility(visible: Boolean) = if (visible) View.VISIBLE else View.GONE

}