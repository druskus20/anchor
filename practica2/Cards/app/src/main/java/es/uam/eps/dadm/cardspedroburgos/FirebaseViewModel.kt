package es.uam.eps.dadm.cardfire

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import es.uam.eps.dadm.cardspedroburgos.Card


private const val DATABASENAME = "tarjetas"

class FirebaseViewModel : ViewModel() {
    var cards: MutableLiveData<List<Card>> = MutableLiveData()
        private set
        get() {
            if (field.value == null) {
                FirebaseDatabase.getInstance().getReference(DATABASENAME)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {}

                        override fun onDataChange(p0: DataSnapshot) {
                            var listOfCards: MutableList<Card> = mutableListOf<Card>()
                            for (card in p0.children) {
                                var newCard = card.getValue(Card::class.java)
                                if (newCard != null)
                                    listOfCards.add(newCard)
                            }
                            field.postValue(listOfCards)
                        }
                    })
            }
            return field
        }
}


// firebaseViewModel.cards.observe(this, observer)