package es.uam.eps.dadm.cards

import kotlin.random.Random
import java.io.Serializable
import java.util.*

open class Deck (var name: String, val id: String = UUID.randomUUID().toString()) : Serializable {
    var cards: MutableList<Card> = mutableListOf()

    var numCards = 0

    // We could add categories at some point
    // We could add a % rate of difficultness


    companion object {
        // Asks for a title and creates an empty deck
        fun readDeck(): Deck {
            println("Nueva Baraja:")
            print("Introduzca el nombre de la baraja: ")
            var name: String?
            do {
                name = readLine()
                if (name.isNullOrEmpty()) {
                    println("Error, introduzca el titulo de nuevo")
                    name = null
                }
            } while (name == null)
            return Deck(name)
        }
    }





    // user command functions ------------------------------------
    fun addCard(card: Card) {
        // We should create the card outside the deck if we want to
        //  reuse the same card
        cards.add(card)
        numCards++
    }



    fun  getCardById(id: String): Card {
        cards.forEach{
            if (it.id == id) {
                return it
            }
        }
        throw Exception("Card not found")   // !!!
    }

    fun getCardIndex(id: String) : Int {
        cards.forEachIndexed{ index, element ->
            if (element.id == id) {
                return index
            }
        }
        throw Exception("Card not found")   // !!!
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


    private fun cmdShowCards() {
        cards.forEach {
            it.show()
            it.update()
        }
    }

    private fun cmdDetailCards() {
        cards.forEach {
            it.details()
        }
    }




    fun print(){
        print("Nombre: $name, Numero de tarjetas: ${cards.size}")
    }

}

