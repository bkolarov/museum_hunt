package bg.tusofia.pmu.museumhunt.base.viewmodel

import androidx.lifecycle.ViewModel
import bg.tusofia.pmu.museumhunt.base.resources.ResourceManager

abstract class BaseViewModel(resourceManager: ResourceManager) : ViewModel(), ResourceManager by resourceManager {

}