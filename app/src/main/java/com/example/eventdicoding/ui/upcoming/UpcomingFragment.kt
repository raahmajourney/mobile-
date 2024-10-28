package com.example.eventdicoding.ui.upcoming

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventdicoding.R
import com.example.eventdicoding.databinding.FragmentUpcomingBinding
import com.example.eventdicoding.ui.EventAdapter
import com.example.eventdicoding.ui.MainViewModel
import com.example.eventdicoding.ui.detail.DetailActivity
import com.google.android.material.snackbar.Snackbar

class UpcomingFragment : Fragment() {
    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel : MainViewModel
    private lateinit var adapter: EventAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeRecyclerView()

        viewModel = ViewModelProvider(this) [MainViewModel::class.java]

        viewModel.activeEvents.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            toggleLoadingIndicator(loading)
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        }

    }


    private fun initializeRecyclerView(){
        adapter = EventAdapter(requireContext()) { event ->
            val detailIntent = Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra("event", event)
            }
            startActivity(detailIntent)
        }
        binding.recycleApiUpcoming.layoutManager = LinearLayoutManager(context)
        binding.recycleApiUpcoming.adapter = adapter
    }

    private fun toggleLoadingIndicator(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
      super.onDestroyView()
        _binding = null
    }

}