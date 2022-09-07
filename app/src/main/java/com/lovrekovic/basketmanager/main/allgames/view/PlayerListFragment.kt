package com.lovrekovic.basketmanager.main.allgames.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lovrekovic.basketmanager.R
import com.lovrekovic.basketmanager.databinding.FragmentPlayerListBinding
import com.lovrekovic.basketmanager.main.allgames.view.adapter.PlayerRecyclerAdapter
import com.lovrekovic.basketmanager.main.allgames.viewmodel.PlayerListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerListFragment : Fragment() {

    private lateinit var binding: FragmentPlayerListBinding
    private val viewModel by viewModel<PlayerListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerListBinding.inflate(inflater, container, false)

        val args by navArgs<PlayerListFragmentArgs>()
        viewModel.getGameByID(args.id)

        viewModel.players.observe(viewLifecycleOwner) {
            Log.d("PlayerListFragment", "onCreateView: $it")
            binding.FragmentPlayerListRV.adapter = PlayerRecyclerAdapter(it)
        }

        binding.FragmentPlayerListArrowBack.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }
}