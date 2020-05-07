package es.uam.eps.dadm.cardspedroburgos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders

class DeckStatsFragment : Fragment() {
    private var card = Card(question = "none", answer = "none")
    private val mainViewModel by lazy {
        activity?.let { ViewModelProviders.of(it) }!![MainViewModel::class.java]
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_deck_stats, container, false)
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.actionbarTitle.value=getString(R.string.app_name) + ": " + getString(R.string.card_add_title)

    }

    companion object {
        fun newInstance(): DeckStatsFragment {
            return DeckStatsFragment()
        }
    }
}
