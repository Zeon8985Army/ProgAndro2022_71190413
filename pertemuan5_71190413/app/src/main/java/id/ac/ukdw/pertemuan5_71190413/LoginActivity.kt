package id.ac.ukdw.pertemuan5_71190413

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity(){
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val edtUsername : EditText = findViewById(R.id.et_username)
        val edtPassword : EditText = findViewById(R.id.et_password)
        val btnLogin : Button = findViewById(R.id.bt_login)

        btnLogin.setOnClickListener {
            login(edtUsername.text.toString(), edtPassword.text.toString())
        }
    }

    fun login(username: String, pass: String){
        if(pass.equals("1234")){
            val i: Intent = Intent( this,MainActivity::class.java)
            i.putExtra("username",username)
            startActivity(i)
        }else{
            showMssg("Password is not correct !")
        }
    }

    fun showMssg(message: String){
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }
}