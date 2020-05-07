package es.uam.eps.dadm.cardspedroburgos

import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FirebaseLiveData: LiveData<List<Card>>() {
    private val DATABASENAME = "tarjetas"
    override fun onActive() {
        super.onActive()

        val reference = FirebaseDatabase.getInstance().getReference(DATABASENAME)

        reference.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                var listOfCards: MutableList<Card> = mutableListOf<Card>()
                for (card in p0.children) {
                    var newCard = card.getValue(Card::class.java)
                    if (newCard != null)
                        listOfCards.add(newCard)
                }
                value = listOfCards
            }
        })
    }
}