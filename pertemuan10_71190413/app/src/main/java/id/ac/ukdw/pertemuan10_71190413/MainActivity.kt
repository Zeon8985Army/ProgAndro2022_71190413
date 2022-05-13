package id.ac.ukdw.pertemuan10_71190413

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    // Variabel untuk SQLite
    lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dbHelper = DatabaseHelper(this)
        db = dbHelper.writableDatabase

        val etNama = findViewById<EditText>(R.id.etNama)
        val etUsia = findViewById<EditText>(R.id.etUsia)
        val etSearch = findViewById<EditText>(R.id.etSearch)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)
        val btnHapus = findViewById<Button>(R.id.btnHapus)
        val btnSearch = findViewById<Button>(R.id.btnSearch)

        btnSimpan.setOnClickListener {
            saveData(etNama.text.toString(), etUsia.text.toString())
        }
        btnHapus.setOnClickListener {
            deleteData(etNama.text.toString(), etUsia.text.toString())
        }
        btnSearch.setOnClickListener {
            searchData(etSearch.text.toString())
        }
        refreshData()
    }

    fun saveData(nama: String, usia: String){
        val values = ContentValues().apply {
            put(DatabaseContract.Penduduk.COLUMN_NAME_NAMA, nama)
            put(DatabaseContract.Penduduk.COLUMN_NAME_USIA, usia)
        }
        db.insert(DatabaseContract.Penduduk.TABLE_NAME, null, values)
        val etNama = findViewById<EditText>(R.id.etNama)
        val etUsia = findViewById<EditText>(R.id.etUsia)
        etNama.setText("")
        etUsia.setText("")
        refreshData()
    }

    fun deleteData(nama: String, usia: String){
        val selection = "${DatabaseContract.Penduduk.COLUMN_NAME_NAMA} LIKE ? OR "+
                "${DatabaseContract.Penduduk.COLUMN_NAME_USIA} LIKE ?"
        val selectionArgs = arrayOf(nama, usia)
        db.delete(DatabaseContract.Penduduk.TABLE_NAME, selection, selectionArgs)
        refreshData()
    }

    @SuppressLint("Range")
    fun searchData(parameter: String){
        var result =""
        val queue = "SELECT * FROM ${DatabaseContract.Penduduk.TABLE_NAME} WHERE " +
                "${DatabaseContract.Penduduk.COLUMN_NAME_NAMA} LIKE ? OR "+
                "${DatabaseContract.Penduduk.COLUMN_NAME_USIA} LIKE ?"
        val selectionArgs = arrayOf(parameter,parameter)
        val cursor = db.rawQuery(queue, selectionArgs)
        var counter =1
        with(cursor!!) {
            while (moveToNext()) {
                result += "${counter.toString()}. ${getString(getColumnIndex(DatabaseContract.Penduduk.COLUMN_NAME_NAMA))} " +
                        "-${getString(getColumnIndex(DatabaseContract.Penduduk.COLUMN_NAME_USIA))} Tahun\n"
                counter=counter+1
            }
        }
        val tvHasilSearch = findViewById<TextView>(R.id.tvHslSearch)
        tvHasilSearch.text = result
        refreshData()
    }

    @SuppressLint("Range")
    fun refreshData(){
        var result =""
        val columns = arrayOf(
            BaseColumns._ID,
            DatabaseContract.Penduduk.COLUMN_NAME_NAMA,
            DatabaseContract.Penduduk.COLUMN_NAME_USIA
        )

        val cursor = db.query(
            DatabaseContract.Penduduk.TABLE_NAME,
            columns,
            null, //klausa where
            null, //argument untuk klause where
            null, //klausa group by
            null,  //klausa having
            null  //klausa order by
        )

        var counter =1
        with(cursor!!) {
            while (moveToNext()) {
                result += "${counter.toString()}. ${getString(getColumnIndex(DatabaseContract.Penduduk.COLUMN_NAME_NAMA))} " +
                        "-${getString(getColumnIndex(DatabaseContract.Penduduk.COLUMN_NAME_USIA))} Tahun\n"
                counter=counter+1
            }
        }
        val tvHasil = findViewById<TextView>(R.id.tvHasil)

        tvHasil.text = result
    }
}