package bg.tusofia.pmu.museumhunt

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bg.tusofia.pmu.museumhunt.base.activity.BaseActivity
import bg.tusofia.pmu.museumhunt.core.dialog.InputDialogBuilder
import bg.tusofia.pmu.museumhunt.databinding.ActivityMainBinding
import bg.tusofia.pmu.museumhunt.ingame.IngameActivity
import bg.tusofia.pmu.museumhunt.ingame.IngameArgs
import bg.tusofia.pmu.museumhunt.ingame.init.ContinueGameInput
import bg.tusofia.pmu.museumhunt.ingame.init.ContinueLevelInput

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        binding.viewModel = viewModel

        viewModel.newGameLiveEvent.observe(this, Observer { (gameId, levelId) ->
            val intent = Intent(this, IngameActivity::class.java)
            intent.putExtra(IngameActivity.KEY_HOME_INPUT, ContinueLevelInput(IngameArgs(gameId, levelId)))
            startActivity(intent)
        })

        viewModel.continueGameLiveEvent.observe(this, Observer {
            val intent = Intent(this, IngameActivity::class.java)
            intent.putExtra(IngameActivity.KEY_HOME_INPUT, ContinueGameInput)
            startActivity(intent)
        })
        viewModel.requireGameNameEvent.observe(this, Observer {
            val dialogView = View.inflate(this, R.layout.dialog_new_game, null)
            InputDialogBuilder(this, R.style.AppTheme_Dialog_InGame)
                .setView(dialogView)
                .setPositiveButton(R.string.create, callback = { text, _, _ ->
                    viewModel.onCreateGameWithName(text)
                })
                .setNegativeButton(R.string.cancel, null)
                .show()
        })

    }

    override fun instantiateViewModel(): MainActivityViewModel =
        ViewModelProviders.of(this, viewModeFactory)[MainActivityViewModel::class.java]

}
