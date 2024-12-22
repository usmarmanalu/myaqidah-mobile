package com.dicoding.myaqidahmobile.core.helper

import com.dicoding.myaqidahmobile.core.firebasemodel.*
import com.google.firebase.auth.*
import com.google.firebase.firestore.*

@Suppress("LABEL_NAME_CLASH")
class FirebaseRegistrasiOnlineHelper(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    /**
     * Simpan data registrasi berdasarkan nama pengguna.
     */

    fun saveDataRegistrasiOnline(
        userData: RegistrasiOnline,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onFailure(Exception("User tidak ditemukan. Silakan login ulang."))
            return
        }

        // Ambil nama pengguna dari koleksi 'users'
        db.collection("users").document(userId)
            .collection("dataUser").document(userId)
            .get()
            .addOnSuccessListener { document ->
                val userName = document.getString("name")
                if (userName.isNullOrEmpty()) {
                    onFailure(Exception("Nama pengguna tidak ditemukan di koleksi 'users'"))
                    return@addOnSuccessListener
                }

                // Simpan data registrasi dan data pasien ke dalam koleksi 'data_pasien'
                db.collection("registrasi_online")
                    .document(userName)
                    .collection("data_pasien")
                    .document(userName)
                    .set(userData)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { exception -> onFailure(exception) }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }


    /**
     * Ambil data registrasi berdasarkan nama pengguna.
     */
    fun fetchDataRegistrasi(
        onSuccess: (RegistrasiOnline) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onFailure(Exception("User tidak ditemukan. Silakan login ulang."))
            return
        }

        // Ambil nama pengguna dari koleksi 'users'
        db.collection("users").document(userId)
            .collection("dataUser").document(userId)
            .get()
            .addOnSuccessListener { document ->
                val userName = document.getString("name")
                if (userName.isNullOrEmpty()) {
                    onFailure(Exception("Nama pengguna tidak ditemukan di koleksi 'users'"))
                    return@addOnSuccessListener
                }

                // Ambil data dari koleksi 'data_pasien' dalam dokumen 'userName' di koleksi 'registrasi_online'
                db.collection("registrasi_online")
                    .document(userName)
                    .collection("data_pasien")
                    .document(userName)
                    .get()
                    .addOnSuccessListener { regDoc ->
                        val data = regDoc.toObject(RegistrasiOnline::class.java)
                        if (data != null) {
                            onSuccess(data)
                        } else {
                            onFailure(Exception("Data registrasi tidak ditemukan di koleksi 'data_pasien'"))
                        }
                    }
                    .addOnFailureListener { exception ->
                        onFailure(exception)
                    }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    /**
     * Simpan data keluarga berdasarkan nama pengguna.
     */

    fun saveDataKeluarga(
        dataKeluarga: DataKeluargaPasien,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onFailure(Exception("User tidak ditemukan"))
            return
        }

        db.collection("users").document(userId)
            .collection("dataUser").document(userId)
            .get()
            .addOnSuccessListener { document ->
                val userName = document.getString("name")
                if (userName.isNullOrEmpty()) {
                    onFailure(Exception("Nama pengguna tidak ditemukan di koleksi 'users'"))
                    return@addOnSuccessListener
                }

                // Simpan data keluarga dengan userName sebagai ID dokumen
                db.collection("registrasi_online")
                    .document(userName)
                    .collection("data_keluarga")
                    .document(userName)
                    .set(dataKeluarga)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { exception -> onFailure(exception) }
            }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    /**
     * Ambil data keluarga berdasarkan nama pengguna.
     */
    fun fetchDataKeluarga(
        onSuccess: (DataKeluargaPasien) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onFailure(Exception("User tidak ditemukan"))
            return
        }

        db.collection("users").document(userId)
            .collection("dataUser").document(userId)
            .get()
            .addOnSuccessListener { document ->
                val userName = document.getString("name")
                if (userName.isNullOrEmpty()) {
                    onFailure(Exception("Nama pengguna tidak ditemukan di koleksi 'users'"))
                    return@addOnSuccessListener
                }

                // Mengambil data keluarga dari koleksi 'data_keluarga' berdasarkan userName
                db.collection("registrasi_online")
                    .document(userName)
                    .collection("data_keluarga")
                    .document(userName)
                    .get()
                    .addOnSuccessListener { docSnapshot ->
                        val data = docSnapshot.toObject(DataKeluargaPasien::class.java)
                        if (data != null) {
                            onSuccess(data)
                        } else {
                            onFailure(Exception("Data keluarga tidak ditemukan."))
                        }
                    }
                    .addOnFailureListener { exception ->
                        onFailure(exception)
                    }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    /**
     * Simpan data janji temu berdasarkan nama pengguna.
     */

    fun saveDataJanjiTemu(
        janjiTemu: JanjiTemuDokter,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onFailure(Exception("User tidak ditemukan. Silakan login ulang."))
            return
        }

        // Ambil nama pengguna dari koleksi 'users'
        db.collection("users").document(userId).collection("dataUser").document(userId).get()
            .addOnSuccessListener { document ->
                val userName = document.getString("name")
                if (userName.isNullOrEmpty()) {
                    onFailure(Exception("Nama pengguna tidak ditemukan di koleksi 'users'"))
                    return@addOnSuccessListener
                }

                val janjiTemuRef = db.collection("registrasi_online")
                    .document(userName)
                    .collection("data_janji_temu")

                janjiTemuRef.add(janjiTemu)
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener { exception ->
                        onFailure(exception)
                    }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    /**
     * Ambil data janji temu berdasarkan nama pengguna.
     */
    fun fetchDataJanjiTemu(
        onSuccess: (JanjiTemuDokter) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onFailure(Exception("User tidak ditemukan. Silakan login ulang."))
            return
        }

        db.collection("users").document(userId)
            .collection("dataUser").document(userId)
            .get()
            .addOnSuccessListener { document ->
                val userName = document.getString("name")
                if (userName.isNullOrEmpty()) {
                    onFailure(Exception("Nama pengguna tidak ditemukan di koleksi 'users'"))
                    return@addOnSuccessListener
                }

                db.collection("registrasi_online")
                    .document(userName)
                    .collection("data_janji_temu")
                    .orderBy(
                        "dateTimestamp",
                        Query.Direction.DESCENDING
                    )
                    .limit(1)
                    .get()
                    .addOnSuccessListener { result ->
                        if (result.isEmpty) {
                            onFailure(Exception("Data janji temu tidak ditemukan."))
                            return@addOnSuccessListener
                        }

                        // Ambil dokumen terbaru
                        val latestJanjiTemu =
                            result.documents.first().toObject(JanjiTemuDokter::class.java)
                        if (latestJanjiTemu != null) {
                            onSuccess(latestJanjiTemu)
                        } else {
                            onFailure(Exception("Data janji temu tidak ditemukan."))
                        }
                    }
                    .addOnFailureListener { exception ->
                        onFailure(exception)
                    }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    /**
     * Ambil data history janji temu berdasarkan nama pengguna.
     */
    fun fetchDataJanjiTemuHistory(
        onSuccess: (List<JanjiTemuDokter>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onFailure(Exception("User tidak ditemukan. Silakan login ulang."))
            return
        }

        db.collection("users").document(userId)
            .collection("dataUser").document(userId)
            .get()
            .addOnSuccessListener { document ->
                val userName = document.getString("name")
                if (userName.isNullOrEmpty()) {
                    onFailure(Exception("Nama pengguna tidak ditemukan di koleksi 'users'"))
                    return@addOnSuccessListener
                }

                db.collection("registrasi_online")
                    .document(userName)
                    .collection("data_janji_temu")
                    .orderBy("dateTimestamp", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener { result ->
                        if (result.isEmpty) {
                            onFailure(Exception("Data janji temu tidak ditemukan."))
                            return@addOnSuccessListener
                        }

                        // Ambil semua dokumen sebagai list
                        val janjiTemuList =
                            result.documents.mapNotNull { it.toObject(JanjiTemuDokter::class.java) }

                        if (janjiTemuList.isNotEmpty()) {
                            onSuccess(janjiTemuList)
                        } else {
                            onFailure(Exception("Data janji temu tidak ditemukan."))
                        }
                    }
                    .addOnFailureListener { exception ->
                        onFailure(exception)
                    }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

}
