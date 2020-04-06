package es.uam.eps.dadm.cards

import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val TAG: String = "MainActivity"



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

        // Listener for the round "+" button
        fab.setOnClickListener { view ->
            // !!! Workaround for making the "+" button do different things
            //      depending on the fragment displayed, so we only use 1 activity
            var text : String
            // https://stackoverflow.com/questions/45247254/how-to-get-current-fragment-from-mainactivity
            var newFragment = supportFragmentManager.findFragmentById(R.id.fragment_container);

            if (newFragment is DeckListFragment) {
                Log.d(TAG, "DeckListFragment")
                text = "DeckListFragment"
            }
            else if (newFragment is CardListFragment){
                Log.d(TAG, "CardListFragment")
                text = "CardListFragment"
            }
            else {
                text = "Unknown"
                Log.d(TAG, "Unknown fragment class")
            }

            Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    // Hides the fab "+" button
    fun hideAddButton(){
        // fab.visibility = View.INVISIBLE // Doesnt work because visibility can only be set from within the library
        fab.hide()
    }
    // Shows the fab "+" button
    fun showAddButton(){
        fab.show()
    }
/*
    answerButton = findViewById(R.id.answer_button)
    questionTextView = findViewById(R.id.question_text_view)

    answerButton.setOnClickListener {
        questionTextView.text = resources.getString(R.string.answer_text)
        answerButton.visibility = View.INVISIBLE
    }
    */


}


