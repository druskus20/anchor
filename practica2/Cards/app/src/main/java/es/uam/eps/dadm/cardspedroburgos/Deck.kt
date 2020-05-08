package es.uam.eps.dadm.cardspedroburgos

import java.io.Serializable
import java.util.*

open class Deck (var name: String = "", val id: String = UUID.randomUUID().toString()) : Serializable {





    var numCards = 0
    var total = 0
    var totalEasy = 0
    var totalDudo = 0
    var totalHard = 0

    // We could add categories at some point
    // We could add a % rate of difficultness

    fun updateDeck(quality: Quality){
        when (quality) {
            Quality.FACIL -> totalEasy++
            Quality.DUDO -> totalDudo++
            Quality.DIFICIL -> totalHard++
            Quality.NO -> true
            else -> true
        }
        total++
    }
}

