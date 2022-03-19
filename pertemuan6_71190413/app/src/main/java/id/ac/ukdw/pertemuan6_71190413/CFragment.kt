package id.ac.ukdw.pertemuan6_71190413

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class CFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_c, container, false)

        val btnFrgmt3 = view.findViewById<Button>(R.id.goToFirst)
        btnFrgmt3.setOnClickListener {
            Toast.makeText(context,"Go To First  Page", Toast.LENGTH_SHORT).show()
            val pageFirst = Intent(context, MainActivity::class.java)
            startActivity(pageFirst)
        }
        return view
    }
}