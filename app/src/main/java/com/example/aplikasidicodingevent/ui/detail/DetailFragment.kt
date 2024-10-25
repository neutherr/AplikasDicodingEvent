package com.example.aplikasidicodingevent.ui.detail

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.aplikasidicodingevent.databinding.FragmentDetailBinding
import java.util.Locale

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()

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
        Log.d(TAG, "Received Event ID: $eventId")

        viewModel.getDetailEvent(eventId)
        viewModel.detailEvent.observe(viewLifecycleOwner) { eventDetail ->
            if (eventDetail != null) {
                Log.d(TAG, "Binding event detail: $eventDetail")
                binding.apply {
                    Glide.with(requireContext())
                        .load(eventDetail.mediaCover)
                        .into(ivPictureDesc)

                    tvTitleDesc.text = eventDetail.name
                    ownerNameValue.text = eventDetail.ownerName

                    val sisa = eventDetail.quota?.minus(eventDetail.registrants ?: 0)
                    quotaLeftValue.text = "$sisa"

                    dateValue.text = eventDetail.beginTime?.let { formatDate(it) }

                    tvInfoValue.text = HtmlCompat.fromHtml(
                        eventDetail.description.toString(),
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )

                    btnRegister.setOnClickListener {
                        eventDetail.link?.let { url -> openLink(url) }
                    }
                }
            } else {
                Log.e(TAG, "Event detail is null")
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
        return try {
            val date = inputFormat.parse(inputDate)
            outputFormat.format(date)
        } catch (e: Exception) {
            inputDate
        }
    }

    private fun openLink(link: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Error opening link: $link", e)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "DetailFragment"
    }
}