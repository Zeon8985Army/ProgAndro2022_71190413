package id.ac.ukdw.pertemuan5_71190413

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val usernameData = intent.getStringExtra("username")
        var usernameView: TextView = findViewById(R.id.tv_welcomeUser)
        val linearLayout: LinearLayout = findViewById(R.id.ll_welcomeUser)

        if (usernameData.toString().length > 5){
            linearLayout.orientation= LinearLayout.VERTICAL

            usernameView.text =  usernameView.text.toString()+usernameData.toString()
        }else{
            usernameView.text =  usernameView.text.toString()+usernameData.toString()
        }

        val btnLogOut: Button = findViewById(R.id.btn_logout)
        btnLogOut.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }
    }
}