package com.lovrekovic.basketmanager.auth.view

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.lovrekovic.basketmanager.R
import com.lovrekovic.basketmanager.auth.viewmodel.RegisterViewModel
import com.lovrekovic.basketmanager.databinding.FragmentRegisterBinding
import lv.chi.photopicker.PhotoPickerFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RegisterFragment : Fragment(), PhotoPickerFragment.Callback {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.error.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.success.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_registerFragment_to_mainActivity)
        }

        return binding.root
    }

    fun openPicker() {
        PhotoPickerFragment.newInstance(
            multiple = true,
            allowCamera = true,
            maxSelection = 1,
            theme = lv.chi.photopicker.R.style.ChiliPhotoPicker_Light
        ).show(childFragmentManager, "picker")
    }

    override fun onImagesPicked(photos: ArrayList<Uri>) {
        if(photos.isNotEmpty()) {
            viewModel.pickedImage = photos.first()
            Glide.with(requireContext()).load(photos.first()).error(R.drawable.jordan_image).centerCrop().into(binding.FragmentRegisterPhoto)
        }
    }
}