package es.uam.eps.dadm.cardspedroburgos.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import es.uam.eps.dadm.cardspedroburgos.Deck
import es.uam.eps.dadm.cardspedroburgos.MainActivity
import es.uam.eps.dadm.cardspedroburgos.R
import es.uam.eps.dadm.cardspedroburgos.SettingsActivity
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.RuntimeException


class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
        val loading = findViewById<ProgressBar>(R.id.loading)

        // Skip the login screen if necessary
        if ( SettingsActivity.getRememberMe(applicationContext) == true){
            // Launch next activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid
            register.isEnabled = loginState.isDataValid
            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })








        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                val result = firebase_login(username.text.toString(), password.text.toString())
                Log.d("RESULT", result.toString())
            }
            register.setOnClickListener {
                loading.visibility = View.VISIBLE
                firebase_register(username.text.toString(), password.text.toString())

            }
        }
    }

    private fun firebase_login (username : String, password: String) {

        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    loading.visibility = View.INVISIBLE
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("LOGIN", "signInWithEmail:success")

                    Toast.makeText(applicationContext, getString(R.string.login_success) + " " + username.toString() , Toast.LENGTH_SHORT).show()

                    // Set logged user
                    SettingsActivity.setLoggedUser(applicationContext, username.replace(".", ","))
                    SettingsActivity.setRememberMe(applicationContext, switch1.isChecked)

                    // Launch next activity
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    setResult(Activity.RESULT_OK)
                    finish()

                } else {
                    loading.visibility = View.INVISIBLE
                    // If sign in fails, display a message to the user.
                    Toast.makeText(applicationContext, getString(R.string.login_error) , Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun firebase_register(username : String, password : String) {


        auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    loading.visibility = View.INVISIBLE

                    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
                    val reference = database.getReference(username.replace(".", ",") + "/Decks")


                    // Sign in success, update UI with the signed-in user's information
                    Log.d("LOGIN", "createUserWithEmail:success")
                    Toast.makeText(applicationContext, getString(R.string.register_success) , Toast.LENGTH_SHORT).show()
                } else {
                    loading.visibility = View.INVISIBLE
                    // If sign in fails, display a message to the user.
                    Log.w("LOGIN", "createUserWithEmail:failure", task.exception)

                    Toast.makeText(applicationContext, getString(R.string.register_error) , Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}


