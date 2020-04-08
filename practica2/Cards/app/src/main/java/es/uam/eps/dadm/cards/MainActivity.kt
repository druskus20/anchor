package es.uam.eps.dadm.cards


import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val TAG: String = "MainActivity"

    private val mainActivityViewModel by lazy {
        // !!!!!! Forzado
        ViewModelProviders.of(this).get(MainViewModel::class.java)
        //ViewModelProviders.of(this).get(DeckListViewModel::class.java)
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


        /// !!!!
        //mainActivityViewModel.load_save()
    }

    override fun onPause() {
        super.onPause()
        //mainActivityViewModel.saveData()
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


