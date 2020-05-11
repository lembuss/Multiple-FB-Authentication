package com.example.multiplefbauthentication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import java.util.*
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var providers : List<AuthUI.IdpConfig>
    val REQUEST_CODE: Int = 7068

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //init
        providers = Arrays.asList<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build(), //email
            AuthUI.IdpConfig.PhoneBuilder().build(), //phone
            AuthUI.IdpConfig.GoogleBuilder().build(), //google
            AuthUI.IdpConfig.FacebookBuilder().build() //facebook

//            AuthUI.IdpConfig.TwitterBuilder().build() //twitter
        )

        showSignInOptions()


        btnSignOut.setOnClickListener {
            //signout
            AuthUI.getInstance().signOut(this@MainActivity)
                .addOnCompleteListener {
                    btnSignOut.isEnabled = false
                    showSignInOptions()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {

            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(this, "" +user!!.email, Toast.LENGTH_LONG).show()

                btnSignOut.isEnabled = true

            } else {

                Toast.makeText(this, "" +response!!.error!!.message, Toast.LENGTH_LONG).show()

            }
        }
    }

    private fun showSignInOptions(){
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.MyTheme)
            .build(),REQUEST_CODE
        )
    }
}
