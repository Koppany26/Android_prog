package com.example.progr3sshabitplannertracker.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.progr3sshabitplannertracker.R
import com.example.progr3sshabitplannertracker.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Színes Logo beállítása HTML feldolgozással (Progr3SS)
        val title = "Progr<font color='#00E5FF'>3SS</font>"
        binding.tvLogo.text = HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY)

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.register(username, email, password)
            }
        }

        viewModel.authResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { authResponse ->
                val name = authResponse.user?.username ?: authResponse.user?.email ?: "User"
                Toast.makeText(requireContext(), "Registration successful! Welcome $name", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
            }.onFailure { error ->
                Toast.makeText(requireContext(), "Registration failed: ${error.message}", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnLogin.setOnClickListener {
            findNavController().navigateUp()
        }
        
        binding.btnGoogle.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
