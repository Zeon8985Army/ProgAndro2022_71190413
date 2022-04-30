package id.ac.ukdw.pertemuan9_71190413

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {
    // Variabel SharedPreferences
    var sharedPref: SharedPreferences? = null
    var spEdit: SharedPreferences.Editor? = null

    // Variabel untuk bahasa
    var list = arrayOf<String>("Select Language","Indonesia","English","Français")
    lateinit var locale: Locale
    private var currentLanguage = ""
    private var currentLang: String? = null

    // Variabel untuk log
    private val TAG = "MyActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // SharedPreferences
        sharedPref = getSharedPreferences("sp_session_lang_txtSize", Context.MODE_PRIVATE)
        spEdit = sharedPref?.edit()

        setContentView(R.layout.activity_main)

        val usernameData = sharedPref!!.getString("username", "")
        var usernameView: TextView = findViewById(R.id.tv_welcomeUser)
        val linearLayout: LinearLayout = findViewById(R.id.ll_welcomeUser)

        if (usernameData.toString().length > 4){
            linearLayout.orientation= LinearLayout.VERTICAL
            usernameView.text =  usernameView.text.toString()+usernameData.toString()
        }else{
            usernameView.text =  usernameView.text.toString()+usernameData.toString()
        }

        val btnLogOut: Button = findViewById(R.id.btn_logout)
        btnLogOut.setOnClickListener {
            spEdit?.putBoolean("logIn", false)
            spEdit?.putString("username", "")
            spEdit?.commit()
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        }

        // Untuk nampung bahasa yang dipilih sebelumnya
        currentLanguage = sharedPref!!.getString("langStrSelected", "").toString()

        // Area Spinner
        val spnrBahasa = findViewById<Spinner>(R.id.spnr_daftar_bahasa)
        val adapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list)
        adapter.setDropDownViewResource(R.layout.spinner_edit)
        val selectedLang = sharedPref!!.getInt("lang", 0)
        spnrBahasa.adapter = adapter
        spnrBahasa.setSelection(selectedLang)
        spnrBahasa.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                spEdit?.putInt("lang", position)
                spEdit?.commit()
                when (position) {
                    0 -> {
                    }
                    1 -> setLocale("in")
                    2 -> setLocale("en")
                    3 -> setLocale("fr")
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // ...
            }
        }

        // Area Teks Size
        val textSize = findViewById<EditText>(R.id.edt_teksSize)
        val hlmDev = findViewById<TextView>(R.id.tv_hlmDev)

        // set teksSize dari nilai yang dibawak sebelum perubahan bahasa terjadi
        hlmDev.setTextSize(sharedPref!!.getFloat("fontSize", 17F))
        textSize.setText((sharedPref!!.getFloat("fontSize", 17F)).toString())

        var valueTeksSizeFloat = 1.0F

        textSize.doAfterTextChanged {
            spEdit?.commit()
            val valueTeksSizeStr = textSize.text.toString()
            try {
                textSize.text.toString().toFloat()
                valueTeksSizeFloat = textSize.text.toString().toFloat()

                // menyimpan teksSize yang dipilih
                spEdit?.putFloat("fontSize", valueTeksSizeFloat)
                spEdit?.commit()

            }catch (e: Exception){
                Toast.makeText(
                    this@MainActivity, "Input is null !", Toast.LENGTH_SHORT).show()
            }

            if (valueTeksSizeStr!=""){
                println(valueTeksSizeStr)

                if(valueTeksSizeFloat>100){
                    Toast.makeText(
                        this@MainActivity, "Font Size is to large !", Toast.LENGTH_SHORT).show()
                }else if(valueTeksSizeFloat<5){
                    Toast.makeText(
                        this@MainActivity, "Font Size is to small !", Toast.LENGTH_SHORT).show()
                }else{
                    hlmDev.setTextSize(valueTeksSizeFloat)
                }
            }


        }
    }

    // Fungsi untuk ganti bahasa
    private fun setLocale(localeName: String) {
        // Check bahasa yang pilih sebelumnya udh diganti blm, kalau blm ke ganti, maka refresh ke
        // bahasa tersebut

        if (localeName != currentLanguage) {
            locale = Locale(localeName)
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = locale
            res.updateConfiguration(conf, dm)
            val refresh = Intent(
                this,
                MainActivity::class.java
            )
            spEdit?.putString("langStrSelected", localeName)
            spEdit?.commit()
            startActivity(refresh)
        }
    }
    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
        exitProcess(0)
    }
}