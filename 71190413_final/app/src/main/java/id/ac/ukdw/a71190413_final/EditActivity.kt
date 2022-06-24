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
import kotlin.collections.ArrayList


class EditActivity : AppCompatActivity() {

    lateinit var GoogleSout: GoogleSignInClient

    // Variabel untuk Menambah field Input penulis
    val listNamaPenulis = arrayOf("Penulis 2","Penulis 3")
    var counterTmbPenulis = 0
    var editTextList = ArrayList<EditText>()
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

    // Variabel untuk unique judul
    var statusUniqeJudul : Boolean = false
    var judulSaatIni : String? = null
    var daftarJudul = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        daftarJudul.clear()

        // Toolbar
        // Untuk Toolbar
        setSupportActionBar(findViewById(R.id.toolbarUmum))
        supportActionBar?.setDisplayShowTitleEnabled(true)

        // Firestore
        firestore = FirebaseFirestore.getInstance()

        // Button
        val btnAddData = findViewById<CardView>(R.id.tambahData)
        val btnAddPenulis = findViewById<Button>(R.id.tambahPenulis)
        val btnDltPenulis = findViewById<Button>(R.id.hapusPenulis)
        val btnAddGambarDepan = findViewById<CardView>(R.id.addCoverDpn)
        val btnAddGambarBelakang = findViewById<CardView>(R.id.addCoverBlkng)
        val btnDeleteImgCvrDpn = findViewById<CardView>(R.id.deleteCvrDpn)
        val btnDeleteImgCvrBlkng = findViewById<CardView>(R.id.deleteCvrBlkng)

        // Input
        val judul = findViewById<EditText>(R.id.judulBuku)
        val penerbit = findViewById<EditText>(R.id.namaPenerbit)
        val penulis1 = findViewById<EditText>(R.id.penulis1)
        val tahunTerbit = findViewById<EditText>(R.id.tahunTerbit)
        val jumlahHalaman = findViewById<EditText>(R.id.jumlahHalaman)

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
                    judulSaatIni = document.data["judul"].toString()

                    judul.setText(document.data["judul"].toString())
                    penerbit.setText(document.data["penerbit"].toString())
                    penulis1.setText(document.data["penulis1"].toString())
                    tahunTerbit.setText(document.data["tahunTerbit"].toString())
                    jumlahHalaman.setText(document.data["jumlahHalaman"].toString())

                    if (document.data["penulis2"].toString()!="" && document.data["penulis3"].toString()!="" ){
                        createPenulis(document.data["penulis2"].toString())
                        createPenulis(document.data["penulis3"].toString())
                        btnAddPenulis.isEnabled = false
                    }else if(document.data["penulis2"].toString()!=""){
                        createPenulis(document.data["penulis2"].toString())
                    }else{
                        btnDltPenulis.isEnabled = false
                    }

