package com.example.rickandmorty.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.rickandmorty.databinding.FragmentPersonBinding
import com.example.rickandmorty.utils.UiState
import com.example.rickandmorty.viewmodel.CharacterViewModel
import com.squareup.picasso.Picasso

import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PersonFragment : Fragment() {
    private val _viewModel: CharacterViewModel by viewModels()
    private var _binding: FragmentPersonBinding? = null
    private val binding get() = _binding!!
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPersonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                _viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is UiState.Success -> {
                            val person = uiState.data.find { x -> x.id == 1 }
                            binding.progressBar.visibility = View.GONE
                            binding.imageViewPerson.visibility = View.VISIBLE
                            val imgUrl = person?.image
                            Picasso
                                .get()
                                .load(imgUrl)
                                .into(binding.imageViewPerson)
                            binding.textViewError.visibility = View.GONE
                            binding.textViewName.text = person?.name
                            binding.textViewDescription.text = "Status: ${person?.status}, Especie: ${person?.species} e Gender: ${person?.gender}"
                        }
                        is UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.textViewError.visibility = View.VISIBLE
                            binding.textViewError.text = uiState.message
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PersonFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}