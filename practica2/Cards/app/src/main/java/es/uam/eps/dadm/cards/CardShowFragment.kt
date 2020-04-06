package es.uam.eps.dadm.cards

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_card_add.*


class CardShowFragment : Fragment() {
    private lateinit var card: Card



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        card = Card("Question", "Answer")
        // Call activity method to hide fab
        (activity as MainActivity).hideAddButton()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_card_show, container, false)
    }

    companion object {
        fun newInstance(): CardShowFragment {
            return CardShowFragment()
        }
    }
}