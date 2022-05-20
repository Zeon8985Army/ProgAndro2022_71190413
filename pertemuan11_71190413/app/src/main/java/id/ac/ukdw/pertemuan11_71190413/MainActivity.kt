package id.ac.ukdw.pertemuan11_71190413

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity() {
    var firestore: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firestore = FirebaseFirestore.getInstance()

        // Inputan
        val inpNIM = findViewById<EditText>(R.id.etNIM)
        val inpName = findViewById<EditText>(R.id.etNama)
        val inpIPK = findViewById<EditText>(R.id.etIPK)
        val inpSRC = findViewById<EditText>(R.id.etSearch)

        // Radio
        val rgSRT = findViewById<RadioGroup>(R.id.rgSort)
        val rdASC = findViewById<RadioButton>(R.id.rbASC)
        val rdDSC = findViewById<RadioButton>(R.id.rbDSC)
        val rdUnSet = findViewById<RadioButton>(R.id.rbClear)

        // Button
        val btnSMPN = findViewById<Button>(R.id.btnSimpan)
        val btnSRC = findViewById<Button>(R.id.btnSearch)

        // OUTPUT
        val txtData = findViewById<TextView>(R.id.tvData)
        val txtHSLSRC = findViewById<TextView>(R.id.tvHslSearch)

        fun tampilinData() {
            firestore?.collection("dataMahasiswa")?.get()!!
                .addOnSuccessListener { documents ->
                    var dataIsi = ""
                    var counter = 1
                    for (document in documents) {
                        dataIsi += "${counter.toString()}. ${document["nim"]} - ${document["nama"]}" +
                                " - ${String.format("%.2f", document["ipk"])}\n"
                        counter = counter + 1
                    }
                    txtData.text = dataIsi
                }
        }
        tampilinData()
        fun search(src: String) {
            Toast.makeText(this, "Sedang dicari", Toast.LENGTH_LONG).show()
            firestore?.collection("dataMahasiswa")?.whereEqualTo("nim", src)
                ?.get()!!
                .addOnSuccessListener { documents ->
                    var dataIsi = ""
                    var counter = 1
                    var statusSrcNimAda = false
                    for (document in documents) {
                        counter = counter + 1
                    }
                    if (counter > 1) {
                        statusSrcNimAda = true
                    }
                    if (statusSrcNimAda) {
                        var counter = 1
                        for (document in documents) {
                            dataIsi += "${counter.toString()}. ${document["nim"]} - ${document["nama"]}" +
                                    " - ${String.format("%.2f", document["ipk"])}\n"
                            counter = counter + 1
                        }
                        txtHSLSRC.text = dataIsi
                        Toast.makeText(this, "Data sudah keluar", Toast.LENGTH_SHORT).show()
                    } else {
                        firestore?.collection("dataMahasiswa")?.whereEqualTo("nama", src)
                            ?.get()!!
                            .addOnSuccessListener { documents ->
                                var dataIsi = ""
                                var counter = 1
                                var statusSrcNameAda = false
                                for (document in documents) {
                                    counter = counter + 1
                                }
                                if (counter > 1) {
                                    statusSrcNameAda = true
                                }
                                if (statusSrcNameAda) {
                                    var counter = 1
                                    for (document in documents) {
                                        dataIsi += "${counter.toString()}. ${document["nim"]} - ${document["nama"]}" +
                                                " - ${String.format("%.2f", document["ipk"])}\n"
                                        counter = counter + 1
                                    }
                                    txtHSLSRC.text = dataIsi
                                    Toast.makeText(this, "Data sudah keluar", Toast.LENGTH_SHORT).show()
                                } else {
                                    firestore?.collection("dataMahasiswa")
                                        ?.whereEqualTo("ipk", src.toFloat())
                                        ?.get()!!
                                        .addOnSuccessListener { documents ->
                                            var dataIsi = ""
                                            var counter = 1
                                            var statusSrcIPKAda = false
                                            for (document in documents) {
                                                counter = counter + 1
                                            }
                                            if (counter > 1) {
                                                statusSrcIPKAda = true
                                            }
                                            if (statusSrcIPKAda) {
                                                var counter = 1
                                                for (document in documents) {
                                                    dataIsi += "${counter.toString()}. ${document["nim"]} - ${document["nama"]}" +
                                                            " - ${
                                                                String.format(
                                                                    "%.2f",
                                                                    document["ipk"]
                                                                )
                                                            }\n"
                                                    counter = counter + 1
                                                }
                                                txtHSLSRC.text = dataIsi
                                                Toast.makeText(this, "Data sudah keluar", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                }
                            }
                    }
                }
        }
        fun searchASC(src: String) {
            Toast.makeText(this, "Sedang dicari", Toast.LENGTH_LONG).show()
            firestore?.collection("dataMahasiswa")?.whereEqualTo("nim", src)
                ?.orderBy("nim", Query.Direction.ASCENDING)?.get()!!
                .addOnSuccessListener { documents ->
                    var dataIsi = ""
                    var counter = 1
                    var statusSrcNimAda = false
                    for (document in documents) {
                        counter = counter + 1
                    }
                    if (counter > 1) {
                        statusSrcNimAda = true
                    }
                    if (statusSrcNimAda) {
                        var counter = 1
                        for (document in documents) {
                            dataIsi += "${counter.toString()}. ${document["nim"]} - ${document["nama"]}" +
                                    " - ${String.format("%.2f", document["ipk"])}\n"
                            counter = counter + 1
                        }
                        txtHSLSRC.text = dataIsi
                        Toast.makeText(this, "Data sudah keluar", Toast.LENGTH_SHORT).show()
                    } else {
                        firestore?.collection("dataMahasiswa")?.whereEqualTo("nama", src)
                            ?.orderBy("nim", Query.Direction.ASCENDING)?.get()!!
                            .addOnSuccessListener { documents ->
                                var dataIsi = ""
                                var counter = 1
                                var statusSrcNameAda = false
                                for (document in documents) {
                                    counter = counter + 1
                                }
                                if (counter > 1) {
                                    statusSrcNameAda = true
                                }
                                if (statusSrcNameAda) {
                                    var counter = 1
                                    for (document in documents) {
                                        dataIsi += "${counter.toString()}. ${document["nim"]} - ${document["nama"]}" +
                                                " - ${String.format("%.2f", document["ipk"])}\n"
                                        counter = counter + 1
                                    }
                                    txtHSLSRC.text = dataIsi
                                    Toast.makeText(this, "Data sudah keluar", Toast.LENGTH_SHORT).show()
                                } else {
                                    firestore?.collection("dataMahasiswa")
                                        ?.whereEqualTo("ipk", src.toFloat())
                                        ?.orderBy("nim", Query.Direction.ASCENDING)?.get()!!
                                        .addOnSuccessListener { documents ->
                                            var dataIsi = ""
                                            var counter = 1
                                            var statusSrcIPKAda = false
                                            for (document in documents) {
                                                counter = counter + 1
                                            }
                                            if (counter > 1) {
                                                statusSrcIPKAda = true
                                            }
                                            if (statusSrcIPKAda) {
                                                var counter = 1
                                                for (document in documents) {
                                                    dataIsi += "${counter.toString()}. ${document["nim"]} - ${document["nama"]}" +
                                                            " - ${
                                                                String.format(
                                                                    "%.2f",
                                                                    document["ipk"]
                                                                )
                                                            }\n"
                                                    counter = counter + 1
                                                }
                                                txtHSLSRC.text = dataIsi
                                                Toast.makeText(this, "Data sudah keluar", Toast.LENGTH_SHORT).show()

                                            }
                                        }
                                }
                            }
                    }
                }
        }
        fun searchDSC(src: String) {
            Toast.makeText(this, "Sedang dicari", Toast.LENGTH_LONG).show()
            firestore?.collection("dataMahasiswa")?.whereEqualTo("nim", src)
                ?.orderBy("nim", Query.Direction.DESCENDING)?.get()!!
                .addOnSuccessListener { documents ->
                    var dataIsi = ""
                    var counter = 1
                    var statusSrcNimAda = false
                    for (document in documents) {
                        counter = counter + 1
                    }
                    if (counter > 1) {
                        statusSrcNimAda = true
                    }
                    if (statusSrcNimAda) {
                        var counter = 1
                        for (document in documents) {
                            dataIsi += "${counter.toString()}. ${document["nim"]} - ${document["nama"]}" +
                                    " - ${String.format("%.2f", document["ipk"])}\n"
                            counter = counter + 1
                        }
                        txtHSLSRC.text = dataIsi
                        Toast.makeText(this, "Data sudah keluar", Toast.LENGTH_SHORT).show()
                    } else {
                        firestore?.collection("dataMahasiswa")?.whereEqualTo("nama", src)
                            ?.orderBy("nim", Query.Direction.DESCENDING)?.get()!!
                            .addOnSuccessListener { documents ->
                                var dataIsi = ""
                                var counter = 1
                                var statusSrcNameAda = false
                                for (document in documents) {
                                    counter = counter + 1
                                }
                                if (counter > 1) {
                                    statusSrcNameAda = true
                                }
                                if (statusSrcNameAda) {
                                    var counter = 1
                                    for (document in documents) {
                                        dataIsi += "${counter.toString()}. ${document["nim"]} - ${document["nama"]}" +
                                                " - ${String.format("%.2f", document["ipk"])}\n"
                                        counter = counter + 1
                                    }
                                    txtHSLSRC.text = dataIsi
                                    Toast.makeText(this, "Data sudah keluar", Toast.LENGTH_SHORT).show()
                                } else {
                                    firestore?.collection("dataMahasiswa")
                                        ?.whereEqualTo("ipk", src.toFloat())
                                        ?.orderBy("nim", Query.Direction.DESCENDING)?.get()!!
                                        .addOnSuccessListener { documents ->
                                            var dataIsi = ""
                                            var counter = 1
                                            var statusSrcIPKAda = false
                                            for (document in documents) {
                                                counter = counter + 1
                                            }
                                            if (counter > 1) {
                                                statusSrcIPKAda = true
                                            }
                                            if (statusSrcIPKAda) {
                                                var counter = 1
                                                for (document in documents) {
                                                    dataIsi += "${counter.toString()}. ${document["nim"]} - ${document["nama"]}" +
                                                            " - ${
                                                                String.format(
                                                                    "%.2f",
                                                                    document["ipk"]
                                                                )
                                                            }\n"
                                                    counter = counter + 1
                                                }
                                                txtHSLSRC.text = dataIsi
                                                Toast.makeText(this, "Data sudah keluar", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                }
                            }
                    }
                }
        }
            btnSMPN.setOnClickListener {
                val dataMahasiswa = DataMahasiswa(
                    inpNIM.text.toString(),
                    inpName.text.toString(),
                    inpIPK.text.toString().toFloat()
                )
                Toast.makeText(this, "Data berhasil ditambahkan !", Toast.LENGTH_SHORT).show()
                firestore?.collection("dataMahasiswa")?.add(dataMahasiswa)
                tampilinData()
            }

            btnSRC.setOnClickListener {
                if (rdASC.isChecked()) {
                    if (inpSRC.text.toString() != "" && inpSRC.text.toString() != null) {
                        searchASC(inpSRC.text.toString())
                    }
                }else if(rdDSC.isChecked()){
                    if (inpSRC.text.toString() != "" && inpSRC.text.toString() != null) {
                        searchDSC(inpSRC.text.toString())
                    }
                }else{
                    if (inpSRC.text.toString() != "" && inpSRC.text.toString() != null) {
                        search(inpSRC.text.toString())
                    }
                }
            }

            rgSRT.setOnCheckedChangeListener { radioGroup, i ->
                if (rdUnSet.isChecked() ){
                    rdASC.setChecked(false)
                    rdDSC.setChecked(false)
                }
            }
    }

}