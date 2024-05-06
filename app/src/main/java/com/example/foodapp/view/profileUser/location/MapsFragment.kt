package com.example.foodapp.view.profileUser.location

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.R
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentMapsBinding
import com.example.foodapp.viewmodel.AccountViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException


class MapsFragment : BaseFragment<FragmentMapsBinding>(), OnMapReadyCallback, LocationListener,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private var mGoogleMap: GoogleMap? = null
    private var mCurrLocationMarker: Marker? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private lateinit var mLocationRequest: LocationRequest

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var address: List<Address>
    private lateinit var currentLocation: Location
    private val permissionCode = 101

    private lateinit var accountViewModel: AccountViewModel
    override fun getLayout(container: ViewGroup?): FragmentMapsBinding =
        FragmentMapsBinding.inflate(layoutInflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun initViews() {
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]

        address = mutableListOf()
        getCurrentLocationUser()

        binding.btnSearchAddress.setOnClickListener {
            searchLocation(it)
        }
        binding.btnSelectAddress.setOnClickListener {
            //getLastLocation()
            callback.showFragment(MapsFragment::class.java, AddToSavedLocationFragment::class.java, 0, 0, address, true)
        }
    }

    private fun getCurrentLocationUser() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), permissionCode)
            return
        }
        val getLocation = fusedLocationProviderClient.lastLocation
        getLocation.addOnSuccessListener { location ->
            if (location != null) {
                val geocoder = Geocoder(requireContext())
                address = geocoder.getFromLocation(location.latitude, location.longitude, 1)!!
                currentLocation = location
                val mapFragment = childFragmentManager
                    .findFragmentById(R.id.fragment_maps) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }
    }

//    @SuppressLint("SetTextI18n")
//    private fun getLastLocation(){
//        if(ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//            fusedLocationProviderClient.lastLocation
//                .addOnSuccessListener {
////                    if(location != null && address.isNotEmpty()){
////                        addressUser.address = "Address: " + address[0].getAddressLine(0)
////                    }
//                }
//        }
//    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionCode -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocationUser()
                //getLastLocation()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("Vị trí của bạn")

        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
        googleMap.addMarker(markerOptions)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                buildGoogleApiClient()
                mGoogleMap!!.isMyLocationEnabled = true
            }
        }else{
            buildGoogleApiClient()
            mGoogleMap!!.isMyLocationEnabled = true
        }
    }

    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(requireContext())
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
        mGoogleApiClient!!.connect()
    }

    override fun onLocationChanged(p0: Location) {
        currentLocation = p0
        if(mCurrLocationMarker != null){
            mCurrLocationMarker!!.remove()
        }

        val latLng = LatLng(p0.latitude, p0.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("Vị trí")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        mCurrLocationMarker = mGoogleMap!!.addMarker(markerOptions)

        mGoogleMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mGoogleMap!!.moveCamera(CameraUpdateFactory.zoomTo(12f))

        if(mGoogleApiClient != null){
            LocationServices.getFusedLocationProviderClient(requireActivity())
        }
    }

    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 1000
        mLocationRequest.fastestInterval = 1000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if(ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            LocationServices.getFusedLocationProviderClient(requireActivity())
        }
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    private fun searchLocation(view: View){
        val loca: String = binding.edtSearchAddress.text.toString().trim()

        if(loca == ""){
            notify("Vui lòng nhập địa chỉ")
        }else{
            val geocoder = Geocoder(requireContext())
            try {
                address = geocoder.getFromLocationName(loca, 1)!!
            }catch (e: IOException){
                e.printStackTrace()
            }
            Log.d("address", address.toString())

            val add = address[0]
            val latLng = LatLng(add.latitude, add.longitude)
            mGoogleMap!!.addMarker(MarkerOptions().position(latLng).title(loca))
            mGoogleMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        }
    }


}