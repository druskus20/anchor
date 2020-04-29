package es.uam.eps.dadm.cards


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import java.io.FileNotFoundException


class MainActivity : AppCompatActivity(), CardShowFragment.onCardShowFragmentInteractionListener,
                                          DeckListFragment.onDeckListFragmentInteractionListener,
                                          CardListFragment.onCardListFragmentInteractionListener {
    private val savefile = "cards.save"

    private val mainActivityViewModel by lazy {
        // !!!!!! Forzado
        ViewModelProviders.of(this).get(MainViewModel::class.java)
        //ViewModelProviders.of(this).get(DeckListViewModel::class.java)
    }

    override fun onDeckSelected() {
    var fragment = CardListFragment()
    supportFragmentManager
        ?.beginTransaction()
        ?.replace(R.id.fragment_container, fragment)
        ?.addToBackStack("CardList")
        ?.commit()
    }
    override fun onCardAdd() {
        var fragment = CardAddFragment()
        supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.fragment_container, fragment)
            // ?.addToBackStack("Decks") !!!
            ?.addToBackStack( "CardAdd" )
            ?.commit()
    }

    override fun onEndStudy() {
        supportFragmentManager?.popBackStack()
    }

    override fun onBeginStudy() {
        var fragment = CardShowFragment()
        supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.fragment_container, fragment)
            ?.addToBackStack("Study")
            ?.commit()
    }

    override fun onEditCard(){
        var fragment = CardEditFragment()
        supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.fragment_container, fragment)
            ?.addToBackStack("editCard")
            ?.commit()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // If there is already a fragment created
        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        // Else, create a new fragment_container
        if (fragment == null) {
            fragment = DeckListFragment ()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                //  ?.addToBackStack( "DeckList" ) do not go back from the first fragment, just exit
                .commit()
        }

        // Livedata for the titlebar
        mainActivityViewModel.actionbarTitle.observe(this, Observer {
            supportActionBar?.title=it
        })

        try {
            val fis = openFileInput(savefile)
            mainActivityViewModel.loadSave(fis)
            fis.close()
        }
        catch (e: FileNotFoundException){
            val view = findViewById<View>(android.R.id.content)
            Snackbar.make(view, "No se ha ENCOTNRADO FICHEOR", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

    }

    override fun onPause() {
        super.onPause()
        val fos = openFileOutput(savefile, Context.MODE_PRIVATE)
        mainActivityViewModel.saveData(fos)
        fos.close()
    }
}


