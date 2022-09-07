package com.lovrekovic.basketmanager.main.profile.view

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.lovrekovic.basketmanager.AuthActivity
import com.lovrekovic.basketmanager.R
import com.lovrekovic.basketmanager.databinding.FragmentProfileBinding
import com.lovrekovic.basketmanager.main.profile.viewmodel.ProfileViewModel
import lv.chi.photopicker.ChiliPhotoPicker
import lv.chi.photopicker.PhotoPickerFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(), PhotoPickerFragment.Callback {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModel<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.getCurrentUser()

        viewModel.error.observe(viewLifecycleOwner) {
            if(!it.isNullOrEmpty()) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        binding.FragmentProfileLogout.setOnClickListener(logoutClickListener)

        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.FragmentProfileUserName.text = user.username + " " + user.nameSurname
            Glide.with(requireContext()).load(user.photoUrl).into(binding.FragmentProfileImage)
        }
        binding.FragmentProfileChangeImage.setOnClickListener {
            openPicker()
        }
        binding.FragmentProfilePlayedMatches.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToUserGamesFragment(false))
        }
        binding.FragmentProfileUpcomingMatches.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToUserGamesFragment(true))
        }

        return binding.root
    }

    private fun openPicker() {
        PhotoPickerFragment.newInstance(
            multiple = true,
            allowCamera = true,
            maxSelection = 1,
            theme = lv.chi.photopicker.R.style.ChiliPhotoPicker_Light
        ).show(childFragmentManager, "picker")
    }

    override fun onImagesPicked(photos: ArrayList<Uri>) {
        if(photos.isNotEmpty()) {
            viewModel.changeImage(photos.first())
            Glide.with(requireContext()).load(photos.first()).error(R.drawable.jordan_image).centerCrop().into(binding.FragmentProfileImage)
        }
    }

    private val logoutClickListener = View.OnClickListener {
        AlertDialog.Builder(requireContext())
            .setTitle("Are you sure you want to logout")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.logout()
                startActivity(Intent(requireContext(), AuthActivity::class.java))
            }
            .setNegativeButton("No") {_, _ -> }
            .create()
            .show()
    }
}