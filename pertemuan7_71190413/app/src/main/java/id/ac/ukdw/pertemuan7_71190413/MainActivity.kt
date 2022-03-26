package id.ac.ukdw.pertemuan7_71190413

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ListKontakTelp = ArrayList<KontakTelp>()
        ListKontakTelp.add(KontakTelp("Mr. Anderson","08567857312",
            "smith.anderson@matrix.com",R.mipmap.persona1_foreground,true,true))
        ListKontakTelp.add(KontakTelp("Mr. Matius","0813344552",
            "matius.harnanto@gmail.com",R.mipmap.persona2_foreground,true,false))
        ListKontakTelp.add(KontakTelp("Mr. Perteson","08333457937",
            "banito.perteson@matrix.com",R.mipmap.persona3_foreground,false,false))
        ListKontakTelp.add(KontakTelp("Mrs. Sinta","0879911222",
            "sinta.sucahyo@matrix.com",R.mipmap.persona4_foreground,false,true))

        val rcySong = findViewById<RecyclerView>(R.id.rcy_kontakTelp)
        rcySong.layoutManager = LinearLayoutManager(this)
        val KontakTelpAdapter = KontakTelpAdapter(ListKontakTelp)
        rcySong.adapter = KontakTelpAdapter
    }
}