package es.uam.eps.dadm.cards

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_card_add.*
import kotlinx.android.synthetic.main.fragment_card_show.*
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.days
import kotlin.time.minutes


class CardShowFragment() : Fragment() {
    private var currentCard = Card("None", "None")
    var today = 1 //


    // Specific viewModel for hiding elements
    private val cardShowViewModel: CardShowViewModel by lazy {
        ViewModelProviders.of(this).get(CardShowViewModel::class.java)
    }

    private val mainViewModel by lazy {
        // !!!!!! Forzado
        activity?.let { ViewModelProviders.of(it) }!![MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Creates the list with the cards to study
        mainViewModel.activeDeck.cards.forEach {
            if (it.nextPracticeDate == today) {
                cardShowViewModel.studyCardList.add(it)

            }


        }
    }


        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Call activity method to show fab ---> CANT BE IN onCreate because of activity destroy on rotation
            //(activity as MainActivity).hideAddButton()
            super.onCreateView(inflater, container, savedInstanceState)
            var fragment = inflater.inflate(R.layout.fragment_card_show, container, false)


            return fragment
        }




    override fun onStart() {
        super.onStart()
        // If there are no more cards to study
        if (cardShowViewModel.studyCardList.size == 0){
            view?.let {
                Snackbar.make(it, getString(R.string.no_cards_study_msg), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
            activity?.supportFragmentManager?.popBackStack();
        }
        nextCard()

        question_text_view.text = currentCard.question
        answer_text_view.text = currentCard.answer


        if (cardShowViewModel.answered == true) {
            answer_button.visibility = View.INVISIBLE

            difficulty_buttons.visibility = View.VISIBLE
            answer_text_view.visibility = View.VISIBLE
        }

        answer_button.setOnClickListener {
            cardShowViewModel.answered = true
            answer_button.visibility = View.INVISIBLE
            difficulty_buttons.visibility = View.VISIBLE
            answer_text_view.visibility = View.VISIBLE

        }

        easy_button.setOnClickListener {
            currentCard.quality = Quality.FACIL
            currentCard.update()
            // If there are no more cards left
            testEndSession()
            nextCard()
        }
        doubt_button.setOnClickListener {
            currentCard.quality = Quality.DUDO
            currentCard.update()
            // If there are no more cards left
            testEndSession()
            nextCard()
        }
        hard_button.setOnClickListener {
            currentCard.quality = Quality.DIFICIL
            currentCard.update()
            // If there are no more cards left
            testEndSession()
            nextCard()
        }
    }

    fun testEndSession(){
        activity?.supportFragmentManager?.apply {
            if (cardShowViewModel.currentCardCount == cardShowViewModel.studyCardList.size)
                this.popBackStack();
        }
    }
    // Para usar localdatenow
    fun nextCard() {

/*
       // UPDATE TEST
        currentCard.question = " AAAA " + cardShowViewModel.currentCardCount
        currentCard.answer = "BBBB " + cardShowViewModel.currentCardCount
*/
        if (cardShowViewModel.studyCardList.size <= cardShowViewModel.currentCardCount)
            return
        else if (cardShowViewModel.studyCardList.size > 0)
            currentCard = cardShowViewModel.studyCardList[cardShowViewModel.currentCardCount]


        // Reload UI
        answer_button.visibility = View.VISIBLE

        question_text_view.text = currentCard.question
        answer_text_view.text = currentCard.answer


        difficulty_buttons.visibility = View.INVISIBLE
        answer_text_view.visibility = View.INVISIBLE
        cardShowViewModel.answered = false
        card_count.text = getString(R.string.show_card_cards_left) + cardShowViewModel.currentCardCount + "/" + cardShowViewModel.studyCardList.size
        cardShowViewModel.currentCardCount++
    }

    companion object {
        fun newInstance(): CardShowFragment {
            return CardShowFragment()
        }
    }
}

