package es.uam.eps.dadm.cardspedroburgos
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FirebaseDecksViewModel : ViewModel() {
    lateinit var reference_name : String
    var decks: MutableLiveData<List<Deck>> = MutableLiveData()
        private set
        get() {
            if (field.value == null) {
                FirebaseDatabase.getInstance().getReference(reference_name)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {}
                        override fun onDataChange(p0: DataSnapshot) {
                            var listOfDecks: MutableList<Deck> = mutableListOf()
                            for (deck in p0.children) {
                                var newDeck = deck.getValue(Deck::class.java)
                                if (newDeck != null)
                                    listOfDecks.add(newDeck)
                            }
                            field.postValue(listOfDecks)
                        }
                    })
            }
            return field
        }

    fun setReference(user_reference_name: String) {
        reference_name = user_reference_name
    }




}