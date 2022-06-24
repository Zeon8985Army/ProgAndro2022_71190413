package id.ac.ukdw.a71190413_final

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.setPadding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*


class DetailActivity : AppCompatActivity() {

    lateinit var mGoogleSignInClient: GoogleSignInClient

    // Variabel untuk Menambah field Input penulis
    val listNamaPenulis = arrayOf("Penulis 2","Penulis 3")
    var counterTmbPenulis = 0
    var editTextList = ArrayList<TextView>()
    var linearLayoutPenulisList = ArrayList<LinearLayout>()

    // Variabel Firestore
    var firestore: FirebaseFirestore? = null
    var successInsertData : Boolean? = null

    // Variabel untuk Load image
    lateinit var ImageURI : Uri
    lateinit var statusGambar : String
    var ImageURIList = ArrayList<Uri>(2)
    var urlCoverDpn : String = ""
    var urlCoverBlkg : String = ""

    // Val simpan Id Data saat ini
    var idData : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Toolbar
        // Untuk Toolbar
        setSupportActionBar(findViewById(R.id.toolbarUmum))
        supportActionBar?.setDisplayShowTitleEnabled(true)

        // Firestore
        firestore = FirebaseFirestore.getInstance()

        // Input
        val judul = findViewById<TextView>(R.id.judulBuku)
        val penerbit = findViewById<TextView>(R.id.namaPenerbit)
        val penulis1 = findViewById<TextView>(R.id.penulis1)
        val tahunTerbit = findViewById<TextView>(R.id.tahunTerbit)
        val jumlahHalaman = findViewById<TextView>(R.id.jumlahHalaman)

        // Layout Gambar
        val gambarCoverDpn = findViewById<ImageView>(R.id.gambarCoverDpn)
        val gambarCoverBelakang = findViewById<ImageView>(R.id.coverBelakang)


        val progresLog = ProgressDialog(this)
        progresLog.setMessage("Load File ...")
        progresLog.setCancelable(false)
        progresLog.show()

        firestore?.collection("dataBuku")
            ?.whereEqualTo("judul",intent.getStringExtra("judulBuku").toString())?.get()!!
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // Dapatin data id document saat ini
                    idData = document.id

                    judul.setText(document.data["judul"].toString())
                    penerbit.setText(document.data["penerbit"].toString())
                    penulis1.setText(document.data["penulis1"].toString())
                    tahunTerbit.setText(document.data["tahunTerbit"].toString())
                    jumlahHalaman.setText(document.data["jumlahHalaman"].toString())

                    if (document.data["penulis2"].toString()!="" && document.data["penulis3"].toString()!="" ){
                        createPenulis(document.data["penulis2"].toString())
                        createPenulis(document.data["penulis3"].toString())
                    }else if(document.data["penulis2"].toString()!=""){
                        createPenulis(document.data["penulis2"].toString())
                    }else{
                    }

                    if (document.data["urlCoverblkg"].toString()==""){
                        val storageReference = FirebaseStorage.getInstance().reference
                        val photoReference = storageReference.child("image/${document.data["urlCoverDpn"].toString()}")
                        val lclFile = File.createTempFile("TmpIMG","jpg")
                        photoReference.getFile(lclFile)
                            .addOnSuccessListener {
                                val bitmap = BitmapFactory.decodeFile(lclFile.absolutePath)
                                gambarCoverDpn.setImageBitmap(bitmap)
                                if (progresLog.isShowing) progresLog.dismiss()
                            }
                            .addOnFailureListener {
                            }
                    }
                    else if (document.data["urlCoverblkg"].toString()!=""){
                        val storageReference = FirebaseStorage.getInstance().reference
                        val photoReference = storageReference.child("image/${document.data["urlCoverDpn"].toString()}")
                        val lclFile = File.createTempFile("TmpIMG","jpg")
                        photoReference.getFile(lclFile)
                            .addOnSuccessListener {
                                val bitmap = BitmapFactory.decodeFile(lclFile.absolutePath)
                                gambarCoverDpn.setImageBitmap(bitmap)
                            }
                            .addOnFailureListener {
                            }

                        val lclFile2 = File.createTempFile("TmpIMG","jpg")
                        val photoReference2 = storageReference.child("image/${document.data["urlCoverblkg"].toString()}")
                        photoReference2.getFile(lclFile2)
                            .addOnSuccessListener {
                                val bitmap = BitmapFactory.decodeFile(lclFile2.absolutePath)
                                gambarCoverBelakang.setImageBitmap(bitmap)
                                if (progresLog.isShowing) progresLog.dismiss()
                            }
                            .addOnFailureListener {
                            }
                    }
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            R.id.logout -> {
                val requestgsso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.webClient))
                    .requestEmail()
                    .build()
                mGoogleSignInClient= GoogleSignIn.getClient(this,requestgsso)
                mGoogleSignInClient.signOut().addOnCompleteListener {
                    val intent= Intent(this, LoginScreenActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    fun createPenulis(penulisAtr :String = ""){
        // Layout
        val linearLayout1 = findViewById<LinearLayout>(R.id.ln_penulis)
        val linearlyt = LinearLayout(this)
        linearlyt.orientation = LinearLayout.HORIZONTAL
        linearlyt.setPadding(10,10,10,0)
        linearLayoutPenulisList.add(linearlyt)

        // TextView
        val textView = TextView(this)
        textView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT
            ,ViewGroup.LayoutParams.WRAP_CONTENT,
            2f
        )
        textView.setText(listNamaPenulis[counterTmbPenulis])

        // Akses multiple penulis lewat array object pada EditText di atas
        // Toast.makeText(this,editTextList[0].text.toString(),Toast.LENGTH_SHORT).show()
        val editText = TextView(this)
        editTextList.add(editText)
        editText.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT
            ,ViewGroup.LayoutParams.WRAP_CONTENT,
            1f
        )

        editText.setBackgroundResource(R.drawable.background_border_full)
        editText.setPadding(50,30,50,30)
        textView.setTextColor(getResources().getColor(R.color.white))
        linearlyt.addView(textView)
        linearlyt.addView(editText)

        linearLayout1.addView(linearlyt)

        if (penulisAtr!=""){
            editText.setText(penulisAtr)
        }
        counterTmbPenulis +=1

    }
}