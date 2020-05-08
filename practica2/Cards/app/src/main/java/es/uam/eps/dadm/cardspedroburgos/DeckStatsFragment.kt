package es.uam.eps.dadm.cardspedroburgos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_deck_stats.*

class DeckStatsFragment : Fragment() {

    lateinit var barDataSet : BarDataSet
    lateinit var barData : BarData

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
                            deckStatsViewModel.totalEasy = d.totalEasy
                            deckStatsViewModel.totalDoubt = d.totalDudo
                            deckStatsViewModel.totalHard = d.totalHard
                            deckStatsViewModel.totalCards = d.numCards
                            updateUI()
                            barDataSet = BarDataSet(getData(), getString(R.string.graph_label))
                            barDataSet.barBorderWidth = 0.9f
                            barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)

                            barData = BarData(barDataSet)
                            barChart.data = barData
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





        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        val classes =  arrayOf(getString(R.string.label_easy),getString(R.string.label_doubt),getString(R.string.label_hard))
        val formatter = IndexAxisValueFormatter(classes)
        xAxis.granularity = 1f
        xAxis.valueFormatter = formatter


        barChart.setFitBars(true)
        barChart.animateXY(5000, 5000)
        barChart.invalidate()

        updateUI()
    }

    private fun getData(): ArrayList<BarEntry> {
        val entries: ArrayList<BarEntry> = ArrayList()
        entries.add(BarEntry(0f, deckStatsViewModel.totalEasy.toFloat()))
        entries.add(BarEntry(1f, deckStatsViewModel.totalDoubt.toFloat()))
        entries.add(BarEntry(2f, deckStatsViewModel.totalHard.toFloat()))
        return entries
    }
    fun updateUI() {
        hard_responses.text = deckStatsViewModel.totalHard.toString()
        easy_responses.text = deckStatsViewModel.totalEasy.toString()
        doubt_responses.text = deckStatsViewModel.totalDoubt.toString()
        deck_stats_n_cards.text = deckStatsViewModel.totalCards.toString()
    }
    companion object {
        fun newInstance(): DeckStatsFragment {
            return DeckStatsFragment()
        }
    }
}


