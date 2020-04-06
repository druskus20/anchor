package es.uam.eps.dadm.cards

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class DeckListFragment : Fragment(){
    private val TAG: String = "DeckListFragment"
    private lateinit var deckRecyclerView: RecyclerView
    private lateinit var deckAdapter: DeckAdapter


    private val deckListViewModel by lazy {
        ViewModelProviders.of(this).get(DeckListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "${deckListViewModel.decks.size} decks")


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Call activity method to show fab ---> CANT BE IN onCreate because of activity destroy on rotation
        (activity as MainActivity).showAddButton()
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_deck_list, container, false)

        deckRecyclerView = view.findViewById(R.id.deck_recycler_view) as RecyclerView
        deckRecyclerView.layoutManager = LinearLayoutManager(activity)

        updateUI()

        return view
    }


    private inner class DeckHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var deck: Deck
        val nameTextView: TextView = itemView.findViewById(R.id.list_item_deck_title)
        val numCardsTextView: TextView = itemView.findViewById(R.id.list_item_deck_numcards)

        init {
            // Click on the deck, replace fragment
            itemView.setOnClickListener {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.fragment_container, CardListFragment.newInstance())
                    // ?.addToBackStack("Decks") !!!
                    ?.commitNow()
            }

            // Alert dialog for deleting the Deck
            itemView.setOnLongClickListener {
                showDeleteMenu(view)
                true
            }
        }
        private fun showDeleteMenu(view: View) {
            val builder: AlertDialog.Builder? = activity?.let {
                AlertDialog.Builder(it)
            }
            builder?.apply {
                setMessage("DIALOG MSG")
                setTitle("TITLE")
                setPositiveButton("DELETE",
                    DialogInterface.OnClickListener { dialog, id ->
                        Snackbar.make(view, "DECK HAS BEEN DELETED", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    })
                setNegativeButton("CANCEL",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            }
            // Set other dialog properties
            builder?.create()?.show()
        }

        fun bind(deck: Deck) {
            this.deck = deck
            nameTextView.text = deck.name
            numCardsTextView.text = deck.numCards.toString()
        }
    }

    private inner class DeckAdapter(val decks : List<Deck>) : RecyclerView.Adapter<DeckHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckHolder {
            val view = layoutInflater.inflate(R.layout.list_item_deck, parent, false) // !!!
            return DeckHolder(view)
        }

        override fun getItemCount() = decks.size

        override fun onBindViewHolder(holder: DeckHolder, position: Int) {
            holder.bind(decks[position])

        }
    }

    private fun updateUI() {
        deckAdapter = DeckAdapter(deckListViewModel.decks)
        deckRecyclerView.adapter = deckAdapter
    }
}