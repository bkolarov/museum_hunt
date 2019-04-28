package bg.tusofia.pmu.museumhunt

import androidx.lifecycle.ViewModel
import bg.tusofia.pmu.museumhunt.base.resources.ResourceManager
import bg.tusofia.pmu.museumhunt.base.viewmodel.BaseViewModel
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(resourceManager: ResourceManager): BaseViewModel(resourceManager)