package bg.tusofia.pmu.museumhunt.ingame.riddle

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import bg.tusofia.pmu.museumhunt.databinding.LayoutRiddleAnswerBinding
import bg.tusofia.pmu.museumhunt.domain.repository.Answer

class AnswersAdapter(private val answerObserver: Observer<Answer>) : RecyclerView.Adapter<AnswersAdapter.AnswerVH>(), Observer<List<Answer>> {

    init {
        setHasStableIds(true)
    }

    private var answers: List<Answer> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onChanged(data: List<Answer>) {
        answers = data
    }

    override fun getItemCount(): Int = answers.size

    override fun getItemId(position: Int): Long {
        return answers[position].hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerVH {
        val binding = LayoutRiddleAnswerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnswerVH(binding, answerObserver)
    }

    override fun onBindViewHolder(holder: AnswerVH, position: Int) {
        holder.bind(answers[position])
    }

    class AnswerVH(binding: LayoutRiddleAnswerBinding, answerObserver: Observer<Answer>) : RecyclerView.ViewHolder(binding.root) {

        private val viewModel = AnswerItemViewModel(answerObserver)

        init {
            binding.viewModel = viewModel
        }

        fun bind(answer: Answer) {
            viewModel.bind(answer)
        }

    }

}