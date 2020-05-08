package es.uam.eps.dadm.cardspedroburgos
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MainViewModel: ViewModel() {
    private  val tag = "MainActivityViewModel"

    lateinit var activeDeck : Deck
    lateinit var activeCard : Card
    val actionbarTitle = MutableLiveData<String>()




}