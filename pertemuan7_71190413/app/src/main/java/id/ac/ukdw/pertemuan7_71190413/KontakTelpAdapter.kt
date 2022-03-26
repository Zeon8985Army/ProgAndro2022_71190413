package id.ac.ukdw.pertemuan7_71190413

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class KontakTelpAdapter(val listKontakTelp : ArrayList<KontakTelp>) :RecyclerView.Adapter<KontakTelpAdapter.KontakTelpHolder>(){
    class  KontakTelpHolder(val v:View):RecyclerView.ViewHolder(v){
        var KontakTelpVar : KontakTelp? = null

        fun bindView(KontakTelp : KontakTelp){
            this.KontakTelpVar = KontakTelp
            v.findViewById<TextView>(R.id.kontakNama1).text = "${KontakTelp.nama}"
            v.findViewById<TextView>(R.id.nomerKontak1).text = "${KontakTelp.nomerTelp}"
            v.findViewById<ImageView>(R.id.imageKontak).setImageResource(KontakTelp.imgKontak)
            v.setOnClickListener {
                if (KontakTelp.wa and  KontakTelp.tg){
                    val i: Intent = Intent( v.context,WaTgKontakDetail::class.java)
                    i.putExtra("namaKontak",KontakTelp.nama)
                    i.putExtra("nomerTelp",KontakTelp.nomerTelp)
                    i.putExtra("email",KontakTelp.email)
                    i.putExtra("imgKontak",KontakTelp.imgKontak)
                    v.context.startActivity(i)
                }else if (KontakTelp.wa){
                    val i: Intent = Intent( v.context,WaKontakDetail::class.java)
                    i.putExtra("namaKontak",KontakTelp.nama)
                    i.putExtra("nomerTelp",KontakTelp.nomerTelp)
                    i.putExtra("email",KontakTelp.email)
                    i.putExtra("imgKontak",KontakTelp.imgKontak)
                    v.context.startActivity(i)
                }else if(KontakTelp.tg){
                    val i: Intent = Intent( v.context,TgKontakDetail::class.java)
                    i.putExtra("namaKontak",KontakTelp.nama)
                    i.putExtra("nomerTelp",KontakTelp.nomerTelp)
                    i.putExtra("email",KontakTelp.email)
                    i.putExtra("imgKontak",KontakTelp.imgKontak)
                    v.context.startActivity(i)
                }else{
                    val i: Intent = Intent( v.context,KontakDetail::class.java)
                    i.putExtra("namaKontak",KontakTelp.nama)
                    i.putExtra("nomerTelp",KontakTelp.nomerTelp)
                    i.putExtra("email",KontakTelp.email)
                    i.putExtra("imgKontak",KontakTelp.imgKontak)
                    v.context.startActivity(i)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KontakTelpHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_kontak_telp, parent, false)
        return KontakTelpHolder(v)
    }

    override fun onBindViewHolder(holder: KontakTelpAdapter.KontakTelpHolder, position: Int) {
        holder.bindView(listKontakTelp[position])
    }

    override fun getItemCount(): Int {
        return listKontakTelp.size
    }
}