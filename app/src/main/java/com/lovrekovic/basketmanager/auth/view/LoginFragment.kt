package com.lovrekovic.basketmanager.auth.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.lovrekovic.basketmanager.R
import com.lovrekovic.basketmanager.auth.viewmodel.LoginViewModel
import com.lovrekovic.basketmanager.databinding.FragmentLoginBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by sharedViewModel<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.error.observe(viewLifecycleOwner){
            if(!it.isNullOrEmpty()){
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.success.observe(viewLifecycleOwner){
            if(it){
                findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
            }
        }

        binding.FragmentLoginRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return binding.root
    }
}