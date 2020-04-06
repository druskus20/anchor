package es.uam.eps.dadm.cards

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar


class CardListFragment : Fragment() {
    private val TAG: String = "CardListFragment"
    private lateinit var cardRecyclerView: RecyclerView
    private lateinit var adapter: CardAdapter


    private val cardListViewModel by lazy {
        ViewModelProviders.of(this).get(CardListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "${cardListViewModel.cards.size} cards")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Call activity method to show fab ---> CANT BE IN onCreate because of activity destroy on rotation
        (activity as MainActivity).showAddButton()
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_card_list, container, false)

        cardRecyclerView = view.findViewById(R.id.card_recycler_view) as RecyclerView
        cardRecyclerView.layoutManager = LinearLayoutManager(activity)

        updateUI()

        return view
    }

    private inner class CardHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var card: Card
        val questionTextView: TextView = itemView.findViewById(R.id.list_item_question)
        val answerTextView:TextView = itemView.findViewById(R.id.list_item_answer)
        val dateTextView: TextView = itemView.findViewById(R.id.list_item_date)

        init {
            itemView.setOnClickListener {

                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.fragment_container, CardShowFragment.newInstance())
                    ?.commitNow()
            }

            // Alert dialog for deleting the Card
            itemView.setOnLongClickListener {
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
                            0 ->  activity?.supportFragmentManager
                                    ?.beginTransaction()
                                    ?.replace(R.id.fragment_container, CardAddFragment.newInstance())
                                    ?.commitNow()
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
            questionTextView.text = card.question
            answerTextView.text = card.answer
            dateTextView.text = card.date.substring(0,13)
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

    private fun updateUI() {
        adapter = CardAdapter(cardListViewModel.cards)
        cardRecyclerView.adapter = adapter
    }

    companion object {
        fun newInstance(): CardListFragment {
            return CardListFragment()
        }
    }
}
