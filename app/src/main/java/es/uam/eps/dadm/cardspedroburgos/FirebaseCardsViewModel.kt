package es.uam.eps.dadm.cardspedroburgos
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FirebaseCardsViewModel : ViewModel() {
    private lateinit var referenceName : String
    var cards: MutableLiveData<List<Card>> = MutableLiveData()
        private set
        get() {
            if (field.value == null) {
                FirebaseDatabase.getInstance().getReference(referenceName)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {}
                        override fun onDataChange(p0: DataSnapshot) {
                            val listOfCards: MutableList<Card> = mutableListOf()
                            for (card in p0.children) {
                                val newCard = card.getValue(Card::class.java)
                                if (newCard != null)
                                    listOfCards.add(newCard)
                            }
                            field.postValue(listOfCards)
                        }
                    })
            }
            return field
        }

    fun setReference(deck_reference_name: String) {
        referenceName = deck_reference_name
    }




}