package id.ac.ukdw.a71190413_final

data class DataBuku(val judul: String, val penerbit: String,
                    val penulis1: String, val penulis2: String = "",
                    val penulis3: String = "", val tahunTerbit: String,
                    val jumlahHalaman: String, val urlCoverDpn: String,
                    val urlCoverblkg: String ="")
