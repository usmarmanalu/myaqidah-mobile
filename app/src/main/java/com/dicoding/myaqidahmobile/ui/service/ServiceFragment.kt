package com.dicoding.myaqidahmobile.ui.service

import android.content.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.fragment.app.*
import com.dicoding.myaqidahmobile.databinding.*
import com.dicoding.myaqidahmobile.ui.service.navigation.*

class ServiceFragment : Fragment() {

    private var _binding: FragmentServiceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuNavigasi()
    }


    private fun menuNavigasi() {
        binding.apply {
            rawatjalan.setOnClickListener {
                val intent = Intent(requireContext(), RawatJalanActivity::class.java)
                startActivity(intent)
            }
            rawatinap.setOnClickListener {
                val intent = Intent(requireContext(), RawatInapActivity::class.java)
                startActivity(intent)
            }
            lab.setOnClickListener {
                Toast.makeText(requireContext(), "Maaf, fitur ini belum tersedia", Toast.LENGTH_SHORT)
                    .show()
            }
            radiologi.setOnClickListener {
                Toast.makeText(requireContext(), "Maaf, fitur ini belum tersedia", Toast.LENGTH_SHORT)
                    .show()
            }
            farmasi.setOnClickListener {
                Toast.makeText(requireContext(), "Maaf, fitur ini belum tersedia", Toast.LENGTH_SHORT)
                    .show()
            }
            igd.setOnClickListener {
                Toast.makeText(requireContext(), "Maaf, fitur ini belum tersedia", Toast.LENGTH_SHORT)
                    .show()
            }
            icu.setOnClickListener {
                Toast.makeText(requireContext(), "Maaf, fitur ini belum tersedia", Toast.LENGTH_SHORT)
                    .show()
            }
            vk.setOnClickListener {
                Toast.makeText(requireContext(), "Maaf, fitur ini belum tersedia", Toast.LENGTH_SHORT)
                    .show()
            }
            ok.setOnClickListener {
                Toast.makeText(requireContext(), "Maaf, fitur ini belum tersedia", Toast.LENGTH_SHORT)
                    .show()
            }
            fisioterapi.setOnClickListener {
                Toast.makeText(requireContext(), "Maaf, fitur ini belum tersedia", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}