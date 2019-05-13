package bg.tusofia.pmu.museumhunt.ingame.browse

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import bg.tusofia.pmu.museumhunt.domain.db.entity.Game

class GameItemViewModel(private val onClick: (Game) -> Unit) : BaseObservable() {

    private var game: Game? = null

    @get:Bindable
    val name
        get() = game?.name

    fun bind(game: Game) {
        this.game = game
        notifyChange()
    }

    fun onClick() {
        game?.let { onClick(it) }
    }

}