package bg.tusofia.pmu.museumhunt.ingame.init

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import bg.tusofia.pmu.museumhunt.base.fragment.BaseFragment
import bg.tusofia.pmu.museumhunt.databinding.FragmentIngameHomeDestinationBinding
import bg.tusofia.pmu.museumhunt.ingame.IngameArgs
import kotlinx.android.parcel.Parcelize

class IngameHomeDestinationFragment : BaseFragment<FragmentIngameHomeDestinationBinding, IngameHomeDestinationViewModel>() {

    private val input: IngameHomeDestinationFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentIngameHomeDestinationBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.openUnityModuleEvent.observe(Observer {
            findNavController().navigate(IngameHomeDestinationFragmentDirections.actionHomeToUnityLauncherFragment(it))
        })

        viewModel.openRiddleScreenEvent.observe(Observer {
            findNavController().navigate(IngameHomeDestinationFragmentDirections.actionHomeToRiddleFragment(it))
        })

        viewModel.openMapScreenEvent.observe(Observer {
            findNavController().navigate(IngameHomeDestinationFragmentDirections.actionHomeToMapFragment(it))
        })

        viewModel.openBrowseGamesEvent.observe(Observer {
            findNavController().navigate(IngameHomeDestinationFragmentDirections.actionHomeToBrowseGamesFragment())
        })

        viewModel.decideAction(input.homeInput)
    }

    override fun instantiateViewModel(): IngameHomeDestinationViewModel =
            ViewModelProviders.of(this, viewModelFactory)[IngameHomeDestinationViewModel::class.java]

}

sealed class IngameHomeDestinationInput : Parcelable

@Parcelize
object ContinueGameInput : IngameHomeDestinationInput()

@Parcelize
data class ContinueLevelInput(val ingameArgs: IngameArgs) : IngameHomeDestinationInput()

@Parcelize
object NewGameInput : IngameHomeDestinationInput()