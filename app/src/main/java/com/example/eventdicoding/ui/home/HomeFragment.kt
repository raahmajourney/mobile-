package com.example.eventdicoding.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventdicoding.R
import com.example.eventdicoding.databinding.FragmentHomeBinding
import com.example.eventdicoding.ui.EventAdapter
import com.example.eventdicoding.ui.MainViewModel
import com.example.eventdicoding.ui.detail.DetailActivity
import com.google.android.material.snackbar.Snackbar
import javax.net.ssl.HandshakeCompletedEvent

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get()= _binding!!

    private lateinit var viewModel: MainViewModel
    private lateinit var ongoingEventAdapter: EventAdapter
    private lateinit var completedEventAdapter: EventAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false )
        return binding.root
    }

    // view model
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        initializeRecyclerViews()

        viewModel.activeEvents.observe(viewLifecycleOwner) { events ->
            ongoingEventAdapter.submitList(events)
        }

        viewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            completedEventAdapter.submitList(events)
        }

        viewModel.searchResult.observe(viewLifecycleOwner) { results ->
            ongoingEventAdapter.submitList(results)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            displayLoadingIndicator(loading)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner)  { message ->
            message?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        }

        viewModel.fetchEvents(1)
        viewModel.fetchEvents(0)
    }

    // adapter
    private fun initializeRecyclerViews(){
        ongoingEventAdapter = EventAdapter(requireContext()) { event ->
            val detailIntent = Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra("event_id", event.id)
            }
            startActivity(detailIntent)
        }

        binding.recyclerViewActiveEvents.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewActiveEvents.adapter = ongoingEventAdapter

        completedEventAdapter = EventAdapter(requireContext()) { event ->
            val detailIntent = Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra("event_id", event.id)
            }
            startActivity(detailIntent)
        }

        binding.recyclerViewFinishedEvents.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewFinishedEvents.adapter = completedEventAdapter
    }

    private fun displayLoadingIndicator(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}