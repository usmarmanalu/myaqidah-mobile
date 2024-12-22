package com.dicoding.myaqidahmobile.ui.forgotpassword

import android.app.*
import android.graphics.drawable.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.activity.*
import androidx.appcompat.app.*
import androidx.core.content.*
import com.dicoding.myaqidahmobile.*
import com.dicoding.myaqidahmobile.core.utils.DrawableUtils.getWhiteBackArrowDrawable
import com.dicoding.myaqidahmobile.databinding.*
import com.google.firebase.auth.*

@Suppress("DEPRECATION")
class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple2)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Lupa Password"
            setHomeAsUpIndicator(getWhiteBackArrowDrawable(this@ForgotPasswordActivity))
            setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this@ForgotPasswordActivity,
                        R.color.purple2
                    )
                )
            )
        }

        auth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengirim permintaan reset password...")
        progressDialog.setCancelable(false)

        // Event handler untuk tombol reset password
        binding.btnResetPassword.setOnClickListener {
            val email = binding.editEmail.text.toString().trim()
            if (email.isNotEmpty()) {
                resetPassword(email)
            } else {
                Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun resetPassword(email: String) {
        progressDialog.show()
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            progressDialog.dismiss()
            if (task.isSuccessful) {
                Toast.makeText(this, "Email reset password telah dikirim", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Gagal mengirim email reset password", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
