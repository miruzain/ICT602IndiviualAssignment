package com.example.electricitybill

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val textStudentInfo = findViewById<TextView>(R.id.textStudentInfo)
        val textUrl = findViewById<TextView>(R.id.textUrl)

        textStudentInfo.text = """
            Full Name: Megat Imran Ruzain bin Ruzaif
            Student ID: 2021241352
            Course Code: CS240
            Course Name: Bachelor of Information Technology
            © 2024 Your Name
        """.trimIndent()

        textUrl.text = "https://github.com/miruzain/ICT602IndiviualAssignment"
        textUrl.movementMethod = LinkMovementMethod.getInstance()
        textUrl.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(textUrl.text.toString()))
            startActivity(intent)
        }
    }
}
