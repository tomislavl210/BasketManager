package com.lovrekovic.basketmanager.main.maps.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lovrekovic.basketmanager.R
import com.lovrekovic.basketmanager.databinding.BottomSheetJoinGameBinding
import com.lovrekovic.basketmanager.databinding.FragmentMapsBinding
import com.lovrekovic.basketmanager.main.allgames.model.models.Game
import com.lovrekovic.basketmanager.main.maps.viewmodel.MapsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsFragment : Fragment() {

    private lateinit var binding: FragmentMapsBinding
    private val viewModel by viewModel<MapsViewModel>()
    private var mMap: GoogleMap? = null
    private var bottomSheetDialog: BottomSheetDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        initMap()
        viewModel.getAllGames()
        viewModel.allGames.observe(viewLifecycleOwner) { games ->
            setMarkers(games)
        }

        viewModel.error.observe(viewLifecycleOwner){
            if(!it.isNullOrEmpty()){
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isGameAdded.observe(viewLifecycleOwner) {
            if(it) {
                bottomSheetDialog?.dismiss()
            }
        }

        return binding.root
    }

    private fun setMarkers(games: List<Game>) {
        games.forEach { game ->
            val latLng = LatLng(game.latitude ?: 0.0, game.longitude ?: 0.0)
            val options: MarkerOptions = MarkerOptions().position(latLng).title("")
            val marker = mMap?.addMarker(options)
            marker?.tag = game.id
            mMap?.setOnMarkerClickListener {
                showBottomSheet(game)
                true
            }
        }
    }

    private fun initMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.Fragment_Maps_Map) as SupportMapFragment
        mapFragment.getMapAsync(mapReadyCallback)
    }

    private val mapReadyCallback = OnMapReadyCallback { googleMap: GoogleMap ->
        mMap = googleMap
    }

    private fun showBottomSheet(game: Game) {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        val bindingSheet = BottomSheetJoinGameBinding.inflate(layoutInflater)
        bottomSheetDialog?.setContentView(bindingSheet.root)

        with(bindingSheet) {
            BottomSheetJoinGameDateTime.text = game.date + " " + game.time
            BottomSheetJoinGameAddress.text = game.cityName
            BottomSheetJoinGameGameType.text = getGameType(game.numOfPlayers)
            BottomSheetJoinGamePlayerList.setOnClickListener {
                bottomSheetDialog?.dismiss()
                findNavController().navigate(MapsFragmentDirections.actionMapsFragmentToPlayerListFragment2(game.id))
            }
            BottomSheetJoinGameJoinButton.setOnClickListener {
                viewModel.joinGame(game)
            }
        }

        bottomSheetDialog?.show()
    }

    private fun getGameType(numOfPlayers: Int?): String {
        return when (numOfPlayers) {
            2 -> "1v1"
            4 -> "2v2"
            6 -> "3v3"
            8 -> "4v4"
            10 -> "5v5"
            else -> ""
        }
    }


}