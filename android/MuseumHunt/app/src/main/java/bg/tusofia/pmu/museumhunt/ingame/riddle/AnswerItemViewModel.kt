package bg.tusofia.pmu.museumhunt.ingame.riddle

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.Observer
import bg.tusofia.pmu.museumhunt.domain.repository.Answer
import com.hadilq.liveevent.LiveEvent

class AnswerItemViewModel(clickObserver: Observer<Answer>) : BaseObservable() {

    private var answer: Answer? = null

    private val onAnswerClickEvent = LiveEvent<Answer>()

    @get:Bindable
    val text: String? get() = answer?.answer

    init {
        onAnswerClickEvent.observeForever(clickObserver)
    }

    fun bind(answer: Answer) {
        this.answer = answer
        notifyChange()
    }



    fun onClick() {
        answer?.let { onAnswerClickEvent.value = it }
    }

}