package id.ac.ukdw.pertemuan9_71190413

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity(){
    @SuppressLint("ResourceAsColor")

    // Variabel SharedPreferences
    var sharedPref: SharedPreferences? = null
    var spEdit: SharedPreferences.Editor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // SharedPreferences
        sharedPref = getSharedPreferences("sp_session_lang_txtSize", Context.MODE_PRIVATE)
        spEdit = sharedPref?.edit()

        // Cek Jika sudah punya seasson login
        if (sharedPref?.getBoolean("logIn", false) == true){
            val i: Intent = Intent( this,MainActivity::class.java)
            startActivity(i)
            finish()
        }else{
            setContentView(R.layout.activity_login)

            val edtUsername : EditText = findViewById(R.id.et_username)
            val edtPassword : EditText = findViewById(R.id.et_password)
            val btnLogin : Button = findViewById(R.id.bt_login)

            btnLogin.setOnClickListener {
                login(edtUsername.text.toString(), edtPassword.text.toString())
            }
        }
    }

    fun login(username: String, pass: String){
        if(pass.equals("1234")){

            // sp_membuat_seasson
            spEdit?.putBoolean("logIn", true)
            spEdit?.putString("username", username)
            spEdit?.commit()

            val i: Intent = Intent( this,MainActivity::class.java)
            i.putExtra("username",sharedPref?.getString("username", ""))
            startActivity(i)
            finish()

        }else{
            showMssg("Password is not correct (hint: 1234)!")
        }
    }

    fun showMssg(message: String){
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }
}