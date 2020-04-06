package es.uam.eps.dadm.cards

import androidx.lifecycle.ViewModel

class CardListViewModel: ViewModel() {

    val cards = mutableListOf<Card>()

    init {
        for (i in 0 until 100)
            cards += Card(question = "es.uam.eps.dadm.cards.Card $i", answer = "Answer to question $i")
    }
}