package es.uam.eps.dadm.cards

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
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
import kotlinx.android.synthetic.main.fragment_card_show.*


class CardShowFragment : Fragment() {
    private lateinit var card: Card
    private val cardsViewModel: CardsViewModel by lazy {
        ViewModelProviders.of(this).get(CardsViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        card = Card("Question", "Answer")



    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Call activity method to show fab ---> CANT BE IN onCreate because of activity destroy on rotation
        (activity as MainActivity).hideAddButton()
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_card_show, container, false)
    }


    override fun onStart() {
        super.onStart()

        if (cardsViewModel.answered == true) {
            answer_button.visibility = View.INVISIBLE
            help_button.visibility = View.INVISIBLE
            difficulty_buttons.visibility = View.VISIBLE

        }

        answer_button.setOnClickListener {
            cardsViewModel.answered = true
            question_text_view.text = resources.getString(R.string.answer_text)
            answer_button.visibility = View.INVISIBLE
            help_button.visibility = View.INVISIBLE
            difficulty_buttons.visibility = View.VISIBLE

        }

        help_button.setOnClickListener {
            val builder: AlertDialog.Builder? = activity?.let {
                AlertDialog.Builder(it)
            }
            builder?.apply {
                setTitle("TITLE")
                setMessage("DIALOG MSG")
            }
            // Set other dialog properties
            builder?.create()?.show()
        }


    }


    companion object {
        fun newInstance(): CardShowFragment {
            return CardShowFragment()
        }
    }
}