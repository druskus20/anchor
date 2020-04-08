package es.uam.eps.dadm.cards

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_card_add.*


class CardAddFragment : Fragment() {

    private var card = Card(question = "none", answer = "none")

    private val mainViewModel by lazy {
        // !!!!!! Forzado
        activity?.let { ViewModelProviders.of(it) }!![MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //card = Card("Question", "Answer") // !!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Call activity method to show fab ---> CANT BE IN onCreate because of activity destroy on rotation
        //(activity as MainActivity).hideAddButton()
        return inflater.inflate(R.layout.fragment_card_add, container, false)
    }

    override fun onStart() {
        super.onStart()

        val questionTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                card.question = s.toString()
            }
        }

        val answerTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                card.answer = s.toString()
            }
        }

        // add_card_button
        add_card_button.setOnClickListener {
            view?.let { it ->
                mainViewModel.activeDeck?.addCard(card)
                Snackbar.make(it, "TEXTO AÑADIR CARTA", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }

            // !!! If there is stack, go back, if not, render the fragment and add it to the stack
            //  so we wont go back to the Add fragment
            activity?.supportFragmentManager?.apply {
                if (this.backStackEntryCount > 0) {
                    this.popBackStack();
                }
                else {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.fragment_container, CardListFragment.newInstance())
                        // !!! GO BACK IN THE STACK
                        ?.addToBackStack("CardList")
                        ?.commit()
                }
            }



        }


        question_edit_text.addTextChangedListener(questionTextWatcher)
        answer_edit_text.addTextChangedListener(answerTextWatcher)
        uuid_label_text_view.text = "TARJETA " + card.id.substring(9, 13).toUpperCase()
        date_text_view.text = card.date.substring(0, 16)
    }

    companion object {
        fun newInstance(): CardAddFragment {
            return CardAddFragment()
        }
    }
}