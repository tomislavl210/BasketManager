package com.lovrekovic.basketmanager.main.addgame.view

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lovrekovic.basketmanager.R
import com.lovrekovic.basketmanager.databinding.BottomSheetAddGameBinding
import com.lovrekovic.basketmanager.databinding.FragmentAddGameBinding
import com.lovrekovic.basketmanager.main.addgame.viewmodel.AddGameViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.IOException

private const val TAG = "AddGameFragment"

class AddGameFragment : Fragment() {

    private lateinit var binding: FragmentAddGameBinding
    private val addGameViewModel: AddGameViewModel by sharedViewModel()
    private var mMap: GoogleMap? = null
    private var address: Address? = null
    private var numOfPlayers = 0
    private var bindingSheet: BottomSheetAddGameBinding? = null
    private var bottomSheetDialog: BottomSheetDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddGameBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        initMap()


        binding.FragmentAddGameSearch.setOnClickListener {
            if (binding.FragmentAddGameSearchbox.text.isNotEmpty()) {
                showLocation()
            } else {
                Toast.makeText(requireContext(), "Write the location first", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding.FragmentAddGameAddLocation.setOnClickListener {
            if (binding.FragmentAddGameSearchbox.text.isNotEmpty()) {
                showBottomSheet()
            } else {
                Toast.makeText(requireContext(), "Write the location first", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        addGameViewModel.error.observe(viewLifecycleOwner) {
            if(!it.isNullOrEmpty()) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        addGameViewModel.success.observe(viewLifecycleOwner) {
            if(it) {
                Toast.makeText(requireContext(), "Game added successfully!", Toast.LENGTH_SHORT).show()
                bottomSheetDialog?.dismiss()
            }
        }

        return binding.root
    }

    fun onRadioButtonChecked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            bindingSheet?.FragmentAddGameType1?.isChecked = false
            bindingSheet?.FragmentAddGameType2?.isChecked = false
            bindingSheet?.FragmentAddGameType3?.isChecked = false
            bindingSheet?.FragmentAddGameType4?.isChecked = false
            bindingSheet?.FragmentAddGameType5?.isChecked = false

            view.isChecked = true
            // Check which radio button was clicked
            when (view.getId()) {
                R.id.Fragment_AddGame_Type1 ->
                    if (checked) {
                        numOfPlayers = 2
                    }
                R.id.Fragment_AddGame_Type2 ->
                    if (checked) {
                        numOfPlayers = 6
                    }
                R.id.Fragment_AddGame_Type3 ->
                    if (checked) {
                        numOfPlayers = 10
                    }
                R.id.Fragment_AddGame_Type4 ->
                    if (checked) {
                        numOfPlayers = 4
                    }
                R.id.Fragment_AddGame_Type5 ->
                    if (checked) {
                        numOfPlayers = 8
                    }
            }
        }

    }

    private fun showBottomSheet() {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        bindingSheet = BottomSheetAddGameBinding.inflate(layoutInflater)
        bottomSheetDialog?.setContentView(bindingSheet!!.root)
        bindingSheet?.fragment = this
        bindingSheet?.viewModel = addGameViewModel

        bindingSheet?.BottomSheetAddGameDateAndTime?.setOnClickListener {
            if (numOfPlayers > 0) {
                bindingSheet?.BottomSheetAddGameDatePicker?.visibility = View.VISIBLE
                bindingSheet?.BottomSheetAddGameTimePicker?.visibility = View.VISIBLE
            }
        }

        bindingSheet?.BottomSheetAddGameAdd?.setOnClickListener {
            if (address != null) {
                addGameViewModel.addGame(address!!, numOfPlayers)
            }
        }
        bottomSheetDialog?.show()
    }


    private fun initMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.Fragment_AddGame_Map) as SupportMapFragment
        mapFragment.getMapAsync(mapReadyCallback)
    }

    private val mapReadyCallback = OnMapReadyCallback { googleMap: GoogleMap ->
        mMap = googleMap
    }

    private fun showLocation() {
        val geocoder = Geocoder(requireContext())
        val list: MutableList<Address>
        try {
            list = geocoder.getFromLocationName(
                binding.FragmentAddGameSearchbox.text.toString(),
                1
            )
            if (list.isNotEmpty()) {
                address = list.first()
                val latLng = LatLng(address?.latitude ?: 0.0, address?.longitude ?: 0.0)
                moveCamera(
                    latLng,
                    address?.getAddressLine(0)?: ""
                )
            }
        } catch (e: IOException) {
            Log.e(TAG, e.message.toString())
        }
    }

    private fun moveCamera(latLng: LatLng, title: String) {
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10F))
        if (title.isNotEmpty()) {
            val options: MarkerOptions = MarkerOptions().position(latLng)
                .title(title)
            mMap?.addMarker(options)
        }
    }


}