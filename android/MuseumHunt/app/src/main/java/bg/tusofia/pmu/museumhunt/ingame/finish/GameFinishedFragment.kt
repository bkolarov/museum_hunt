package bg.tusofia.pmu.museumhunt.ingame.finish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bg.tusofia.pmu.museumhunt.base.fragment.BaseFragment
import bg.tusofia.pmu.museumhunt.base.resources.ResourceManager
import bg.tusofia.pmu.museumhunt.base.viewmodel.BaseViewModel
import bg.tusofia.pmu.museumhunt.databinding.FragmentGameFinishedBinding
import com.hadilq.liveevent.LiveEvent
import javax.inject.Inject

class GameFinishedFragment : BaseFragment<FragmentGameFinishedBinding, GameFinishedViewModel>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentGameFinishedBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.viewModel = viewModel

        viewModel.navigateToStartScreen.observe(Observer {
            activity?.finish()
        })
    }

    override fun instantiateViewModel(): GameFinishedViewModel =
        ViewModelProviders.of(this, viewModelFactory)[GameFinishedViewModel::class.java]

}


class GameFinishedViewModel @Inject constructor(resourceManager: ResourceManager) : BaseViewModel(resourceManager) {

    private val _navigateToStartScreen = LiveEvent<Unit>()

    val navigateToStartScreen: LiveData<Unit> = _navigateToStartScreen

    fun onClick() {
        _navigateToStartScreen.postValue(Unit)
    }

}