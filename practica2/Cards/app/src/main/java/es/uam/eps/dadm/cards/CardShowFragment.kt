package es.uam.eps.dadm.cards

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_card_add.*
import kotlinx.android.synthetic.main.fragment_card_show.*


class CardShowFragment() : Fragment() {
    private lateinit var card: Card
    private val cardShowViewModel: CardShowViewModel by lazy {
        ViewModelProviders.of(this).get(CardShowViewModel::class.java)
    }
    private val mainViewModel by lazy {
        // !!!!!! Forzado
        activity?.let { ViewModelProviders.of(it) }!![MainViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        card = mainViewModel.activeCard



    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Call activity method to show fab ---> CANT BE IN onCreate because of activity destroy on rotation
        //(activity as MainActivity).hideAddButton()
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_card_show, container, false)
    }


    override fun onStart() {
        super.onStart()
        question_text_view.text = card.question
        answer_text_view.text = card.answer

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



    }


    companion object {
        fun newInstance(): CardShowFragment {
            return CardShowFragment()
        }
    }
}