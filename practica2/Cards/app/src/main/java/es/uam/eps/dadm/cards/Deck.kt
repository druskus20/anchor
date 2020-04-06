package es.uam.eps.dadm.cards

import kotlin.random.Random
import java.io.Serializable
import java.util.*

open class Deck (var name: String, val id: String = UUID.randomUUID().toString()) : Serializable {
    private var cards: MutableList<Card> = mutableListOf()
    private var opciones: MutableList<Command> = mutableListOf()
    var numCards = 0

    // We could add categories at some point
    // We could add a % rate of difficultness

    init {
        // Menu options
        opciones.add(Command(::cmdAddCard, "Añadir tarjeta"))
        opciones.add(Command(::cmdShowCards, "Presentar tarjetas"))
        opciones.add(Command(::cmdDetailCards, "Listar valores de tarjetas"))
        opciones.add(Command(::cmdSimulate, "Simula el paso del tiempo y las respuestas"))
        opciones.add(Command(null, "Volver"))
        // We could add a save deck option
    }
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



    // Shows different options
    fun menu() {
        while (true) {
            println("------------------------------------------")
            var optionNumber = 1
            opciones.forEach {
                println("$optionNumber. ${it.nombre}")
                optionNumber++
            }
            // Asks for user input
            var userSel: Int?
            do {
                print("Elige una opción: ")
                userSel = readLine()?.toIntOrNull() ?: let {
                    println("Error, opcion incorrecta")
                    null
                }
                println("------------------------------------------")
            } while (userSel == null || userSel !in 1..opciones.size)

            // Calls the function selected

            if (userSel == opciones.size) {     // Exit
                return
            }
            userSel -= 1
            opciones[userSel].call()
        }
    }

    // user command functions ------------------------------------
    private fun cmdAddCard() {
        // We should create the card outside the deck if we want to
        //  reuse the same card
        val card: Card = Card.readCard()
        cards.add(card)
        numCards++
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

