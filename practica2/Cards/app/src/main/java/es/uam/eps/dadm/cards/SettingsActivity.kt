package es.uam.eps.dadm.cards

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager


class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false)

    }

    // For using the arrow at the top to go back to home
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }

    companion object {
        val max_tarjetas = "max_tarjetas"
        val remember_me = "remember_me"
        val logged_user = "logged_user"


        fun getMaxTarjetas(context: Context): String? {
            return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString("max_tarjetas", "20")
        }

        fun setMaxTarjetas(context: Context, max: String) {
            val sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putString(SettingsActivity.max_tarjetas, max)
            editor.commit()
        }

        fun getRememberMe(context: Context): String? {
            return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString("remember_me", "false")
        }

        fun setRememberMe(context: Context, max: String) {
            val sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putString(SettingsActivity.remember_me, max)
            editor.commit()
        }

        fun getLoggedUser(context: Context): String? {
            return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString("logged_user", "null")
        }

        fun setLoggedUser(context: Context, max: String) {
            val sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putString(SettingsActivity.logged_user, max)
            editor.commit()
        }
    }
}