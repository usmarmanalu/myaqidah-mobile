package com.dicoding.myaqidahmobile.ui.home

import com.dicoding.myaqidahmobile.ui.home.sectionMenu.MonitorKesehatanActivity
import android.animation.*
import android.annotation.*
import android.content.*
import android.graphics.*
import android.os.*
import android.view.*
import androidx.appcompat.app.*
import androidx.fragment.app.*
import androidx.recyclerview.widget.*
import com.dicoding.myaqidahmobile.R
import com.dicoding.myaqidahmobile.core.helper.*
import com.dicoding.myaqidahmobile.core.firebasemodel.*
import com.dicoding.myaqidahmobile.core.ui.*
import com.dicoding.myaqidahmobile.databinding.*
import com.dicoding.myaqidahmobile.ui.home.menu.*
import com.dicoding.myaqidahmobile.ui.home.sectionMenu.*
import com.dicoding.myaqidahmobile.ui.registrasi.*
import com.google.android.material.snackbar.*
import com.google.firebase.auth.*

class HomeFragment : Fragment() {

    private var loadingDialog: AlertDialog? = null

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseDataManager: FirebaseDataManager

    private lateinit var beritaAdapter: BeritaAdapter
    private lateinit var galleryAdapter: GaleryAdapter
    private lateinit var artikelAdapter: ArtikelAdapter

    private val listArtikel = ArrayList<Artikel>()
    private val handler = Handler(Looper.getMainLooper())

    private var currentBeritaPage = 0
    private var currentGalleryPage = 0

    private val autoSlideRunnable = object : Runnable {
        override fun run() {
            if (::beritaAdapter.isInitialized) {
                currentBeritaPage =
                    if (currentBeritaPage == beritaAdapter.itemCount - 1) 0 else currentBeritaPage + 1
                binding.carouselBeritaViewPager.setCurrentItem(currentBeritaPage, true)
            }
            if (::galleryAdapter.isInitialized) {
                currentGalleryPage =
                    if (currentGalleryPage == galleryAdapter.itemCount - 1) 0 else currentGalleryPage + 1
                binding.carouselGaleryViewPager.setCurrentItem(currentGalleryPage, true)
            }
            handler.postDelayed(this, 8000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseDataManager = FirebaseDataManager()

        setupViews()

        // Show loading dialog and start data loading
        showLoadingDialog()
        loadDataForCarouselBerita()
        loadDataForCarouselGallery()
        loadArticles()

        binding.fabRegistrasi.setOnClickListener {
            val intent = Intent(requireContext(), RegistrasiOnlineActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoadingDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setView(R.layout.loading_dialog)
        dialogBuilder.setCancelable(false)
        loadingDialog = dialogBuilder.create()
        loadingDialog?.show()
    }

    private fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
    }

    private fun setupCarouselBerita(items: List<Pair<String, String>>) {
        beritaAdapter = BeritaAdapter(items)
        binding.carouselBeritaViewPager.adapter = beritaAdapter
        binding.indicatorBerita.attachTo(binding.carouselBeritaViewPager)
        startAutoSlide()
        checkDataLoadComplete()
    }

    private fun setupCarouselGallery(items: List<Pair<String, String>>) {
        galleryAdapter = GaleryAdapter(items)
        binding.carouselGaleryViewPager.adapter = galleryAdapter
        binding.indicatorGalery.attachTo(binding.carouselGaleryViewPager)
        startAutoSlide()
        checkDataLoadComplete()
    }

    private fun setupViews() {
        binding.tvOpeningHours.apply {
            isSelected = true
            paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
            animateOpeningHours()
        }
        setupMenuCardClickListeners()
        setupRecyclerView()
        fetchUserName()
    }

    private fun animateOpeningHours() {
        val screenWidth = resources.displayMetrics.widthPixels
        val textWidth = binding.tvOpeningHours.width
        ObjectAnimator.ofFloat(
            binding.tvOpeningHours,
            View.TRANSLATION_X,
            -textWidth.toFloat(),
            screenWidth.toFloat()
        ).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun setupRecyclerView() {
        artikelAdapter = ArtikelAdapter(listArtikel)
        binding.rvArtikel.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = artikelAdapter
        }
    }

    private fun fetchUserName() {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            firebaseDataManager.getCurrentUserName(user.uid) { name ->
                binding.tvUser.text = getString(R.string.welcome, name)
            }
        } ?: showSnackbar("User not logged in.")
    }

    private fun loadDataForCarouselBerita() {
        firebaseDataManager.getDataBerita(
            onSuccess = { items ->
                setupCarouselBerita(items)
            },
            onFailure = { exception ->
                showSnackbar("Failed to load berita: ${exception.message}")
                checkDataLoadComplete()
            }
        )
    }

    private fun loadDataForCarouselGallery() {
        firebaseDataManager.getDataGallery(
            onSuccess = { items ->
                setupCarouselGallery(items)
            },
            onFailure = { exception ->
                showSnackbar("Failed to load gallery: ${exception.message}")
                checkDataLoadComplete()
            }
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadArticles() {
        firebaseDataManager.getArticles(
            onSuccess = { articles ->
                listArtikel.clear()
                listArtikel.addAll(articles)
                artikelAdapter.notifyDataSetChanged()
                checkDataLoadComplete()
            },
            onFailure = { exception ->
                showSnackbar("Failed to load articles: ${exception.message}")
                checkDataLoadComplete()
            }
        )
    }

    private fun setupMenuCardClickListeners() {
        with(binding.menuGrid) {
            indicatorCard.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        IndicatorMutuActivity::class.java
                    )
                )
            }
            aboutCard.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        InformasiRumahSakitActivity::class.java
                    )
                )
            }
            hakkewajibanCard.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        HakKewajibanActivity::class.java
                    )
                )
            }
            partnerCard.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        RekananActivity::class.java
                    )
                )
            }
            karirCard.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        KarirActivity::class.java
                    )
                )
            }
            allmenuCard.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        AllLayananActivity::class.java
                    )
                )
            }
            bedCard.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        BedKTTActivity::class.java
                    )
                )
            }
            infohealthCard.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        InfoKesehatanActivity::class.java
                    )
                )
            }
            janjitemuCard.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        JanjiTemuActivity::class.java
                    )
                )
            }
            healthCard.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        MonitorKesehatanActivity::class.java
                    )
                )
            }
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun startAutoSlide() {
        handler.postDelayed(autoSlideRunnable, 3000)
    }

    private fun checkDataLoadComplete() {
        if (::beritaAdapter.isInitialized && ::galleryAdapter.isInitialized && listArtikel.isNotEmpty()) {
            dismissLoadingDialog()
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(autoSlideRunnable)
    }

    override fun onResume() {
        super.onResume()
        startAutoSlide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        handler.removeCallbacks(autoSlideRunnable)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuAmbulance -> {
                startActivity(Intent(requireContext(), EmergencyActivity::class.java))
                true
            }

            R.id.menuNotification -> {
                startActivity(Intent(requireContext(), NotificationActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}