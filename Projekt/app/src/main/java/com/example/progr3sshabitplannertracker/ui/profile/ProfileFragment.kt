package com.example.progr3sshabitplannertracker.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.progr3sshabitplannertracker.R
import com.example.progr3sshabitplannertracker.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    private val getContentLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            viewModel.uploadProfileImage(uri, requireContext())
            Toast.makeText(requireContext(), "Uploading profile image...", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadProfile()

        viewModel.profile.observe(viewLifecycleOwner) { profile ->
            if (profile != null) {
                binding.tvUsername.text = profile.username ?: profile.email
                binding.tvEmail.text = profile.email

                if (!profile.profileImageUrl.isNullOrBlank()) {
                    val url = if (profile.profileImageUrl.startsWith("http")) {
                        profile.profileImageUrl
                    } else {
                        "http://localhost:8080${profile.profileImageUrl}"
                    }
                    Glide.with(this)
                        .load(url)
                        .placeholder(R.mipmap.ic_launcher_round)
                        .error(R.mipmap.ic_launcher_round)
                        .into(binding.ivProfile)
                }
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { err ->
            if (!err.isNullOrBlank()) {
                Toast.makeText(requireContext(), err, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.isLogoutSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            }
        }

        binding.ivProfile.setOnClickListener {
            getContentLauncher.launch("image/*")
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
