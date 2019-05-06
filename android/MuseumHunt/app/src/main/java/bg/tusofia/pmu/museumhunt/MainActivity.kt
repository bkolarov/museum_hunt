package bg.tusofia.pmu.museumhunt

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bg.tusofia.pmu.museumhunt.base.activity.BaseActivity
import bg.tusofia.pmu.museumhunt.databinding.ActivityMainBinding
import bg.tusofia.pmu.museumhunt.ingame.IngameActivity

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        binding.viewModel = viewModel

        viewModel.newGameLiveEvent.observe(this, Observer { gameId ->
            gameId?.let {
                val intent = Intent(this, IngameActivity::class.java)
                intent.putExtra(IngameActivity.KEY_GAME_ID, it)
                startActivity(intent)
            }
        })

        viewModel.continueGameLiveEvent.observe(this, Observer {

        })

        viewModel.browseGamesLiveEvent.observe(this, Observer {

        })
    }

    override fun instantiateViewModel(): MainActivityViewModel =
        ViewModelProviders.of(this, viewModeFactory)[MainActivityViewModel::class.java]

}
