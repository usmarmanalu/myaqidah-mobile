package com.dicoding.myaqidahmobile.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import com.dicoding.myaqidahmobile.*
import com.dicoding.myaqidahmobile.databinding.FragmentProfileBinding
import com.dicoding.myaqidahmobile.ui.onboarding.OnboardingScreenActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        binding.btnLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        // Set text for each menu item
        binding.menuProfilePasien.titleTextView.text = "Profil Pasien"
        binding.menuBerikanPenilaian.titleTextView.text = "Berikan Penilaian"
        binding.menuSyaratKetentuan.titleTextView.text = "Syarat & Ketentuan"
        binding.menuBahasa.titleTextView.text = "Bahasa"
        binding.menuPusatBantuan.titleTextView.text = "Pusat Bantuan"

        // Set icon for each menu item
        binding.menuProfilePasien.iconImageView.setImageResource(R.drawable.icon_person)
        binding.menuBerikanPenilaian.iconImageView.setImageResource(R.drawable.icon_penilaian)
        binding.menuSyaratKetentuan.iconImageView.setImageResource(R.drawable.icon_terms)
        binding.menuBahasa.iconImageView.setImageResource(R.drawable.icon_language)
        binding.menuPusatBantuan.iconImageView.setImageResource(R.drawable.icon_help)
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Konfirmasi Logout")
            .setMessage("Apakah Anda yakin ingin keluar?")
            .setPositiveButton("Ya") { _, _ ->
                logout()
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss() // Dismiss the dialog if user selects "No"
            }
            .create()
            .show()
    }

    private fun logout() {
        auth.signOut()
        // Clear shared preferences if necessary
        val sharedPreferences = requireActivity().getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(MainActivity.KEY_ONBOARDING_COMPLETED)
        editor.apply()

        // Navigate back to OnboardingScreenActivity
        val intent = Intent(requireActivity(), OnboardingScreenActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
