package android.p.weatherApp.manav

import android.p.weatherApp.R
import android.p.weatherApp.data.model.openweathermap.WeatherApp
import android.p.weatherApp.databinding.ActivityMainBinding
import android.p.weatherApp.manav.network.ApiInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

///a2a29794c04a3fc029f32a2d35a1d5b8
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchWeatherData("Brampton")
        SearchCity()
    }

    private fun SearchCity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun fetchWeatherData(cityName: String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)

        val response = retrofit.getWeatherData(cityName, "a2a29794c04a3fc029f32a2d35a1d5b8", "metric")
        response.enqueue(object : Callback<WeatherApp> {
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val temperature = responseBody.main.temp.toString()
                    //Log.d("TAG", "onResponse: $temperature")
                    binding.temperature.text = "$temperature°C"
                    val humidity = responseBody.main.humidity
                    binding.humidity.text = "$humidity %"
                    val windSpeed = responseBody.wind.speed
                    binding.windSpeed.text = "$windSpeed m/s"
                    val sunRise = responseBody.sys.sunrise.toLong()
                    binding.sunrise.text = "${time(sunRise)}"
                    val sunSet = responseBody.sys.sunset.toLong()
                    binding.sunset.text = "${time(sunSet)}"
                    val seaLevel = responseBody.main.pressure
                    binding.sea.text = "$seaLevel hPa"
                    val condition = responseBody.weather.firstOrNull()?.main ?: "unknown"
                    binding.weather.text = condition
                    val maxTemp = responseBody.main.temp_max
                    binding.maxTemp.text = "Max Temp: $maxTemp °C"
                    val minTemp = responseBody.main.temp_min
                    binding.minTemp.text = "Min Temp: $minTemp °C"
                    binding.condition.text = condition
                    binding.day.text = dayName(System.currentTimeMillis())
                    binding.date.text = date()
                    binding.cityName.text = "$cityName"

                    changeImages(condition)
                }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
            }
        })
    }

    private fun changeImages(conditions: String) {
        when (conditions) {
            "Mist", "Foggy", "Clouds", "Partly Clouds", "Overcast" -> {
                binding.root.setBackgroundResource(R.drawable.cloud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }

            "Clear Sky", "Sunny", "Clear" -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }

            "Light Rain", "Drizzle", "Moderate Rain", "Showers", "Heavy Rain" -> {
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }

            "Light Snow", "Moderate Snow", "Heavy Snow", "Blizzard" -> {
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }

            else -> {
                binding.root.setBackgroundResource(R.drawable.cloud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }

        }
        binding.lottieAnimationView.playAnimation()
    }

    private fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }

    private fun time(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:MM", Locale.getDefault())
        return sdf.format((Date(timestamp * 1000)))
    }

    fun dayName(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }
}