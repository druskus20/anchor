package es.uam.eps.dadm.cardspedroburgos

import androidx.lifecycle.ViewModel


class DeckStatsViewModel : ViewModel() {
    private val TAG : String = "DeckStatsViewModel"

    var totalEasy = 0
    var totalHard = 0
    var totalDoubt = 0

    var totalCards = 0
}