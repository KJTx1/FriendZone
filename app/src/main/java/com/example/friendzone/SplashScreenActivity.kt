package com.example.friendzone

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.friendzone.UserContent.Companion.USER_DISPLAY_NAME
import com.example.friendzone.UserContent.Companion.USER_KEY
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        auth = (application as FriendZoneApp).auth

        val background = object: Thread() {
            override fun run() {
                super.run()
                var intent: Intent? = null
                try {
                    Thread.sleep(2000)
                    if (auth.currentUser != null) {
                        intent = Intent(baseContext, UserContent::class.java).apply {
                            putExtra(USER_KEY, auth.currentUser)
//                            putExtra(USER_DISPLAY_NAME, auth.currentUser!!.displayName)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                    } else {
                        intent = Intent(baseContext, MainActivity::class.java)
                    }
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        background.start()
    }
}
