package es.uam.eps.dadm.cards

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_card_list.*
import kotlinx.android.synthetic.main.list_item_card.*


class CardListFragment : Fragment() {
    private val TAG: String = "CardListFragment"
    private lateinit var cardRecyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter


    private val mainViewModel by lazy {
        // !!!!!! Forzado
        activity?.let { ViewModelProviders.of(it) }!![MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        updateUI()

        return view
    }

    override fun onStart() {
        super.onStart()
        // Listener for the round "+" button
        fab.setOnClickListener { view ->
            addCard()
        }
    }

    fun addCard() {
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.fragment_container, CardAddFragment.newInstance())
            // ?.addToBackStack("Decks") !!!
            ?.addToBackStack( "CardAdd" )
            ?.commit()
    }

    private fun updateUI() {
        cardAdapter = CardAdapter(mainViewModel.activeDeck.cards)
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
        val scoreTextView: TextView = itemView.findViewById(R.id.list_item_score)



        init {
            itemView.setOnClickListener {
                mainViewModel.activeCard = card
                card.expanded = !card.expanded
                cardAdapter.notifyItemChanged(adapterPosition);
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
            val options = arrayOf("EDIT", "DELETE")
            builder?.apply {
                setTitle("")
                setItems(options,
                    DialogInterface.OnClickListener { _, which ->
                        when (which)
                        {
                            0 ->  Snackbar.make(view, "Cant edit cards yet", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show()/*activity?.supportFragmentManager
                                    ?.beginTransaction()
                                    ?.replace(R.id.fragment_container, CardAddFragment.newInstance(deckId))
                                    ?.addToBackStack("AddCard")
                                    ?.commit()*/
                            1 -> showDeleteMenu(view)
                        }
                })
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
                setMessage("DIALOG MSG")
                setTitle("TITLE")
                setPositiveButton("DELETE",
                    DialogInterface.OnClickListener { dialog, id ->
                        mainViewModel.activeDeck.removeCardById(card.id)
                        cardAdapter.notifyItemChanged(adapterPosition)
                        cardAdapter.notifyItemRangeRemoved(adapterPosition, 1)
                        Snackbar.make(view, "CARD HAS BEEN DELETED", Snackbar.LENGTH_LONG)
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

        fun bind(card: Card) {
            this.card = card

            var expanded = card.expanded


            questionTextView.text = card.question
            answerTextView.text = card.answer
            dateTextView.text = card.date.substring(0,13)
            scoreTextView.text = card.easiness.toString()

            answerTextView.visibility = if (expanded) View.VISIBLE else View.GONE
            scoreTextView.visibility = if (expanded) View.VISIBLE else View.GONE

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
