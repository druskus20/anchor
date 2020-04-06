package es.uam.eps.dadm.cards

import Deck
import androidx.lifecycle.ViewModel

class DeckListViewModel: ViewModel()  {
    val decks = mutableListOf<Deck>()

    init {
        for (i in 0 until 100)
            decks += Deck(name="DECK NAME")
    }
}

