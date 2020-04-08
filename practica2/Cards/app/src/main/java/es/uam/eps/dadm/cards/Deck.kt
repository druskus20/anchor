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


    private fun cmdSimulate() {
        val cardQualityMap = mutableMapOf<Card, Quality>()

        // Asks for a number of days to simulate
        var days: Int?
        do {
            print("Elige el numero de dias a simular: ")
            days = readLine()?.toIntOrNull() ?: let {
                println("Error, opcion incorrecta")
                null
            }
        } while(days == null)

        // Randomizes quality for each card and adds it to the map
        for (card in cards) {
            cardQualityMap[card] = when (Random.nextInt(0, 3))
            {
                0 -> Quality.DIFICIL
                1 -> Quality.DUDO
                2 -> Quality.FACIL
                else -> throw Exception("Invalid quality number")
            }
        }

        simulate(cardQualityMap, days)
    }

    // Simulate the responses to a list of cards over 20 days
    private fun simulate(cq : Map<Card, Quality>, days: Int) {

        /*
        println("Fecha: 0")
        cq.forEach(){
            it.key.currentDate = 0
            it.key.quality = it.value
            it.key.update()
            it.key.details()
        }*/

        for (day in 0..days){
            println("------------------------------------------")
            println("Fecha: $day")
            cq.forEach {
                it.key.currentDate = day
                if (day  == it.key.nextPracticeDate) {
                    it.key.quality = it.value
                    it.key.update()
                    it.key.details()
                }
            }
        }
        println("------------------------------------------")
    }

    fun print(){
        print("Nombre: $name, Numero de tarjetas: ${cards.size}")
    }

}

