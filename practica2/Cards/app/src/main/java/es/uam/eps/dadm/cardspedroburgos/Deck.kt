package es.uam.eps.dadm.cardspedroburgos

import java.io.Serializable
import java.util.*

open class Deck (var name: String = "", val id: String = UUID.randomUUID().toString()) : Serializable {





    var numCards = 0
    var total = 0
    var total_easy = 0
    var total_dudo = 0
    var total_hard = 0

    // We could add categories at some point
    // We could add a % rate of difficultness

    fun updateDeck(quality: Quality){
        when (quality) {
            Quality.FACIL -> total_easy++
            Quality.DUDO -> total_dudo++
            Quality.DIFICIL -> total_hard++
            Quality.NO -> true
            else -> true
        }
        total++
    }
}

