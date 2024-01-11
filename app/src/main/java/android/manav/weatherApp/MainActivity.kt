package android.p.weatherApp

import android.p.weatherApp.databinding.ActivityMainBinding
import android.p.weatherApp.details.DetailsViewModel
import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val detailsViewModel: DetailsViewModel by viewModel()

    private lateinit var binding: ActivityMainBinding

    private val locationPermissionRequest = registerForActivityResult(RequestMultiplePermissions()) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)
                || permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                lifecycleScope.launch { getWeatherByLocation() }
            }

            else -> {

                lifecycleScope.launch { detailsViewModel.fetchWeatherData("Brampton") }
            }
        }
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewmodel = detailsViewModel

        setContentView(binding.root)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    detailsViewModel.fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?) = true
        })

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        lifecycleScope.launch {
            getWeatherByLocation()
        }

        detailsViewModel.weatherImage.observe(this) {
            binding.content.setBackgroundResource(it.background)
            binding.lottieAnimationView.setAnimation(it.animation)
            binding.lottieAnimationView.playAnimation()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        detailsViewModel.setLocale(newConfig.locales[0])
    }

    private fun onMyLocation() {
        lifecycleScope.launch {
            getWeatherByLocation()
        }
    }

    private suspend fun getWeatherByLocation() {
        when (PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION),
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) -> {
                withContext(Dispatchers.IO) {
                    val result = fusedLocationClient.getCurrentLocation(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        CancellationTokenSource().token
                    ).await()
                    detailsViewModel.fetchWeatherData(result)
                }
            }

            else -> locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}