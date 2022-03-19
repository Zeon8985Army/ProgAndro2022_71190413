package id.ac.ukdw.pertemuan6_71190413

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class AFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_a, container, false)

        val btnFrgmt1 = view.findViewById<Button>(R.id.goToSecond)
        btnFrgmt1.setOnClickListener {
            Toast.makeText(context,"Go To Second Page", Toast.LENGTH_SHORT).show()
            val pageTwo = Intent(context, SecondPage::class.java)
            startActivity(pageTwo)
        }
        return view
    }
}