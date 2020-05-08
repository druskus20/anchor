package es.uam.eps.dadm.cardspedroburgos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_deck_stats.*

class DeckStatsFragment : Fragment() {
    private var card = Card(question = "none", answer = "none")


    private val mainViewModel by lazy {
        activity?.let { ViewModelProviders.of(it) }!![MainViewModel::class.java]
    }

    private val deckStatsViewModel by lazy {
        ViewModelProviders.of(this).get(DeckStatsViewModel::class.java)
    }

    private val referencePath by lazy {
        val user = activity?.applicationContext?.let { SettingsActivity.getLoggedUser(it) }
        val deckid = mainViewModel.activeDeck.id


        "$user/Decks"
    }

    private val databaseReference by lazy {
        FirebaseDatabase.getInstance().getReference(referencePath)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (deck in dataSnapshot.children) {
                    val d = deck.getValue(Deck::class.java)

                    if (d != null) {
                        if (d.id == mainViewModel.activeDeck.id) {
                            deckStatsViewModel.total_easy = d.total_easy
                            deckStatsViewModel.total_doubt = d.total_dudo
                            deckStatsViewModel.total_hard = d.total_hard

                            Log.d("DECKSTATS", d.total_hard.toString())
                            Log.d("DECKSTATS", d.total_dudo.toString())
                            Log.d("DECKSTATS", d.total_easy.toString())

                            updateUI()
                            break
                        }
                    }

                }
            }
            override fun onCancelled(databaseError: DatabaseError) { // ...
            }
        })
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

        uuid_label_text_view.text = getString(R.string.card_list_title) + ": " +  mainViewModel.activeDeck.name
        mainViewModel.actionbarTitle.value=getString(R.string.app_name) + ": " + getString(R.string.card_stats_label)

        updateUI()
    }

    fun updateUI() {
        hard_responses.text = deckStatsViewModel.total_hard.toString()
        easy_responses.text = deckStatsViewModel.total_easy.toString()
        doubt_responses.text = deckStatsViewModel.total_doubt.toString()
    }
    companion object {
        fun newInstance(): DeckStatsFragment {
            return DeckStatsFragment()
        }
    }
}
