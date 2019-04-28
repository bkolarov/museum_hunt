package bg.tusofia.pmu.museumhunt.di.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Provider

class ViewModelProviderFactory(private val creators: Map<Class<out ViewModel>, Provider<ViewModel>>) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass] ?: creators.asSequence()
            .find { (key, _) ->
                modelClass.isAssignableFrom(key)
            }?.let { (_, value) ->
                value
            } ?: throw IllegalArgumentException("Cannot find creator for class $modelClass")

        try {
            @Suppress("UNCHECKED_CAST")
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

}