package es.uam.eps.dadm.cardspedroburgos

import android.util.Log
import androidx.lifecycle.ViewModel



class DeckStatsViewModel : ViewModel() {
    private val TAG : String = "DeckStatsViewModel"

    var total_easy = 0
    var total_hard = 0
    var total_doubt = 0
}