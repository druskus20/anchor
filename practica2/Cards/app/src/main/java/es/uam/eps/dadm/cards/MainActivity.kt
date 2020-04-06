package es.uam.eps.dadm.cards

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    val TAG: String = "MainActivity"
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
                .commit()
        }


        // !!! Workaround for making the "+" button do different things
        //      depending on the fragment displayed, so we only use 1 activity
        var text : String
        if (fragment is DeckListFragment) {
            Log.d(TAG, "DeckListFragment")
            text = "DeckListFragment"
        }
        else if (fragment is CardListFragment){
            Log.d(TAG, "CardListFragment")
            text = "CardListFragment"
        }
        else {
            text = "Unknown"
            Log.d(TAG, "Unknown fragment class")
        }

        // Listener for the round "+" button
        fab.setOnClickListener { view ->
            Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()


        }
    }

}

/*
package es.uam.eps.dadm.cards

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
} */


