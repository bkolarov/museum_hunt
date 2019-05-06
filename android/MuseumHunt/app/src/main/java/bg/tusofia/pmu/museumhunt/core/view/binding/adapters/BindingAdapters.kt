package bg.tusofia.pmu.museumhunt.core.view.binding.adapters

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.databinding.BindingAdapter

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("dispatchWindowInsetsToChildren")
    fun dispatchWindowInsetsToChildren(viewGroup: ViewGroup, dispatch: Boolean) {
        if (dispatch) {
            viewGroup.setOnApplyWindowInsetsListener { _, insets ->
                viewGroup.children.forEach {
                    it.dispatchApplyWindowInsets(insets)
                }

                insets
            }
        } else {
            viewGroup.setOnApplyWindowInsetsListener(null)
        }
    }

    @JvmStatic
    @BindingAdapter("constraintByWindowInsets")
    fun constraintByWindowInsets(view: View, constraint: Boolean) {
        if (constraint) {
            ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
                val params = v.layoutParams as ViewGroup.MarginLayoutParams
                params.topMargin = insets.systemWindowInsetTop
                insets.consumeSystemWindowInsets()
            }
        } else {
            ViewCompat.setOnApplyWindowInsetsListener(view, null)
        }
    }
}