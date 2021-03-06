package es.uam.eps.dadm.cardspedroburgos

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import es.uam.eps.dadm.cardspedroburgos.ui.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_card_list.*
import org.joda.time.DateTime


class CardListFragment : Fragment() {
    private lateinit var cardRecyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter

    // !!!
    var listener: OnCardListFragmentInteractionListener? = null

    private val mainViewModel by lazy {

        activity?.let { ViewModelProviders.of(it) }!![MainViewModel::class.java]
    }


    private val firebaseCardsViewModel by lazy {
        //activity?.let { ViewModelProviders.of(it) }!![FirebaseCardsViewModel::class.java]
        ViewModelProviders.of(this).get(FirebaseCardsViewModel::class.java)
    }

    private val referencePath by lazy {
        val user = activity?.applicationContext?.let { SettingsActivity.getLoggedUser(it) }
        val deckid = mainViewModel.activeDeck.id
         "$user/Decks/$deckid"
    }

    private val databaseReference by lazy {
        FirebaseDatabase.getInstance().getReference(referencePath)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    // Menu bar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.card_list_menu , menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings_button -> {
                // Show stats menu
                val intent = Intent(activity, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.stats_button -> {
                listener?.onDeckStats()
                true
            }
            R.id.logout_button -> {
                val intent = Intent(activity, LoginActivity::class.java)
                activity?.let {
                    SettingsActivity.setRememberMe(it.applicationContext, false)
                    startActivity(intent)
                    it.finish()
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Call activity method to show fab ---> CANT BE IN onCreate because of activity destroy on rotation
        //(activity as MainActivity).showAddButton()
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_card_list, container, false)

        cardRecyclerView = view.findViewById(R.id.card_recycler_view) as RecyclerView
        cardRecyclerView.layoutManager = LinearLayoutManager(activity)
       // mainViewModel.activeDeck.cards.forEach{ it.expanded = false }


        val observer =
            Observer<List<Card>> { t ->
                if (t != null){
                    updateUI(t)
                }
            }
        firebaseCardsViewModel.setReference("$referencePath/Cards")
        firebaseCardsViewModel.cards.observe(this, observer)


        return view
    }

    override fun onStart() {
        super.onStart()

        title_label.text = getString(R.string.card_list_title) + ": " +  mainViewModel.activeDeck.name
        mainViewModel.actionbarTitle.value = getString(R.string.app_name) + ": " + title_label.text

        // Listener for the round "+" button
        fab.setOnClickListener { view ->
            // addCard()
            //listener = context as onCardListFragmentInteractionListener?
            listener?.onCardAdd()
        }
        study_button.setOnClickListener{
            listener?.onBeginStudy()
        }


    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnCardListFragmentInteractionListener?
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnCardListFragmentInteractionListener {
        fun onCardAdd()
        fun onBeginStudy()
        fun onEditCard()
        fun onDeckStats()
    }


    private fun updateUI(cards: List<Card>) {
        cardAdapter = CardAdapter(cards)
        cardRecyclerView.adapter = cardAdapter
    }




    companion object {
        fun newInstance(): CardListFragment {
            return CardListFragment()
        }
    }

    private inner class CardHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var card: Card
        val questionTextView: TextView = itemView.findViewById(R.id.list_item_question)
        val answerTextView:TextView = itemView.findViewById(R.id.list_item_answer)
        val dateTextView: TextView = itemView.findViewById(R.id.list_item_date)
        val scoreView: TextView = itemView.findViewById(R.id.list_item_score)

        init {
            itemView.setOnClickListener {
                mainViewModel.activeCard = card
                card.expanded = !card.expanded
                cardAdapter.notifyItemChanged(adapterPosition)
            }

            // Alert dialog for deleting the Card
            itemView.setOnLongClickListener {
                mainViewModel.activeCard = card
                // Easy way to show a menu without making a new class
                showOptionsMenu(view)
                true
            }
        }

        // Easy way to show a menu without making a new class
        private fun showOptionsMenu(view: View) {
            val builder: AlertDialog.Builder? = activity?.let {
                AlertDialog.Builder(it)
            }
            val options = arrayOf(getString(R.string.card_edit_option), getString(R.string.card_delete_option))
            builder?.apply {
                setItems(options
                ) { _, which ->
                    when (which) {
                        0 -> listener?.onEditCard()
                        1 -> showDeleteMenu(view)
                    }
                }
                setNegativeButton("CANCEL", null)
            }
            // Set other dialog properties
            builder?.create()?.show()
        }
        private fun showDeleteMenu(view: View) {
            val builder: AlertDialog.Builder? = activity?.let {
                AlertDialog.Builder(it)
            }
            builder?.apply {
                setTitle(getString(R.string.card_delete_title))
                setMessage(getString(R.string.card_delete_question))
                setPositiveButton(getString(R.string.ok_button)
                ) { dialog, id ->
                    databaseReference.child("Cards").child(card.id).removeValue()
                    mainViewModel.activeDeck.numCards--


                    databaseReference.child("numCards").setValue(mainViewModel.activeDeck.numCards)

                    cardAdapter.notifyItemChanged(adapterPosition)
                    cardAdapter.notifyItemRangeRemoved(adapterPosition, 1)
                    Snackbar.make(view, getString(R.string.card_delete_msg), Snackbar.LENGTH_LONG)
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

        fun bind(card: Card) {
            this.card = card

            val expanded = card.expanded

            questionTextView.text = card.question
            answerTextView.text = card.answer
            dateTextView.text = card.date
            scoreView.text = DateTime(card.nextPracticeDate).toLocalDate().toString()
            answerTextView.visibility = if (expanded) View.VISIBLE else View.GONE
            scoreView.visibility = if (expanded) View.VISIBLE else View.GONE
        }
    }

    private inner class CardAdapter(val cards : List<Card>) : RecyclerView.Adapter<CardHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
            val view = layoutInflater.inflate(R.layout.list_item_card, parent, false)
            return CardHolder(view)
        }

        override fun getItemCount() = cards.size
        override fun onBindViewHolder(holder: CardHolder, position: Int) {
            holder.bind(cards[position])

        }
    }


}
