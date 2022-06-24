package id.ac.ukdw.a71190413_final

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginScreenActivity : AppCompatActivity() {

    lateinit var GoogleSout: GoogleSignInClient
    private lateinit var firebase_auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        if(GoogleSignIn.getLastSignedInAccount(this)!=null){
            startActivity(Intent(this, BukuItemList::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        // Variabel klik
        var Signin = findViewById<LinearLayout>(R.id.Signin)
        var register = findViewById<TextView>(R.id.registerGoogle)

        // Inisialisasi FireBase
        FirebaseApp.initializeApp(this)

        val optionGso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.webClient))
            .requestEmail()
            .build()
        GoogleSout= GoogleSignIn.getClient(this,optionGso)

        firebase_auth= FirebaseAuth.getInstance()

        Signin.setOnClickListener{ view: View? ->
            actionSignIn()
        }

        register.setOnClickListener {
            var browserIntent: Intent? = null
            browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://accounts.google.com/signup/v2/webcreateaccount?continue=https%3A%2F%2Fcontacts.google.com%2F%3Fhl%3Den%26tab%3DkC&hl=en&dsh=S638564630%3A1655747212148553&biz=false&flowName=GlifWebSignIn&flowEntry=SignUp"));
            startActivity(browserIntent);
        }

    }

    private  fun actionSignIn(){
        val signInIntent: Intent =GoogleSout.signInIntent
        startActivityForResult(signInIntent,123)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==123){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            cekingResult(task)
        }
    }

    private fun cekingResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount? =completedTask.getResult(ApiException::class.java)
            if (account != null) {
                goToAfterAuthOkay(account)
            }
        } catch (e: ApiException){
            Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToAfterAuthOkay(account: GoogleSignInAccount){
        val credential= GoogleAuthProvider.getCredential(account.idToken,null)
        firebase_auth.signInWithCredential(credential).addOnCompleteListener {task->
            if(task.isSuccessful) {
                val intent = Intent(this, BukuItemList::class.java)
                startActivity(intent)
                finish()
            }
        }
    }



}