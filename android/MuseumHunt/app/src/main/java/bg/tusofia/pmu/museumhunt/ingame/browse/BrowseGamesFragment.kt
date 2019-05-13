package bg.tusofia.pmu.museumhunt.ingame.browse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import bg.tusofia.pmu.museumhunt.R
import bg.tusofia.pmu.museumhunt.base.fragment.BaseFragment
import bg.tusofia.pmu.museumhunt.core.dialog.InputDialogBuilder
import bg.tusofia.pmu.museumhunt.databinding.FragmentBrowseGamesBinding
import bg.tusofia.pmu.museumhunt.ingame.IngameArgs
import bg.tusofia.pmu.museumhunt.ingame.init.ContinueLevelInput

class BrowseGamesFragment : BaseFragment<FragmentBrowseGamesBinding, BrowseGamesViewModel>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentBrowseGamesBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding { vm ->
            viewModel = vm

            val adapter = GamesAdapter(vm::onItemClick)

            vm.gamesLiveData.observe(viewLifecycleOwner, adapter.dataObserver)

            rvGames.setHasFixedSize(true)
            rvGames.layoutManager = LinearLayoutManager(context)
            rvGames.adapter = adapter

        }

        viewModel.apply {
            continueLevelEvent.observe(Observer { (gameId, levelId) ->
                val ingameArgs = IngameArgs(gameId, levelId)
                val homeInput = ContinueLevelInput(ingameArgs)
                val directions = BrowseGamesFragmentDirections.actionBrowseGamesFragmentToHome(homeInput)

                findNavController().navigate(directions)
            })

            requireGameNameEvent.observe(Observer {
                context?.let { context ->
                    val dialogView = View.inflate(context, R.layout.dialog_new_game, null)
                    InputDialogBuilder(context, R.style.AppTheme_Dialog_InGame)
                        .setView(dialogView)
                        .setPositiveButton(R.string.create, callback = { text, _, _ ->
                            viewModel.onCreateGameWithName(text)
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show()
                }
            })
        }
    }

    override fun instantiateViewModel(): BrowseGamesViewModel =
        ViewModelProviders.of(this, viewModelFactory)[BrowseGamesViewModel::class.java]
}

