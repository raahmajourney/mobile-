package com.example.eventdicoding.ui.finish

import android.content.Intent
import android.os.Bundle
import android.provider.Telephony.Mms.Intents
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.eventdicoding.R
import com.example.eventdicoding.databinding.FragmentFinishedBinding
import com.example.eventdicoding.ui.EventAdapter
import com.example.eventdicoding.ui.MainViewModel
import com.example.eventdicoding.ui.detail.DetailActivity
import com.google.android.material.snackbar.Snackbar


class FinishedFragment : Fragment() {
    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    private lateinit var  viewModel: MainViewModel
    private lateinit var adapter: EventAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    //viewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeRecyclerViews()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.finishedEvents.observe(viewLifecycleOwner){ events ->
            adapter.submitList(events)
        }

        viewModel.isLoading.observe(viewLifecycleOwner){ loading ->
            displayLoadingIndicator(loading)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        }
    }


    private fun initializeRecyclerViews(){
        adapter = EventAdapter(requireContext()) { selectedEvent ->
            val detailIntent = Intent(requireContext(), DetailActivity::class.java).apply{
                putExtra("event", selectedEvent)
            }
            startActivity(detailIntent)
        }

        binding.recycleApiFinish.layoutManager = LinearLayoutManager(context)
        binding.recycleApiFinish.adapter = adapter
    }

    private fun displayLoadingIndicator(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}