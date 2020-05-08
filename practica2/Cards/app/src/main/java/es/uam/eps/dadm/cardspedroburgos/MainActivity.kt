package es.uam.eps.dadm.cardspedroburgos


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.database.FirebaseDatabase
import es.uam.eps.dadm.cardspedroburgos.ui.login.LoginViewModel
import es.uam.eps.dadm.cardspedroburgos.ui.login.LoginViewModelFactory
import net.danlew.android.joda.JodaTimeAndroid


class MainActivity : AppCompatActivity(), CardShowFragment.onCardShowFragmentInteractionListener,
                                          DeckListFragment.onDeckListFragmentInteractionListener,
                                          CardListFragment.onCardListFragmentInteractionListener,
                                          CardAddFragment.onCardAddFragmentInteractionListener {
    private val savefile = "cards.save"

    private val mainActivityViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
        //ViewModelProviders.of(this).get(DeckListViewModel::class.java)
    }

    override fun onCardAddNoBackStack() {
        var fragment = CardListFragment.newInstance()
        supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.fragment_container, fragment)
            // !!! GO BACK IN THE STACK
            ?.addToBackStack("CardList")
            .commit()
    }

     override fun onDeckSelected() {
    var fragment = CardListFragment.newInstance()
         supportFragmentManager
             ?.beginTransaction()
             ?.replace(R.id.fragment_container, fragment)
             .addToBackStack("CardList")
             .commit()
    }
    override fun onCardAdd() {
        var fragment = CardAddFragment.newInstance()
        supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.fragment_container, fragment)
            // ?.addToBackStack("Decks") !!!
            ?.addToBackStack( "CardAdd" )
            .commit()
    }

    override fun onEndStudy() {
        supportFragmentManager.popBackStack()
    }

    override fun onBeginStudy() {
        var fragment = CardShowFragment.newInstance()
        supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.fragment_container, fragment)
            ?.addToBackStack("Study")
            .commit()
    }

    override fun onEditCard(){
        var fragment = CardEditFragment.newInstance()
        supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.fragment_container, fragment)
            ?.addToBackStack("editCard")
            .commit()
    }

    override fun onDeckStats() {
        var fragment = DeckStatsFragment.newInstance()
        supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.fragment_container, fragment)
            ?.addToBackStack("editCard")
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        JodaTimeAndroid.init(applicationContext)
        setContentView(R.layout.activity_main)

        // If there is already a fragment created
        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        // Else, create a new fragment_container
        if (fragment == null) {
            fragment = DeckListFragment()
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    //  ?.addToBackStack( "DeckList" ) do not go back from the first fragment, just exit
                    .commit()
        }

        // Livedata for the titlebar
        mainActivityViewModel.actionbarTitle.observe(this, Observer {
            supportActionBar?.title = it
        })
    }

}



