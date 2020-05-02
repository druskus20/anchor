package es.uam.eps.dadm.cards

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import es.uam.eps.dadm.cards.ui.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_deck_list.*

class DeckListFragment : Fragment(){
    private lateinit var deckRecyclerView: RecyclerView
    private lateinit var deckAdapter: DeckAdapter
    var listener: onDeckListFragmentInteractionListener? = null

    private val mainViewModel by lazy {
        // !!!!!! Forzado
        activity?.let { ViewModelProviders.of(it) }!![MainViewModel::class.java]

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Call activity method to show fab ---> CANT BE IN onCreate because of activity destroy on rotation
        // (activity as MainActivity).showAddButton()
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_deck_list, container, false)

        deckRecyclerView = view.findViewById(R.id.deck_recycler_view) as RecyclerView
        deckRecyclerView.layoutManager = LinearLayoutManager(activity)

        updateUI()

        return view
    }

    override fun onStart() {
        super.onStart()

        mainViewModel.actionbarTitle.value=getString(R.string.app_name) + ": " + getString(R.string.deck_list_title)
        // Listener for the round "+" button
        fab.setOnClickListener { view ->
            addDeck(view)
        }
    }

    private fun addDeck(view: View) {

        val alert: AlertDialog.Builder? = activity.let {
            AlertDialog.Builder(it)
        }
        alert?.setTitle(getString(R.string.deck_add_title))
        alert?.setMessage(getString(R.string.deck_add_subt))

        // Set an EditText view to get user input
        val input = EditText(activity)
        alert?.setView(input)

        alert?.setPositiveButton(getString(R.string.ok_button)
        ) { dialog, whichButton ->
            val name = input.text.toString()
            mainViewModel.addDeck(name)
            deckAdapter.notifyDataSetChanged()
            Snackbar.make(view, getString(R.string.deck_add_msg), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        alert?.setNegativeButton(getString(R.string.cancel_button)
        ) { dialog, which ->
        }

        // Set other dialog properties
        alert?.create()?.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.std_menu , menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings_button -> {
                // Show stats menu
                val intent = Intent(activity, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.logout_button -> {
                val intent = Intent(activity, LoginActivity::class.java)
                activity?.finish()
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as onDeckListFragmentInteractionListener?
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface onDeckListFragmentInteractionListener {
        fun onDeckSelected()
    }

    private inner class DeckHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var deck: Deck
        val nameTextView: TextView = itemView.findViewById(R.id.list_item_deck_title)
        val numCardsTextView: TextView = itemView.findViewById(R.id.list_item_deck_numcards)

        init {
            // View a deck

            itemView.setOnClickListener {
                mainViewModel.activeDeck = deck
                listener?.onDeckSelected()

            }

            // Delete a deck
            itemView.setOnLongClickListener {
                mainViewModel.activeDeck = deck
                showDeleteMenu(view)

                true
            }
        }

        private fun showDeleteMenu(view: View) {

            val builder: AlertDialog.Builder? = activity?.let {
                AlertDialog.Builder(it)
            }
            builder?.apply {
                setMessage(getString(R.string.deck_delete_question))
                setTitle(getString(R.string.deck_delete_title))
                setPositiveButton(getString(R.string.delete_button)
                ) { dialog, id ->
                    // Delete

                    mainViewModel.removeActiveDeck()
                    deckAdapter.notifyItemChanged(adapterPosition)
                    deckAdapter.notifyItemRangeRemoved(adapterPosition, 1)
                    // Show Feedback
                    Snackbar.make(view,  getString(R.string.deck_delete_msg), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                }
                setNegativeButton(getString(R.string.cancel_button)
                ) { dialog, id ->
                    // User cancelled the dialog
                }
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
            val view = layoutInflater.inflate(R.layout.list_item_deck, parent, false)
            return DeckHolder(view)
        }

        override fun getItemCount() = decks.size

        override fun onBindViewHolder(holder: DeckHolder, position: Int) {
            holder.bind(decks[position])

        }
    }

    private fun updateUI() {
        deckAdapter = DeckAdapter(mainViewModel.decks)
        deckRecyclerView.adapter = deckAdapter
    }
}