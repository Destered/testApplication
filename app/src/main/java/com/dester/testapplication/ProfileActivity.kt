package com.dester.testapplication

import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.IOException
import java.io.InputStream
import java.util.*


class ProfileActivity : AppCompatActivity() {
    private val PROFILE_PREFERENCES = "CurrentProfile"
    private val PROFILE_PREFERENCES_USER = "User"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val user = intent.getStringExtra("user")
        profileBuild(user)
    }

    private fun profileBuild(user: String) {
        val info = user.split(';')
        firstName.text = info[0]
        secondName.text = info[1]
        email_output.text = info[2]
        val picName = ("avatar_" + info[0] + info[1] + ".jpg").toLowerCase(Locale.ROOT)
        val inputStream: InputStream?
        try {
            val imageView = findViewById<ImageView>(R.id.avatar)
            inputStream = applicationContext.assets.open(picName)
            val d = Drawable.createFromStream(inputStream, null)
            imageView.setImageDrawable(d)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun logout(view: View) {
        val pref: SharedPreferences = getSharedPreferences(PROFILE_PREFERENCES, MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(PROFILE_PREFERENCES_USER, "non")
        editor.apply()
        finish()
    }

}
