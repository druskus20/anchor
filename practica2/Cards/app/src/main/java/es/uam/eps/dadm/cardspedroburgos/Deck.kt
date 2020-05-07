package es.uam.eps.dadm.cardspedroburgos

import java.io.Serializable
import java.util.*

open class Deck (var name: String, val id: String = UUID.randomUUID().toString()) : Serializable {
    var cards: MutableList<Card> = mutableListOf()

    var numCards = 0

    // We could add categories at some point
    // We could add a % rate of difficultness


    // user command functions ------------------------------------
    fun addCard(card: Card) {
        // We should create the card outside the deck if we want to
        //  reuse the same card
        cards.add(card)
        numCards++
    }

    fun  removeCardById(id: String): Int {
        cards.forEachIndexed{ index, element ->
            if (element.id == id) {
                cards.removeAt(index)
                return index
            }
        }
        throw Exception("Card not found")   // !!!
    }
}

