package com.dicoding.myaqidahmobile.ui.home.sectionMenu

import android.annotation.*
import android.graphics.drawable.*
import android.os.*
import android.view.*
import androidx.appcompat.app.*
import androidx.core.content.*
import androidx.recyclerview.widget.*
import com.dicoding.myaqidahmobile.*
import com.dicoding.myaqidahmobile.core.firebasemodel.*
import com.dicoding.myaqidahmobile.core.helper.*
import com.dicoding.myaqidahmobile.core.ui.*
import com.dicoding.myaqidahmobile.core.utils.*
import com.dicoding.myaqidahmobile.databinding.*
import com.google.android.material.snackbar.*

@Suppress("DEPRECATION")
class BedKTTActivity : AppCompatActivity() {

    private var loadingDialog: AlertDialog? = null

    private lateinit var binding: ActivityBedKttactivityBinding
    private lateinit var bedKttAdapter: BedKttAdapter
    private val listBedKTT = ArrayList<BedKTT>()
    private lateinit var firebaseDataManager: FirebaseDataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBedKttactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupRecyclerView()

        firebaseDataManager = FirebaseDataManager()
        loadBedKTTData()
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Ketersediaan Tempat Tidur"
            setHomeAsUpIndicator(DrawableUtils.getWhiteBackArrowDrawable(this@BedKTTActivity))
            setBackgroundDrawable(
                ColorDrawable(ContextCompat.getColor(this@BedKTTActivity, R.color.purple2))
            )
        }
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple2)
    }

    private fun setupRecyclerView() {
        bedKttAdapter = BedKttAdapter(listBedKTT)
        binding.rvBedKTT.apply {
            layoutManager = LinearLayoutManager(this@BedKTTActivity)
            adapter = bedKttAdapter
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun loadBedKTTData() {
        showLoadingDialog()
        firebaseDataManager.getDataBedKTT(
            onSuccess = { bedList ->
                listBedKTT.clear()
                listBedKTT.addAll(bedList)
                bedKttAdapter.notifyDataSetChanged()
                checkDataLoadComplete()
            },
            onFailure = { exception ->
                showSnackbar("Failed to load data: ${exception.message}")
                checkDataLoadComplete()
            }
        )
    }

    private fun checkDataLoadComplete() {
        dismissLoadingDialog()
        binding.viewEmpty.root.visibility =
            if (listBedKTT.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = AlertDialog.Builder(this)
                .setView(R.layout.loading_dialog)
                .setCancelable(false)
                .create()
        }
        loadingDialog?.show()
    }

    private fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
