package com.dicoding.myaqidahmobile.ui.jadwal

import android.content.*
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.myaqidahmobile.R
import com.dicoding.myaqidahmobile.core.data.*
import com.dicoding.myaqidahmobile.core.domain.model.JadwalDoctors
import com.dicoding.myaqidahmobile.core.ui.DoctorAdapter
import com.dicoding.myaqidahmobile.databinding.FragmentScheduleBinding
import com.dicoding.myaqidahmobile.ui.jadwal.favorite.FavoriteActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!
    private val doctorViewModel: DoctorViewModel by viewModel()
    private lateinit var doctorAdapter: DoctorAdapter
    private var originalDoctorsList: List<JadwalDoctors> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the doctorAdapter
        doctorAdapter = DoctorAdapter(requireContext())

        setupRecyclerView()
        observeViewModel()
        setupSearchView()

        // Retrieve the passed doctor data if available
        @Suppress("DEPRECATION")
        arguments?.getParcelable<JadwalDoctors>(EXTRA_DATA)?.let { selectedDoctor ->
            showDoctor(selectedDoctor)
        }
    }

    private fun showDoctor(jadwalDoctors: JadwalDoctors) {
        doctorViewModel.setFavoriteDoctor(jadwalDoctors, jadwalDoctors.isFavorite)
    }

    private fun setupRecyclerView() {
        with(binding.rvDoctor) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = doctorAdapter
        }
    }

    private fun observeViewModel() {
        doctorViewModel.getDoctors.observe(viewLifecycleOwner) { resource ->
            when (resource) {

                is Resource.Loading -> showLoading(true)

                is Resource.Success -> {
                    showLoading(false)
                    resource.data?.let { doctors ->
                        originalDoctorsList = doctors
                        doctorAdapter.submitList(doctors)
                    }
                }

                is Resource.Error -> {
                    showLoading(false)
                    binding.viewError.root.visibility = View.VISIBLE
                    binding.viewError.tvError.text =
                        resource.message ?: getString(R.string.something_wrong)
                }
            }
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterDoctors(newText)
                return true
            }
        })
    }

    private fun filterDoctors(query: String?) {
        val filteredList = if (query.isNullOrEmpty()) {
            originalDoctorsList
        } else {
            originalDoctorsList.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }
        doctorAdapter.submitList(filteredList)
        showLoading(false)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_favorite, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuFavorite -> {
                startActivity(Intent(requireContext(), FavoriteActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}
