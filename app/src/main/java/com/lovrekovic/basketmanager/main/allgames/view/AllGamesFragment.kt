package com.lovrekovic.basketmanager.main.allgames.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lovrekovic.basketmanager.databinding.FragmentAllGamesBinding
import com.lovrekovic.basketmanager.main.allgames.view.adapter.AllGamesRecyclerAdapter
import com.lovrekovic.basketmanager.main.allgames.viewmodel.AllGamesViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class AllGamesFragment : Fragment() {

    private lateinit var binding: FragmentAllGamesBinding
    private val viewModel: AllGamesViewModel by sharedViewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllGamesBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        viewModel.getAllGames()
        viewModel.allGames.observe(viewLifecycleOwner) {
            binding.FragmentAllGamesRecyclerView.adapter =
                AllGamesRecyclerAdapter(games = it, onItemClick = { id ->
                    findNavController().navigate(
                        AllGamesFragmentDirections.actionAllGamesFragmentToPlayerListFragment(
                            id
                        )
                    )
                })
        }

        viewModel.error.observe(viewLifecycleOwner){
            if(!it.isNullOrEmpty()){
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}