                    if (document.data["urlCoverblkg"].toString()==""){
                        val storageReference = FirebaseStorage.getInstance().reference
                        val photoReference = storageReference.child("image/${document.data["urlCoverDpn"].toString()}")
                        val lclFile = File.createTempFile("TmpIMG","jpg")
                        photoReference.getFile(lclFile)
                            .addOnSuccessListener {
                                val bitmap = BitmapFactory.decodeFile(lclFile.absolutePath)
                                gambarCoverDpn.getLayoutParams().height = 1200
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
                                gambarCoverDpn.getLayoutParams().height = 1200
                                gambarCoverDpn.setImageBitmap(bitmap)
                            }
                            .addOnFailureListener {
                            }

                        val lclFile2 = File.createTempFile("TmpIMG","jpg")
                        val photoReference2 = storageReference.child("image/${document.data["urlCoverblkg"].toString()}")
                        photoReference2.getFile(lclFile2)
                            .addOnSuccessListener {
                                val bitmap = BitmapFactory.decodeFile(lclFile2.absolutePath)
                                gambarCoverBelakang.getLayoutParams().height = 1200
                                gambarCoverBelakang.setImageBitmap(bitmap)
                                if (progresLog.isShowing) progresLog.dismiss()
                            }
                            .addOnFailureListener {
                            }
                    }
                }
            }


        // Event Click
        // Add Penulis
        btnAddPenulis.setOnClickListener {
            if (counterTmbPenulis==2){
                btnAddPenulis.isEnabled = false
            }else{
                btnDltPenulis.isEnabled = true
                createPenulis()
            }
        }

        // Hapus Penulis
        btnDltPenulis.setOnClickListener {
            val lenLnPenulis = linearLayoutPenulisList.size
            if (lenLnPenulis==0){
                btnDltPenulis.isEnabled = false
            }else{
                btnAddPenulis.isEnabled = true
                hapusPenulis(lenLnPenulis)
            }
        }

        // Add Gambar Depan
        btnAddGambarDepan.setOnClickListener {
            statusGambar="Depan"
            pilihGambar()
        }

        // Add Gambar Belakang
        btnAddGambarBelakang.setOnClickListener {
            statusGambar="Belakang"
            pilihGambar()
        }

        // Add Data
        btnAddData.setOnClickListener {
            updateData(judul,penerbit,penulis1,tahunTerbit,jumlahHalaman,daftarJudul)
        }

        btnDeleteImgCvrDpn.setOnClickListener {
            deleteGambar(gambarCoverDpn)
        }

        btnDeleteImgCvrBlkng.setOnClickListener {
            deleteGambar(gambarCoverBelakang)
        }

        // Untuk dapetin daftar judul
        firestore?.collection("dataBuku")?.get()!!
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    daftarJudul.add(document.data["judul"].toString())
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
                GoogleSout= GoogleSignIn.getClient(this,requestgsso)
                GoogleSout.signOut().addOnCompleteListener {
                    val intent= Intent(this, LoginScreenActivity::class.java)
                    Toast.makeText(this,"Logging Out",Toast.LENGTH_SHORT).show()
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
        val editText = EditText(this)
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
        counterTmbPenulis +=1
        if (counterTmbPenulis>=2){
            counterTmbPenulis=2
            val btnAddPenulis = findViewById<Button>(R.id.tambahPenulis)
            btnAddPenulis.isEnabled = false
        }

        if (penulisAtr!=""){
            editText.setText(penulisAtr)
        }
    }

    fun hapusPenulis(lenLnPenulis:Int){
        val linearLayout1 = findViewById<LinearLayout>(R.id.ln_penulis)
        (linearLayoutPenulisList[lenLnPenulis-1].parent as ViewGroup).removeView(linearLayoutPenulisList[lenLnPenulis-1])
        linearLayoutPenulisList.remove(linearLayoutPenulisList[lenLnPenulis-1])
        editTextList.remove(editTextList[lenLnPenulis-1])

        counterTmbPenulis-=1
        if (counterTmbPenulis<0){
            counterTmbPenulis=0
            val btnDltPenulis = findViewById<Button>(R.id.hapusPenulis)
            btnDltPenulis.isEnabled = false
        }
    }

    fun updateData(judul:EditText,penerbit:EditText,penulis1:EditText,tahunTerbit:EditText,jumlahHalaman:EditText, daftarJudul: ArrayList<String>): Boolean {

        // Cek apakah semua input sudah diisi
        val lenPenulis = editTextList.size

        if (judul.text.toString()==""){
            judul.setError("Judul Harus diisi !")
            judul.requestFocus()
            return true
        }

        if (penerbit.text.toString()==""){
            penerbit.setError("Penerbit Harus diisi !")
            penerbit.requestFocus()
            return true
        }

        if (penulis1.text.toString()==""){
            penulis1.setError("Penulis 1 Harus diisi !")
            penulis1.requestFocus()
            return true
        }

        if (tahunTerbit.text.toString()==""){
            tahunTerbit.setError("Tahun Terbit Harus diisi !")
            tahunTerbit.requestFocus()
            return true
        }

        if (jumlahHalaman.text.toString()==""){
            jumlahHalaman.setError("Jumlah Halaman Harus diisi !")
            jumlahHalaman.requestFocus()
            return true
        }

        for (judulItem in daftarJudul){
            if (judul.text.toString()==judulItem && judul.text.toString()!=judulSaatIni){
                statusUniqeJudul=true
                break
            }
        }


        if (statusUniqeJudul==true){
            judul.setError("Judul Sudah Pernah dipakai !")
            judul.requestFocus()
            statusUniqeJudul=false
            return true
        }

        val gambarCoverDpn = findViewById<ImageView>(R.id.gambarCoverDpn)
        val gambarCoverBelakang = findViewById<ImageView>(R.id.coverBelakang)
        // Layout gambar
        val rl_coverDpn = findViewById<RelativeLayout>(R.id.rl_coverDpn)
        val addCoverDpn = findViewById<CardView>(R.id.addCoverDpn)

        val bitmap = (gambarCoverDpn.getDrawable() as BitmapDrawable).bitmap

        if (bitmap==null){
            rl_coverDpn.setBackgroundResource(R.drawable.background_border_relativelayout_red)
            addCoverDpn.focusable
            Toast.makeText(this, "Cover depan harus diisi", Toast.LENGTH_SHORT).show()
            return true
        }

        if (gambarCoverDpn.getDrawable()!=null || gambarCoverBelakang.getDrawable()!=null){
            uploadGambar(judul.text.toString(),penerbit.text.toString(),tahunTerbit.text.toString(),
                penulis1.text.toString(),jumlahHalaman.text.toString())
        }

        if (lenPenulis==1){
            if (editTextList[0].text.toString()==""){
                editTextList[0].setError("Penulis 2 Harus diisi !")
                editTextList[0].requestFocus()
                return true
            }
            firestore?.collection("dataBuku")?.document(idData.toString())
                ?.update(
                    "judul",judul.text.toString(),
                    "penerbit",penerbit.text.toString(),
                    "penulis1",penulis1.text.toString(),
                    "penulis2",editTextList[0].text.toString(),
                    "tahunTerbit",tahunTerbit.text.toString(),
                    "jumlahHalaman",jumlahHalaman.text.toString(),
                    "urlCoverDpn",urlCoverDpn,
                    "urlCoverblkg",urlCoverBlkg
                )
                ?.addOnSuccessListener {
                    successInsertData = true
                }?.addOnFailureListener {
                    successInsertData = false
                }
        }else if(lenPenulis==2){
            if (editTextList[0].text.toString()==""){
                editTextList[0].setError("Penulis 2 Harus diisi !")
                editTextList[0].requestFocus()
                return true
            }
            if (editTextList[1].text.toString()==""){
                editTextList[1].setError("Penulis 3 Harus diisi !")
                editTextList[1].requestFocus()
                return true
            }

            firestore?.collection("dataBuku")?.document(idData.toString())
                ?.update(
                    "judul",judul.text.toString(),
                    "penerbit",penerbit.text.toString(),
                    "penulis1",penulis1.text.toString(),
                    "penulis2",editTextList[0].text.toString(),
                    "penulis3",editTextList[1].text.toString(),
                    "tahunTerbit",tahunTerbit.text.toString(),
                    "jumlahHalaman",jumlahHalaman.text.toString(),
                    "urlCoverDpn",urlCoverDpn,
                    "urlCoverblkg",urlCoverBlkg
                )
                ?.addOnSuccessListener {
                    successInsertData = true
                }?.addOnFailureListener {
                    successInsertData = false
                }
        }else{
            firestore?.collection("dataBuku")?.document(idData.toString())
                ?.update(
                    "judul",judul.text.toString(),
                    "penerbit",penerbit.text.toString(),
                    "penulis1",penulis1.text.toString(),
                    "tahunTerbit",tahunTerbit.text.toString(),
                    "jumlahHalaman",jumlahHalaman.text.toString(),
                    "urlCoverDpn",urlCoverDpn,
                    "urlCoverblkg",urlCoverBlkg
                )
                ?.addOnSuccessListener {
                    successInsertData = true
                }?.addOnFailureListener {
                    successInsertData = false
                }
        }

        return true
    }

    fun pilihGambar(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent,100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val gambarCoverDpn = findViewById<ImageView>(R.id.gambarCoverDpn)
        val gambarCoverBelakang = findViewById<ImageView>(R.id.coverBelakang)

        if (requestCode == 100 && resultCode == RESULT_OK){
            if (statusGambar=="Depan"){
                ImageURI = data?.data!!
                ImageURIList.add(0,ImageURI)
                gambarCoverDpn.getLayoutParams().height = 1200
                gambarCoverDpn.setImageURI(ImageURIList[0])
            }else if (statusGambar=="Belakang"){
                ImageURI = data?.data!!
                if (ImageURIList.size==0){
                    ImageURIList.add(0,ImageURI)
                }
                ImageURIList.add(1,ImageURI)
                gambarCoverBelakang.getLayoutParams().height = 1200
                gambarCoverBelakang.setImageURI(ImageURIList[1])
            }
        }
    }

    fun uploadGambar(judul:String,penerbit: String, tahun: String,penulis1: String,jumlahHalaman: String){
        val gambarCoverDpn = findViewById<ImageView>(R.id.gambarCoverDpn)
        val gambarCoverBelakang = findViewById<ImageView>(R.id.coverBelakang)

        // Input
        val judul_input = findViewById<EditText>(R.id.judulBuku)
        val penerbit_input  = findViewById<EditText>(R.id.namaPenerbit)
        val penulis1_input  = findViewById<EditText>(R.id.penulis1)
        val tahunTerbit_input  = findViewById<EditText>(R.id.tahunTerbit)
        val jumlahHalaman_input  = findViewById<EditText>(R.id.jumlahHalaman)

        val progresLog = ProgressDialog(this)
        progresLog.setMessage("Upload Data ...")
        progresLog.setCancelable(false)
        progresLog.show()

        if (successInsertData==false){
            if (progresLog.isShowing) progresLog.dismiss()
            Toast.makeText(this, "Data gagal ditambahkan", Toast.LENGTH_SHORT).show()
        }else{
            if (gambarCoverDpn.getDrawable()!=null && gambarCoverBelakang.getDrawable()!=null){
                val fileName = UUID.randomUUID().toString()+"|Depan"
                urlCoverDpn = fileName
                val firestorage = FirebaseStorage.getInstance().getReference("image/$fileName")

                val fileNameBelakang = UUID.randomUUID().toString()+"|Belakang"
                urlCoverBlkg = fileNameBelakang

                val bitmap = (gambarCoverDpn.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos)
                val data = baos.toByteArray()
                firestorage.putBytes(data)
                    .addOnSuccessListener {
                        val bitmap2 = (gambarCoverBelakang.drawable as BitmapDrawable).bitmap
                        val baos2 = ByteArrayOutputStream()
                        bitmap2.compress(Bitmap.CompressFormat.JPEG, 25, baos2)
                        val data2 = baos2.toByteArray()

                        val firestorageDua = FirebaseStorage.getInstance().getReference("image/$fileNameBelakang")
                        firestorageDua.putBytes(data2)
                            .addOnSuccessListener {
                                gambarCoverDpn.setImageURI(null)
                                gambarCoverBelakang.setImageURI(null)
                                if (progresLog.isShowing) progresLog.dismiss()

                                val intent = Intent(this, BukuItemList::class.java)
                                startActivity(intent)
                                finish()

                            }.addOnFailureListener {
                                if (progresLog.isShowing) progresLog.dismiss()
                                Toast.makeText(this, "Gambar gagal ditambahkan", Toast.LENGTH_SHORT).show()
                            }
                    }.addOnFailureListener {
                        if (progresLog.isShowing) progresLog.dismiss()
                        Toast.makeText(this, "Gambar gagal ditambahkan", Toast.LENGTH_SHORT).show()
                    }
            }else if (gambarCoverDpn.getDrawable()!=null){
                val fileName = UUID.randomUUID().toString()+"|Depan"
                urlCoverDpn = fileName
                val firestorage = FirebaseStorage.getInstance().getReference("image/$fileName")

                val bitmap = (gambarCoverDpn.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos)
                val data = baos.toByteArray()

                firestorage.putBytes(data)
                    .addOnSuccessListener {
                        gambarCoverDpn.setImageURI(null)
                        if (progresLog.isShowing) progresLog.dismiss()

                        val intent = Intent(this, BukuItemList::class.java)
                        startActivity(intent)
                        finish()
                    }.addOnFailureListener {
                        if (progresLog.isShowing) progresLog.dismiss()
                        Toast.makeText(this, "Gambar gagal ditambahkan", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    fun deleteGambar(gambar : ImageView){
        gambar.setImageBitmap(null)
    }
}