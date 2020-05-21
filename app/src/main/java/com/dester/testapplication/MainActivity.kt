package com.dester.testapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var loginStatus = false
    private val PROFILE_PREFERENCES = "CurrentProfile"
    private val PROFILE_PREFERENCES_USER = "User"
    private var emailField = "0"
    private var passwordField = "0"
    private lateinit var pref: SharedPreferences
    val emailRegex =
        "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])"
    val passwordRegex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,50}\$"


    override fun onCreate(savedInstanceState: Bundle?) {
        pref = getSharedPreferences(PROFILE_PREFERENCES, MODE_PRIVATE)
        val check = pref.getString(PROFILE_PREFERENCES_USER, "non")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (check != null && check != "non") openProfile(check)
        email_input.addTextChangedListener(object : TextWatcher {

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val str = charSequence.toString()
                if (!Regex(emailRegex).matches(str)) {
                    errMsgEmail.text = "E-mail invalid!"
                } else errMsgEmail.text = ""
            }

            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(editable: Editable) {
                if (Regex(emailRegex).matches(editable.toString()) || editable.toString() == ""
                ) errMsgEmail.text = ""
                else errMsgEmail.text = "E-mail invalid!"

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })

        password_input.addTextChangedListener(object : TextWatcher {

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val str = charSequence.toString()
                if (!Regex(passwordRegex).matches(str)) {
                    errMsgPassword.text = "The password does not meet the following conditions:\n" +
                            " Has >= 6 characters\n" +
                            " Contains a capital letter\n" +
                            " Contains a lowercase letter\n" +
                            " Contain the digits"
                } else errMsgPassword.text = ""
            }

            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(editable: Editable) {
                if (Regex(passwordRegex).matches(editable.toString()) || editable.toString() == ""
                ) errMsgPassword.text = ""
                else errMsgPassword.text =
                    "The password does not meet the following conditions:\n" +
                            "Has >= 6 characters\n" +
                            "Contains a capital letter\n" +
                            "Contains a lowercase letter\n" +
                            "Contain the digits"

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })

    }

    fun login(view: View) {
        val user = pref.getString(PROFILE_PREFERENCES_USER, "non")

        if (user == "non") {
            emailField = email_input.text.toString()
            passwordField = password_input.text.toString()

            val br = application.assets.open("UserInfo.txt").bufferedReader()
            br.forEachLine {
                if (check(it, emailField, passwordField)) {
                    loginStatus = true
                    openProfile(it)
                }
            }
            if (!loginStatus) {
                val toast = Toast.makeText(
                    applicationContext,
                    "Wrong Password or Email",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }
        } else {
            if (user != null) {
                openProfile(user)
            }

        }


    }

    private fun check(inLine: String, emailField: String, passwordField: String): Boolean {
        val line = inLine.split(';')
        if (line[2] == emailField) {
            if (line[3] == passwordField) {
                return true
            }
            return false
        }
        return false
    }

    private fun openProfile(User: String) {
        val profileIntent = Intent(this, ProfileActivity::class.java)
        val editor = pref.edit()
        editor.putString(PROFILE_PREFERENCES_USER, User)
        editor.apply()
        profileIntent.putExtra("user", User)
        startActivity(profileIntent)
    }
}
