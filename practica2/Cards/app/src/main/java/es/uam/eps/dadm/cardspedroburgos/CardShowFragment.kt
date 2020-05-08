package es.uam.eps.dadm.cardspedroburgos

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_card_show.*
import org.joda.time.DateTime
import org.joda.time.DateTimeComparator


class CardShowFragment : Fragment() {
    private var currentCard = Card("None", "None")
    var listener: OnCardShowFragmentInteractionListener? = null
    var totalCards = 0

    // Specific viewModel for hiding elements
    private val cardShowViewModel: CardShowViewModel by lazy {
        ViewModelProviders.of(this).get(CardShowViewModel::class.java)
    }

    private val mainViewModel by lazy {
        // !!!!!! Forzado
        activity?.let { ViewModelProviders.of(it) }!![MainViewModel::class.java]
    }


    private val referencePath by lazy {
        val user = activity?.applicationContext?.let { SettingsActivity.getLoggedUser(it) }
        val deckid = mainViewModel.activeDeck.id
        "$user/Decks/$deckid"
    }

    private val databaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("$referencePath/Cards")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var maxCards = 0


        // Smart cast fix
        var temp  = activity?.applicationContext?.let { SettingsActivity.getMaxTarjetas(it)?.toInt() }
        if (temp == null) {
            temp = 0
        }
        maxCards = temp

        // I hate asyncronous calls
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (cardShowViewModel.init)
                    return
                cardShowViewModel.init = true
                val listOfCards: MutableList<Card> = mutableListOf()
                val dateTimeComparator = DateTimeComparator.getDateOnlyInstance()
                for (card in dataSnapshot.children) {
                    val newCard = card.getValue(Card::class.java)
                    if (newCard != null) {
                        val diff = dateTimeComparator.compare(
                            DateTime(newCard.nextPracticeDate),
                            DateTime.now()
                        )
                        if ((diff <= 0) && (totalCards < maxCards)) {
                            totalCards++
                            listOfCards.add(newCard)
                        }
                    }
                }

                cardShowViewModel.studyCardList.addAll(listOfCards)
                totalCards = cardShowViewModel.studyCardList.size
                if (totalCards == 0){
                    view?.let {
                        Snackbar.make(it, getString(R.string.no_cards_study_msg), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    }
                    listener?.onEndStudy()
                }
               setFirstCard()
            }
            override fun onCancelled(databaseError: DatabaseError) { // ...
            }
        })

        /*
        if (cardShowViewModel.studyCardList.size == 0) {
            // Creates the list with the cards to study TODAY
            val dateTimeComparator = DateTimeComparator.getDateOnlyInstance()
            mainViewModel.activeDeck.cards.forEach {
                val diff = dateTimeComparator.compare(DateTime(it.nextPracticeDate), DateTime.now())
                if ((diff <= 0) && (total_cards < max_cards)) {
                    cardShowViewModel.studyCardList.add(it)
                    total_cards++
                }
            }
        }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Call activity method to show fab ---> CANT BE IN onCreate because of activity destroy on rotation
        //(activity as MainActivity).hideAddButton()
        super.onCreateView(inflater, container, savedInstanceState)




        return inflater.inflate(R.layout.fragment_card_show, container, false)
    }

    interface OnCardShowFragmentInteractionListener {
        fun onEndStudy()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnCardShowFragmentInteractionListener?
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onStart() {
        super.onStart()

        mainViewModel.actionbarTitle.value=getString(R.string.app_name) + ": " + getString(R.string.show_card_title)
        // If there are no more cards to study


        setFirstCard()

        if (cardShowViewModel.answered) {
            answer_button.visibility = View.INVISIBLE

            difficulty_buttons.visibility = View.VISIBLE
            answer_text_view.visibility = View.VISIBLE
        }

        answer_button.setOnClickListener {
            cardShowViewModel.answered = true
            answer_button.visibility = View.INVISIBLE
            difficulty_buttons.visibility = View.VISIBLE
            answer_text_view.visibility = View.VISIBLE

        }

        easy_button.setOnClickListener {
            currentCard.quality = Quality.FACIL
            firebaseCardUpdate(currentCard)
            // If there are no more cards left
            testEndSession()
            nextCard()
        }
        doubt_button.setOnClickListener {
            currentCard.quality = Quality.DUDO
            firebaseCardUpdate(currentCard)
            // If there are no more cards left
            testEndSession()
            nextCard()
        }
        hard_button.setOnClickListener {
            currentCard.quality = Quality.DIFICIL
            firebaseCardUpdate(currentCard)
            // If there are no more cards left
            testEndSession()
            nextCard()
        }
    }

    private fun testEndSession(){
        activity?.supportFragmentManager?.apply {
            if (cardShowViewModel.currentCardCount == cardShowViewModel.studyCardList.size - 1) {
                cardShowViewModel.end = true
                this.popBackStack()
            }
        }
    }

    private fun setFirstCard() {

        if (cardShowViewModel.studyCardList.size <= cardShowViewModel.currentCardCount)
            return

        currentCard = cardShowViewModel.studyCardList[cardShowViewModel.currentCardCount]
        question_text_view.text = currentCard.question
        answer_text_view.text = currentCard.answer
        card_count.text = getString(R.string.show_card_cards_left) + cardShowViewModel.currentCardCount + "/" + cardShowViewModel.studyCardList.size

    }
    // Para usar localdatenow
    @SuppressLint("SetTextI18n")
    private fun nextCard() {

        if (cardShowViewModel.end)
            return

        cardShowViewModel.currentCardCount++

        if (cardShowViewModel.studyCardList.size > 0)
            currentCard = cardShowViewModel.studyCardList[cardShowViewModel.currentCardCount]

        // Reload UI
        answer_button.visibility = View.VISIBLE
        question_text_view. text = currentCard.question
        answer_text_view.text = currentCard.answer

        difficulty_buttons.visibility = View.INVISIBLE
        answer_text_view.visibility = View.INVISIBLE
        cardShowViewModel.answered = false
        card_count.text = getString(R.string.show_card_cards_left) + (cardShowViewModel.currentCardCount) + "/" + cardShowViewModel.studyCardList.size
    }

    // Updates the card info based on the quality received
    private fun firebaseCardUpdate(card : Card){
        // Temporal copy that we push to the remote

        val currentDeck = mainViewModel.activeDeck
        currentCard.update()
        currentDeck.updateDeck(currentCard.quality)

        val cardReference = FirebaseDatabase.getInstance().getReference("$referencePath/Cards/${currentCard.id}")
        val deckReference = FirebaseDatabase.getInstance().getReference(referencePath)

        cardReference.child("quality").setValue(currentCard.quality)
        cardReference.child("easiness").setValue(currentCard.easiness)
        cardReference.child("repetitions").setValue(currentCard.repetitions)
        cardReference.child("interval").setValue(currentCard.interval)
        cardReference.child("nextPracticeDate").setValue(currentCard.nextPracticeDate)


        deckReference.child("total_easy").setValue(currentDeck.totalEasy)
        deckReference.child("total_dudo").setValue(currentDeck.totalDudo)
        deckReference.child("total_hard").setValue(currentDeck.totalHard)
        deckReference.child("total").setValue(currentDeck.total)

    }

    companion object {

        fun newInstance(): CardShowFragment {
            return  CardShowFragment()
        }
    }

}