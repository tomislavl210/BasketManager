package com.lovrekovic.basketmanager.main.profile.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lovrekovic.basketmanager.R
import com.lovrekovic.basketmanager.databinding.FragmentUserGamesBinding
import com.lovrekovic.basketmanager.main.allgames.view.adapter.AllGamesRecyclerAdapter
import com.lovrekovic.basketmanager.main.profile.viewmodel.UserGamesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.LocalDateTime

private const val TAG = "UserGamesFragment"

class UserGamesFragment : Fragment() {

    private lateinit var binding: FragmentUserGamesBinding
    private val viewModel: UserGamesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserGamesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.getCurrentUser()
        val args by navArgs<UserGamesFragmentArgs>()
        if (args.isItUpcoming) {
            binding.FragmentPlayerGamesUpcomingFinishedText.text =
                getString(R.string.upcoming_games)
        } else {
            binding.FragmentPlayerGamesUpcomingFinishedText.text = getString(R.string.game_history)
        }

        viewModel.games.observe(viewLifecycleOwner) { games ->
            Log.d(TAG, "onCreateView: $games")
            if (!games.isNullOrEmpty()) {
                val filteredGames = games.filter { game ->
                    if(game.date != null) {
                        Log.d(TAG, "onCreateView: ${viewModel.parseDateFromString(game.date)}")
                        if (args.isItUpcoming) {
                            viewModel.parseDateFromString(game.date).isBefore(LocalDate.now())
                        } else {
                            viewModel.parseDateFromString(game.date).isAfter(LocalDate.now())
                        }
                    } else false
                }
                Log.d(TAG, "onCreateView: filtered $filteredGames")
                binding.FragmentPlayerGamesRV.adapter = AllGamesRecyclerAdapter(filteredGames, onItemClick = {})
            }
        }

        binding.FragmentPlayerGamesArrowBack.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }
}