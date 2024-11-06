package com.example.aplikasidicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.aplikasidicodingevent.R
import com.example.aplikasidicodingevent.data.local.entity.FavoriteEvent
import com.example.aplikasidicodingevent.data.response.ListEventsItem
import com.example.aplikasidicodingevent.databinding.FragmentDetailBinding
import com.example.aplikasidicodingevent.ui.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val eventId = arguments?.getInt("eventId", 0) ?: 0

        setupObserver()
        viewModel.getDetailEvent(eventId)
        observeFavorite(eventId)
    }

    private fun setupObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.detailEvent.observe(viewLifecycleOwner) { event ->
            event?.let { showDetail(it) }
        }
    }

    private fun observeFavorite(eventId: Int) {
        viewModel.getFavoriteStatus(eventId.toString()).observe(viewLifecycleOwner) { favorite ->
            val isFavorite = favorite != null
            setFavoriteButtonState(isFavorite)
            setupFavoriteButton(favorite, isFavorite)
        }
    }

    private fun showDetail(event: ListEventsItem) {
        binding.apply {
            Glide.with(requireContext())
                .load(event.mediaCover)
                .into(ivPictureDesc)

            tvTitleDesc.text = event.name
            ownerNameValue.text = event.ownerName

            val remainingQuota = event.quota?.minus(event.registrants ?: 0)
            quotaLeftValue.text = remainingQuota.toString()

            dateValue.text = formatDate(event.beginTime ?: "")

            tvInfoValue.text = HtmlCompat.fromHtml(
                event.description.toString(),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            btnRegister.setOnClickListener {
                event.link?.let { url -> openLink(url) }
            }
        }
    }

    private fun setupFavoriteButton(favorite: FavoriteEvent?, isFavorite: Boolean) {
        binding.fabFavorite.setOnClickListener {
            if (favorite != null) {
                viewModel.toggleFavorite(favorite, isFavorite)
            } else {
                viewModel.detailEvent.value?.let { event ->
                    val newFavorite = FavoriteEvent(
                        id = event.id.toString(),
                        name = event.name ?: "",
                        mediaCover = event.mediaCover
                    )
                    viewModel.toggleFavorite(newFavorite, isFavorite)
                }
            }
        }
    }

    private fun setFavoriteButtonState(isFavorite: Boolean) {
        binding.fabFavorite.setImageResource(
            if (isFavorite) R.drawable.ic_favorite
            else R.drawable.ic_favorite_border
        )
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
        return try {
            val date = inputFormat.parse(inputDate)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            inputDate
        }
    }

    private fun openLink(link: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Failed to open link", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}