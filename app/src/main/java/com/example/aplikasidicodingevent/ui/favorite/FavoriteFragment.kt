package com.example.aplikasidicodingevent.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasidicodingevent.databinding.FragmentFavoriteBinding
import com.example.aplikasidicodingevent.ui.EventAdapter
import com.example.aplikasidicodingevent.ui.ViewModelFactory

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeFavorites()


    }

    private fun setupRecyclerView() {
        adapter = EventAdapter()
        binding.rvEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = this@FavoriteFragment.adapter
        }
    }

    private fun observeFavorites() {
        viewModel.favoriteEvents.observe(viewLifecycleOwner) { events ->
            if (events.isEmpty()) {
                showEmptyState(true)
            } else {
                showEmptyState(false)
                adapter.setEvents(events)
            }
        }
    }

    private fun showEmptyState(show: Boolean) {
        binding.apply {
            rvEvents.visibility = if (show) View.GONE else View.VISIBLE
            emptyState.visibility = if (show) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}