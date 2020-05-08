package es.uam.eps.dadm.cardspedroburgos

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
import kotlinx.android.synthetic.main.fragment_card_add.answer_edit_text
import kotlinx.android.synthetic.main.fragment_card_add.date_text_view
import kotlinx.android.synthetic.main.fragment_card_add.question_edit_text
import kotlinx.android.synthetic.main.fragment_card_add.uuid_label_text_view
import kotlinx.android.synthetic.main.fragment_card_edit.*


class CardEditFragment : Fragment() {

    private lateinit var card : Card

    private val mainViewModel by lazy {
        activity?.let { ViewModelProviders.of(it) }!![MainViewModel::class.java]
    }

    private val cardEditViewModel by lazy {
        ViewModelProviders.of(this).get(CardEditViewModel::class.java)
    }

    private val referencePath by lazy {
        val user = activity?.applicationContext?.let { SettingsActivity.getLoggedUser(it) }
        val deckid = mainViewModel.activeDeck.id
        val cardid = mainViewModel.activeCard.id

        "$user/Decks/$deckid/Cards/$cardid"
    }

    private val databaseReference by lazy {
        FirebaseDatabase.getInstance().getReference(referencePath)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Call activity method to show fab ---> CANT BE IN onCreate because of activity destroy on rotation
        //(activity as MainActivity).hideAddButton()
        return inflater.inflate(R.layout.fragment_card_edit, container, false)
    }



    override fun onStart() {
        super.onStart()


        card = mainViewModel.activeCard

        mainViewModel.actionbarTitle.value=getString(R.string.app_name) + ": " + getString(R.string.edit_card_title)


        question_edit_title.text = getString(R.string.question) + ": " + card.question
        answer_edit_title.text = getString(R.string.answer) + ": " + card.answer
        val questionTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                cardEditViewModel.question_text = s.toString()
            //    card.question = s.toString()
            }
        }

        val answerTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                cardEditViewModel.answer_text = s.toString()
            //    card.answer = s.toString()
            }
        }

        // finish changes button
        finish_edit_button.setOnClickListener {

            if (cardEditViewModel.question_text != "")
                databaseReference.child("question").setValue(cardEditViewModel.question_text)
            if (cardEditViewModel.answer_text != "")
                databaseReference.child("answer").setValue(cardEditViewModel.answer_text)
            // !!! If there is stack, go back, if not, render the fragment and add it to the stack
            //  so we wont go back to the Add fragment
            activity?.supportFragmentManager?.apply {
                view?.let { it ->
                    Snackbar.make(it, getString(R.string.edit_card_msg), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                }
                if (this.backStackEntryCount > 0) {

                    this.popBackStack()

                }
                else {
                     activity?.supportFragmentManager?.apply {
                        if (this.backStackEntryCount > 0) {
                            this.popBackStack()
                        }
                    }
                }
            }
        }

        question_edit_text.addTextChangedListener(questionTextWatcher)
        answer_edit_text.addTextChangedListener(answerTextWatcher)
        uuid_label_text_view.text = getString(R.string.edit_card_title) + ": " + card.id.substring(9, 13).toUpperCase()
        date_text_view.text = card.date
    }

    companion object {
        fun newInstance(): CardEditFragment {
            return CardEditFragment()
        }
    }
}