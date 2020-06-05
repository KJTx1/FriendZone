package com.example.friendzone

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_user_content.*


class UserContent : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser

    private var backPressTime: Long? = null
    private lateinit var backToast: Toast

    companion object {
        const val USER_KEY = "USER_KEY"
        const val USER_DISPLAY_NAME = "USER_DISPLAY_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_content)

        backPressTime = 0
        backToast = Toast.makeText(this,"Press back again to exit",Toast.LENGTH_LONG);

        auth = (application as FriendZoneApp).auth

        currentUser = intent.getParcelableExtra<FirebaseUser>(USER_KEY)!!
        if (currentUser.displayName != null) {
            tvUser.text = currentUser.displayName
        } else {
            tvUser.text = intent.getStringExtra(USER_DISPLAY_NAME)?.toString()
        }

        btnLogout.setOnClickListener {
            logout()
        }

        btnAdd.setOnClickListener {
            startActivity(Intent(this, Upload::class.java))
        }
    }

    private fun logout() {
        auth.signOut()
        Toast.makeText(this, "User ${tvUser.text} logged out", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onBackPressed() {
        if (backPressTime?.plus(2000)!! > System.currentTimeMillis()) {
            backToast.cancel()
            super.onBackPressed()
            finishAffinity()
        } else {
            backToast.show()
        }
        backPressTime = System.currentTimeMillis()
    }

}
