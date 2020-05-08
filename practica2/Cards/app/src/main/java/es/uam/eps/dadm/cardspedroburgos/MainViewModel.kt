package es.uam.eps.dadm.cardspedroburgos
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.*



class MainViewModel: ViewModel() {
    private  val tag = "MainActivityViewModel"

    lateinit var activeDeck : Deck
    lateinit var activeCard : Card
    val actionbarTitle = MutableLiveData<String>()




}