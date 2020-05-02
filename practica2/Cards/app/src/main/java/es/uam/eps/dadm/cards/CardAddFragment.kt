package es.uam.eps.dadm.cards

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_card_add.*


class CardAddFragment : Fragment() {

    private var card = Card(question = "none", answer = "none")
    var listener: onCardAddFragmentInteractionListener? = null
    private val mainViewModel by lazy {
        activity?.let { ViewModelProviders.of(it) }!![MainViewModel::class.java]
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as onCardAddFragmentInteractionListener?
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface onCardAddFragmentInteractionListener {
        fun onCardAddNoBackStack()
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
        mainViewModel.actionbarTitle.value=getString(R.string.app_name) + ": " + getString(R.string.card_add_title)

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
                if (card.question == "none" || card.answer == "none"){
                    Snackbar.make(it, "CAMPOS INVALIDOS", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                    return@setOnClickListener
                }
                mainViewModel.activeDeck.addCard(card)
                Snackbar.make(it, "TEXTO AÑADIR CARTA", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
            
            // !!! If there is stack, go back, if not, render the fragment and add it to the stack
            //  so we wont go back to the Add fragment
            activity?.supportFragmentManager?.apply {
                if (this.backStackEntryCount > 0) {
                    this.popBackStack()
                }
                else {
                    listener?.onCardAddNoBackStack()
                }
            }
        }

        question_edit_text.addTextChangedListener(questionTextWatcher)
        answer_edit_text.addTextChangedListener(answerTextWatcher)
        uuid_label_text_view.text = getString(R.string.card_add_title) + ": " + card.id.substring(9, 13).toUpperCase()
        date_text_view.text = card.date
    }

    companion object {
        fun newInstance(): CardAddFragment {
            return CardAddFragment()
        }
    }
}