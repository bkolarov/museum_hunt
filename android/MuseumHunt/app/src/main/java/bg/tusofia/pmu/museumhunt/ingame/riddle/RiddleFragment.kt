package bg.tusofia.pmu.museumhunt.ingame.riddle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import bg.tusofia.pmu.museumhunt.base.fragment.BaseFragment
import bg.tusofia.pmu.museumhunt.databinding.FragmentRiddleBinding

class RiddleFragment : BaseFragment<FragmentRiddleBinding, RiddleViewModel>() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentRiddleBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding { vm ->
            viewModel = vm

            toolbar.setNavigationOnClickListener {
                vm.onToolbarNavigationClick()
            }
        }

    }

    override fun instantiateViewModel(): RiddleViewModel =
            ViewModelProviders.of(this, viewModelFactory)[RiddleViewModel::class.java]

}