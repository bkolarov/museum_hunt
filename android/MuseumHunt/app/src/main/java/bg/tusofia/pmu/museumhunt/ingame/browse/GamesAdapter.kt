package bg.tusofia.pmu.museumhunt.ingame.browse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import bg.tusofia.pmu.museumhunt.databinding.LayoutItemGameBinding
import bg.tusofia.pmu.museumhunt.domain.db.entity.Game

class GamesAdapter(private val onItemClick: (Game) -> Unit) : RecyclerView.Adapter<GamesAdapter.GameViewHolder>() {

    private var data = emptyList<Game>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    val dataObserver = Observer<List<Game>> {
        data = it
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GameViewHolder(LayoutItemGameBinding.inflate(inflater, parent, false), onItemClick)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(data[position])
    }

    class GameViewHolder(private val binding: LayoutItemGameBinding, onItemClick: (Game) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        private val viewModel = GameItemViewModel(onItemClick)

        init {
            binding.viewModel = viewModel
        }

        fun bind(game: Game) {
            viewModel.bind(game)
            binding.executePendingBindings()
        }

    }

}