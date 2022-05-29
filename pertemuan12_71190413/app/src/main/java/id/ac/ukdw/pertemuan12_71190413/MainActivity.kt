package id.ac.ukdw.pertemuan12_71190413

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var btnProses = findViewById<Button>(R.id.btnProces)
        var namaKota = findViewById<EditText>(R.id.namaKota)




        btnProses.setOnClickListener {
            cekRamalanCuaca(namaKota.text.toString())
        }
    }

    // fungsi untuk cek ramalan cuaca
    fun cekRamalanCuaca(kota :String){
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.openweathermap.org/data/2.5/forecast?q=${kota}&appid=dcfe71645d5a29637beb98fad9380007"


        // Request a string response from the provided URL.
        val stringRequest = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->
                val data = JSONObject(response)
                val cuacaToday = data.getJSONArray("list").getJSONObject(0)
                    .getJSONArray("weather").getJSONObject(0).getString("description")
                val cuacaTomorow = data.getJSONArray("list").getJSONObject(8)
                    .getJSONArray("weather").getJSONObject(0).getString("description")
                val cuacaLusa = data.getJSONArray("list").getJSONObject(16)
                    .getJSONArray("weather").getJSONObject(0).getString("description")

                val cuacaDateToday = data.getJSONArray("list").getJSONObject(0)
                    .getString("dt_txt").split(" ")
                val cuacaDateTomorow = data.getJSONArray("list").getJSONObject(8)
                    .getString("dt_txt").split(" ")
                val cuacaDateLusa = data.getJSONArray("list").getJSONObject(16)
                    .getString("dt_txt").split(" ")

                // Variabel View Elemen
                // Date
                var todayDate = findViewById<TextView>(R.id.todayDate)
                var tomorowDate = findViewById<TextView>(R.id.tomoroDate)
                var lusaDate = findViewById<TextView>(R.id.lusaDate)

                // Result
                var resultToday = findViewById<TextView>(R.id.resultToday)
                var resultTomorow = findViewById<TextView>(R.id.resultTomorow)
                var resultLusa = findViewById<TextView>(R.id.resultLusa)


                todayDate.text = cuacaDateToday[0].toString()
                tomorowDate.text = cuacaDateTomorow[0].toString()
                lusaDate.text = cuacaDateLusa[0].toString()

                resultToday.text = cuacaToday.toString()
                resultTomorow.text = cuacaTomorow.toString()
                resultLusa.text = cuacaLusa.toString()

            },
            Response.ErrorListener {
                var resultToday = findViewById<TextView>(R.id.resultToday)
                resultToday.text = "City is not found."

            })

        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }


}