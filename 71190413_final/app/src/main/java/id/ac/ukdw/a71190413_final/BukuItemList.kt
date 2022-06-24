package id.ac.ukdw.a71190413_final

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.firestore.FirebaseFirestore


class BukuItemList : AppCompatActivity(){

    // Variabel Logout
    lateinit var GoogleSout: GoogleSignInClient

    // Variabel firestore
    var firestore: FirebaseFirestore? = null

    // Variabel untuk Search
    lateinit var searchView: SearchView
    var ListDataBuku = ArrayList<DataBuku>()
    var ListDataBukuFiltered = ArrayList<DataBuku>()
    var statusResume :Boolean = false
    

    override fun onResume() {
        super.onResume()

        if (!checkDataUpdate()){
            refreshPage()
        }else{
            if (statusResume){
                refreshPage()
            }
        }

        statusResume = true

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_item_buku)

        // Untuk Toolbar
        setSupportActionBar(findViewById(R.id.toolbarUmum))
        supportActionBar?.setDisplayShowTitleEnabled(true)

        refreshPage()

        val btnAddData= findViewById<CardView>(R.id.addData).setOnClickListener{
            val i: Intent = Intent( this,InsertActivity::class.java)
            startActivity(i)
        }

        searchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
//                Log.d("Tag",query)
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                ListDataBukuFiltered.clear()

                for (data in ListDataBuku){
                    if (data.judul.lowercase()==newText.lowercase() || data.jumlahHalaman.lowercase()==newText.lowercase()
                        || data.penerbit.lowercase()==newText.lowercase() || data.penulis1.lowercase()==newText.lowercase()
                        || data.penulis2.lowercase()==newText.lowercase() || data.penulis3.lowercase()==newText.lowercase()
                        || data.tahunTerbit.lowercase()==newText.lowercase()){
                        ListDataBukuFiltered.add(data)
                        refreshPage(ListDataBukuFiltered)
                    }else if(newText.lowercase()==""){
                        refreshPage2()
                    }
                }
                return false
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu, menu)
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
                    startActivity(intent)
                    finish()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun refreshPage(listBukuFiltered : ArrayList<DataBuku> = ArrayList()){
        if (listBukuFiltered.size!=0) {
            val rcyBuku = findViewById<RecyclerView>(R.id.rcyItemBuku)
            rcyBuku.layoutManager = LinearLayoutManager(this)
            val KontakTelpAdapter = DataBukuAdapter(listBukuFiltered)
            rcyBuku.adapter = KontakTelpAdapter
        }else{
            ListDataBuku.clear()
            firestore = FirebaseFirestore.getInstance()

            val progresLog = ProgressDialog(this)
            progresLog.setMessage("Load File ...")
            progresLog.setCancelable(false)
            progresLog.show()

            firestore?.collection("dataBuku")?.get()!!
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        ListDataBuku.add(
                            DataBuku(
                                judul = document.data["judul"].toString(),
                                penerbit =  document.data["penerbit"].toString(),
                                penulis1 =  document.data["penulis1"].toString(),
                                penulis2 =  document.data["penulis2"].toString(),
                                penulis3 =  document.data["penulis3"].toString(),
                                tahunTerbit =  document.data["tahunTerbit"].toString(),
                                jumlahHalaman =  document.data["jumlahHalaman"].toString(),
                                urlCoverDpn =  document.data["urlCoverDpn"].toString(),
                                urlCoverblkg =  document.data["urlCoverblkg"].toString()
                            )
                        )
                    }
                    val rcyBuku = findViewById<RecyclerView>(R.id.rcyItemBuku)
                    rcyBuku.layoutManager = LinearLayoutManager(this)
                    val KontakTelpAdapter = DataBukuAdapter(ListDataBuku)
                    rcyBuku.adapter = KontakTelpAdapter

                    if (progresLog.isShowing) progresLog.dismiss()

                }
        }
    }

    fun refreshPage2(){
        firestore = FirebaseFirestore.getInstance()

        firestore?.collection("dataBuku")?.get()!!
            .addOnSuccessListener { documents ->
                ListDataBuku.clear()
                for (document in documents) {
                    ListDataBuku.add(
                        DataBuku(
                            judul = document.data["judul"].toString(),
                            penerbit =  document.data["penerbit"].toString(),
                            penulis1 =  document.data["penulis1"].toString(),
                            penulis2 =  document.data["penulis2"].toString(),
                            penulis3 =  document.data["penulis3"].toString(),
                            tahunTerbit =  document.data["tahunTerbit"].toString(),
                            jumlahHalaman =  document.data["jumlahHalaman"].toString(),
                            urlCoverDpn =  document.data["urlCoverDpn"].toString(),
                            urlCoverblkg =  document.data["urlCoverblkg"].toString()
                        )
                    )
                }
                val rcyBuku = findViewById<RecyclerView>(R.id.rcyItemBuku)
                rcyBuku.layoutManager = LinearLayoutManager(this)
                val KontakTelpAdapter = DataBukuAdapter(ListDataBuku)
                rcyBuku.adapter = KontakTelpAdapter

            }
    }

    fun checkDataUpdate():Boolean{
        var ListDataLama = ArrayList<DataBuku>()

        firestore?.collection("dataBuku")?.get()!!
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    ListDataLama.add(
                        DataBuku(
                            judul = document.data["judul"].toString(),
                            penerbit = document.data["penerbit"].toString(),
                            penulis1 = document.data["penulis1"].toString(),
                            penulis2 = document.data["penulis2"].toString(),
                            penulis3 = document.data["penulis3"].toString(),
                            tahunTerbit = document.data["tahunTerbit"].toString(),
                            jumlahHalaman = document.data["jumlahHalaman"].toString(),
                            urlCoverDpn = document.data["urlCoverDpn"].toString(),
                            urlCoverblkg = document.data["urlCoverblkg"].toString()
                        )
                    )
                }
            }

        if(ListDataBuku.size==ListDataLama.size){
            return true
        }else{
            return false
        }
    }

}