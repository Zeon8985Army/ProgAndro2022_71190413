package id.ac.ukdw.pertemuan6_71190413

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class BFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_b, container, false)

        val btnFrgmt2 = view.findViewById<Button>(R.id.goToThrid)
        btnFrgmt2.setOnClickListener {
            Toast.makeText(context,"Go To Thrird  Page", Toast.LENGTH_SHORT).show()
            val pageThree = Intent(context, ThridPage::class.java)
            startActivity(pageThree)
        }
        return view
    }
}