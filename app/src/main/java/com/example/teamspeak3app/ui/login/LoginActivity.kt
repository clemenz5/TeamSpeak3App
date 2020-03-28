package com.example.teamspeak3app.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.teamspeak3app.R

class LoginActivity : AppCompatActivity() {

    companion object {
        const val LOGIN_HOST = "login_host"
        const val LOGIN_USERNAME = "login_username"
        const val LOGIN_PASSWORD = "login_password"
    }

    private var mLoginButton: Button? = null
    private var mHostInput: EditText? = null
    private var mUsernameInput: EditText? = null
    private var mPasswordInput: EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mLoginButton = findViewById(R.id.login_button_login_activity)
        mHostInput = findViewById(R.id.host_edit_text_login_activity)
        mUsernameInput = findViewById(R.id.username_edit_text_login_activity)
        mPasswordInput = findViewById(R.id.password_edit_text_login_activity)

        mLoginButton?.setOnClickListener { _ -> run{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(LOGIN_HOST, mHostInput?.text.toString())
            intent.putExtra(LOGIN_USERNAME, mUsernameInput?.text.toString())
            intent.putExtra(LOGIN_PASSWORD, mPasswordInput?.text.toString())
            startActivity(intent)
            finish()
        } }

    }
}
