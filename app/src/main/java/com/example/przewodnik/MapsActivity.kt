package com.example.przewodnik

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.przewodnikpotoruniu.DBHelper
import com.example.przewodnikpotoruniu.Object
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.properties.Delegates

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, SensorEventListener,
        GoogleMap.OnMarkerClickListener {
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    lateinit var backButton: Button
    lateinit var listButton: Button
    lateinit var findButton: Button
    lateinit var objectName: String
    lateinit var databaseHandler: DBHelper
    lateinit var pickedObjectLabel: TextView
    lateinit var webView: WebView
    private lateinit var sensorManager : SensorManager
    lateinit var positionSensor: Sensor
    private lateinit var randomObject: Object
    private var counter by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        thread.start()
        initialize()
    }
    val thread = Thread(){
        run{

        }
        runOnUiThread(){
            sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            positionSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            sensorManager.registerListener(this, positionSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
    private fun initialize(){
        backButton = findViewById(R.id.backButton_Map)
        backButton.setOnClickListener(backClick)
        listButton = findViewById(R.id.pickObjButton_Map)
        listButton.setOnClickListener(pickClick)
        findButton = findViewById(R.id.findButton_Map)
        findButton.setOnClickListener(findClick)
        databaseHandler = DBHelper(this)
        objectName = intent.getStringExtra("name").toString()
        pickedObjectLabel = findViewById(R.id.pickedObject_Map)
        if (objectName == "null") pickedObjectLabel.text = "Wybrano: "
        else pickedObjectLabel.text = "Wybrano: " + objectName
        counter = 0

        initializeWebView()
        initializeMap()
    }

    private fun initializeMap() {
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun initializeWebView() {
        webView = findViewById(R.id.webView_Map)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    view?.loadUrl(url)
                }
                return true
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)

        setUpMap()
    }


    override fun onMarkerClick(p0: Marker?) = false
    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        map.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            }
        }

    }
    private val backClick = View.OnClickListener {
        val intent = Intent(this, OpeningActivity::class.java)
        startActivity(intent)
        this.finish()
    }
    private val pickClick = View.OnClickListener {
        val intent = Intent(this, ObjectListActivity::class.java)
        startActivity(intent)
        this.finish()
    }
    private val findClick = View.OnClickListener {
        map.clear()
        val pickedObject = databaseHandler.getObject(objectName)
        if(pickedObject == null){
            Toast.makeText(this, "Nie wybrano obiektu!", Toast.LENGTH_SHORT).show()
        }
        else{
            pickedObject.url?.let { it1 -> webView.loadUrl(it1) }
            val location = pickedObject.longitude?.let { it1 -> pickedObject.latitude?.let { it2 ->
                LatLng(it1,
                    it2
                )
            } }
            map.addMarker(MarkerOptions().position(location))
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 18f))
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event!!.sensor.type == Sensor.TYPE_ACCELEROMETER){
            if (counter == 100){
                val randomID = (1..35).random()
                randomObject = databaseHandler.getObject(randomID)!!
                objectName = randomObject.name.toString()
                pickedObjectLabel.text = "Wybrano: " + randomObject.name
                counter = 0
            } else counter++
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

}