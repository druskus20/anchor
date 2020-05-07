package es.uam.eps.dadm.cardspedroburgos

import android.util.Log
import androidx.lifecycle.ViewModel



class CardShowViewModel : ViewModel() {
    private val TAG : String = "CardShowViewModel"
    var studyCardList = mutableListOf<Card>()
    var currentCardCount = 0
    var end = false
    var answered = false

    init {
        Log.d(TAG, "CardsViewModel created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "CardsViewModel is about to be destroyed")
    }
}