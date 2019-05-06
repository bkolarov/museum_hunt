package bg.tusofia.pmu.museumhunt.ingame.riddle

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import bg.tusofia.pmu.museumhunt.R
import bg.tusofia.pmu.museumhunt.base.fragment.BaseFragment
import bg.tusofia.pmu.museumhunt.base.fragment.finishOnBackPressed
import bg.tusofia.pmu.museumhunt.databinding.FragmentRiddleBinding
import com.google.android.material.snackbar.Snackbar

class RiddleFragment : BaseFragment<FragmentRiddleBinding, RiddleViewModel>() {

    private val input by navArgs<RiddleFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentRiddleBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        finishOnBackPressed()

        val answersAdapter = AnswersAdapter(Observer {
            viewModel.onAnswerClick(it)
        })

        viewModel.answersEvent.observe(viewLifecycleOwner, answersAdapter)

        viewModel.goBackEvent.observe(viewLifecycleOwner, Observer {
            activity?.finish()
        })

        viewModel.errorSnackBarEvent.observe(viewLifecycleOwner, Observer {
            Snackbar.make(view, getString(R.string.riddle_answer_error), Snackbar.LENGTH_LONG)
                .show()
        })

        viewModel.showDialogEvent.observe(viewLifecycleOwner, Observer {
            AlertDialog.Builder(context, R.style.AppTheme_Dialog_InGame)
                .setTitle(it.title)
                .setMessage(it.message)
                .setPositiveButton(it.positiveBtnTxt) { _, _ -> it.positiveBtnCallback }
                .show()
        })

        viewModel.nextScreenEevent.observe(viewLifecycleOwner, Observer { args ->
            args?.levelId?.let { findNavController().navigate(RiddleFragmentDirections.actionRiddleFragmentToMapFragment(it)) }
        })

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

        viewModel.init(input.levelId)
    }

    override fun instantiateViewModel(): RiddleViewModel =
        ViewModelProviders.of(this, viewModelFactory)[RiddleViewModel::class.java]

}