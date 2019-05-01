package bg.tusofia.pmu.museumhunt.ingame

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import bg.tusofia.pmu.museumhunt.R
import bg.tusofia.pmu.museumhunt.base.activity.BaseActivity
import bg.tusofia.pmu.museumhunt.databinding.ActivityIngameBinding
import bg.tusofia.pmu.museumhunt.ingame.init.IngameHomeDestinationInput

class IngameActivity : BaseActivity<ActivityIngameBinding, IngameViewModel>() {

    companion object {
        const val KEY_GAME_ID = "game-id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityIngameBinding>(this, R.layout.activity_ingame)

        val navController = findNavController(R.id.nav_host_fragment)

        navController.setGraph(R.navigation.ingame_navigation, Bundle().apply {
            putParcelable("homeInput", IngameHomeDestinationInput(getInt(KEY_GAME_ID)))
        })
    }

    override fun instantiateViewModel(): IngameViewModel =
        ViewModelProviders.of(this, viewModeFactory)[IngameViewModel::class.java]

}