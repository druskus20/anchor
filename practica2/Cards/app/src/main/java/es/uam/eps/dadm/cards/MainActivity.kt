package es.uam.eps.dadm.cards

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
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

                addDeck(view)
            }
            else if (newFragment is CardListFragment){

                addCard()
            }
            else {
                Log.e(TAG, "Unknown fragment class")
            }
        }
    }

    fun addDeck(view: View) {
        val alert = AlertDialog.Builder(this)
        alert.setTitle("TITLE --")
        alert.setMessage("MESSAGE ---")

        // Set an EditText view to get user input
        val input = EditText(this)
        alert.setView(input)

        alert.setPositiveButton("Ok",
            DialogInterface.OnClickListener { dialog, whichButton ->
                val value = input.text.toString()
                // !!!
                Snackbar.make(view, "MSG CARD ADDED", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            })

        alert.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, which ->
            })

        // Set other dialog properties
        alert.create().show()
    }


    fun addCard() {
        supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.fragment_container, CardAddFragment.newInstance())
            // ?.addToBackStack("Decks") !!!
            ?.commitNow()
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


