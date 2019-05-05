package bg.tusofia.pmu.museumhunt.ingame.riddle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import bg.tusofia.pmu.museumhunt.R
import bg.tusofia.pmu.museumhunt.base.fragment.BaseFragment
import bg.tusofia.pmu.museumhunt.databinding.FragmentRiddleBinding

class RiddleFragment : BaseFragment<FragmentRiddleBinding, RiddleViewModel>() {

    private val input by navArgs<RiddleFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentRiddleBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val answersAdapter = AnswersAdapter(Observer {
            viewModel.onAnswerClick(it)
        })

        viewModel.answersEvent.observe(viewLifecycleOwner, answersAdapter)

        binding { vm ->
            viewModel = vm

            toolbar.inflateMenu(R.menu.menu_toolbar_riddle_fragment)
            toolbar.setOnMenuItemClickListener {
                if (it.itemId == R.id.menu_item_help) {
                    vm.onHelpClick()
                }

                true
            }

            toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
            toolbar.setNavigationOnClickListener {
                vm.onToolbarNavigationClick()
            }

            rvAnswers.layoutManager = LinearLayoutManager(context)
            rvAnswers.setHasFixedSize(true)
            rvAnswers.adapter = answersAdapter
        }

        viewModel.goBackEvent.observe(viewLifecycleOwner, Observer {
            requireActivity().onBackPressed()
        })

        viewModel.init(input.levelId)
    }

    override fun instantiateViewModel(): RiddleViewModel =
            ViewModelProviders.of(this, viewModelFactory)[RiddleViewModel::class.java]

}