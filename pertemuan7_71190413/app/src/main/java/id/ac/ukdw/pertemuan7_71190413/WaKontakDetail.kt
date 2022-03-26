package id.ac.ukdw.pertemuan7_71190413

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class WaKontakDetail : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_kontak_wa)

        // Variabel penampung element di detail
        val kontakNama : TextView = findViewById(R.id.tv_namaKontak)
        val nomerHp : TextView = findViewById(R.id.tv_nomorTelp)
        val email : TextView = findViewById(R.id.tv_email)
        val img : ImageView = findViewById(R.id.imgKontak)

        val waChat : TextView = findViewById(R.id.tv_whatsappChat)
        val waCall : TextView = findViewById(R.id.tv_whatsappCall)


        kontakNama.text = intent.getStringExtra("namaKontak").toString()
        nomerHp.text = intent.getStringExtra("nomerTelp").toString()
        email.text = intent.getStringExtra("email").toString()
        img.setImageResource(intent.getIntExtra("imgKontak",1))

        waChat.text = "Message "+intent.getStringExtra("nomerTelp").toString()
        waCall.text = "Call "+intent.getStringExtra("nomerTelp").toString()

    }
}