package com.example.yandexapi

import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.yandexapi.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.CameraPosition
import java.util.Locale

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //получение данных широты и долготы
        val lan = intent.getDoubleExtra("Latitude", 52.28)
        val lon = intent.getDoubleExtra("Longitude", 104.3)
        //определение точки на карте
        val LatLng = LatLng(lan,lon)
        // Добавление маркера и камеры для удобства карт, иначе сразу будет вся карта, а не мини кусок
        mMap.addMarker(MarkerOptions().position(LatLng).title("Im there!"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng))
        //приближение карты(здесь наша позиция, масштаб и 2 наклона)
        val CameraPosition = CameraPosition(LatLng, 15f, 23f, 10f)
        //применяем CP
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition))
        //обработчки нажатий на карте
        mMap.setOnMapClickListener {
            val geocoder = Geocoder (applicationContext, Locale.getDefault())
            //получение адресса
            var address : String = ""
            //получение всех ближайших адрессов, it-точка касания
            val addresses : MutableList<Address>? = geocoder.getFromLocation(it.latitude, it.longitude, 1)//получение точки на карте
            //проверка на то, получили ли мы какието адресса или нет
            if(addresses != null){
                for (adr in addresses){
                    address += adr.getAddressLine(addresses.indexOf(adr))
                }

            }
            //ставим маркер в той же точке, где и нажал пользователь с подписью адресса
            mMap.addMarker((MarkerOptions().position(it).title((address))))
        }

    }
}