package es.uam.eps.dadm.cards

import android.os.Environment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.*
import java.lang.Exception


class MainViewModel: ViewModel() {
    private  val tag = "MainActivityViewModel"
    val savefile = "decks.save"     // Default filename for the saved data
    var decks =   mutableListOf<Deck>()
    lateinit var activeDeck : Deck
    lateinit var activeCard : Card
    val actionbarTitle = MutableLiveData<String>()

    fun removeActiveDeck() : Int {
        val index = decks.indexOf(activeDeck)
        decks.removeAt(index)
        return index
    }


    // Adds a new deck to this.decks
    fun addDeck(name:String) {
        //val deck: Deck = Deck.readDeck() CLI
        val newDeck = Deck(name)
        decks.add(newDeck)
    }

    // Saves the app data into a file
    fun saveData(fos : FileOutputStream) {

        // Makes use of Serializable to store cards to a file
        try{

            val oos= ObjectOutputStream(fos)
            oos.writeObject(decks)
            oos.close()

        } catch(ioe: IOException){
            println("Error, fichero no accesible o vacio")
            throw ioe
        }
    }

    // Loads the app data from a file
    fun loadSave(fis: FileInputStream){

        Log.d(this.tag, "LOAD_SAVE")

        // Makes use of Serializable to read cards from a file
        try {
            val ois = ObjectInputStream(fis)

            // The following warning is being ignored.
            //  I could use a cast but that would mean
            //  dealing with a undefined type ArrayList
            decks = ois.readObject() as MutableList<Deck>
            ois.close()

        } catch (ioe: IOException) {
            println("Error, fichero no accesible o vacio")
            return
        } catch (c: ClassNotFoundException) {
            println("Error, clase no encontrada")
            return
        }
    }
}