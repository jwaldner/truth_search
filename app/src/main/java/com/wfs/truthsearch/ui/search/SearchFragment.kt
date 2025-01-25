package com.wfs.truthsearch.ui.search

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.wfs.truthsearch.SharedViewModel
import com.wfs.truthsearch.databinding.DialogBibleSearchBinding
import com.wfs.truthsearch.utils.PreferenceManager
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.random.Random

class SearchFragment : DialogFragment() {

    private var _binding: DialogBibleSearchBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var adapter: SearchResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Requirement 2: Prevent dismissal via system back button
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogBibleSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeader()
        setupListeners()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupHeader() {
        val version = if (PreferenceManager.getBool(PreferenceManager.KEY_PREFS_SEARCH_ESV)) "[ ESV ]" else "[ KJV ]"
        binding.textViewHeader.text = "Search $version"
    }

    private fun setupListeners() {
        // Requirement 1: ONLY close when explicit close (X) button is tapped
        binding.buttonClose.setOnClickListener {
            dismiss()
        }

        binding.buttonSearch.setOnClickListener {
            performSearch()
        }

        binding.editTextSearchQuery.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else {
                false
            }
        }

        binding.editTextSearchQuery.doAfterTextChanged { text ->
            val query = text?.toString().orEmpty()
            searchViewModel.updateQuery(query)
            if (query.isBlank()) {
                searchViewModel.clearResults()
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = SearchResultAdapter { verseId ->
            launchBrowser(verseId)
        }
        binding.recyclerViewResults.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewResults.adapter = adapter
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    searchViewModel.searchQuery.collect { query ->
                        if (binding.editTextSearchQuery.text?.toString() != query) {
                            binding.editTextSearchQuery.setText(query)
                            binding.editTextSearchQuery.setSelection(query.length)
                        }
                    }
                }
                launch {
                    searchViewModel.searchResults.collect { results ->
                        adapter.submitList(results)
                        binding.textViewEmptyState.visibility =
                            if (results.isEmpty() && searchViewModel.searchQuery.value.isNotBlank()) {
                                View.VISIBLE
                            } else {
                                View.GONE
                            }
                    }
                }
            }
        }
    }

    private fun performSearch() {
        val query = binding.editTextSearchQuery.text?.toString().orEmpty().trim()
        if (query.isNotEmpty()) {
            hideKeyboard()
            searchViewModel.performSearch(requireContext(), query)
        }
    }

    fun launchBrowser(verse: String) {
        sharedViewModel.setVerseId(verse)
        val version = if (PreferenceManager.getBool(PreferenceManager.KEY_PREFS_SEARCH_ESV)) "esv" else "kjv"
        sharedViewModel.setBibleVersion(version)
        sharedViewModel.setId(Random.nextInt())
        lifecycleScope.launch {
            sharedViewModel.emitLaunchBrowserEvent()
        }
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(binding.editTextSearchQuery.windowToken, 0)
    }

    override fun onStart() {
        super.onStart()
        dialog?.let { dlg ->
            // Requirement 2: Prevent dismissal on outside touch
            dlg.setCanceledOnTouchOutside(false)

            // Requirement 4: Tablet window sizing (max width 600dp, height 85% of screen)
            dlg.window?.let { window ->
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                val displayMetrics = resources.displayMetrics
                val screenWidthPx = displayMetrics.widthPixels
                val screenHeightPx = displayMetrics.heightPixels

                val maxTabletWidthPx = (600 * displayMetrics.density).toInt()
                val targetWidthPx = min((screenWidthPx * 0.90f).toInt(), maxTabletWidthPx)
                val targetHeightPx = (screenHeightPx * 0.85f).toInt()

                window.setLayout(targetWidthPx, targetHeightPx)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
