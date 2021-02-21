package ru.rikov.evgeniy.pdfsheetmusicreader.feature.main

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.markodevcic.peko.Peko
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import ru.rikov.evgeniy.core.android.base.BaseFragment
import ru.rikov.evgeniy.pdf_renderer.android.AppPdfRenderer
import ru.rikov.evgeniy.pdfsheetmusicreader.databinding.MainFragmentBinding
import ru.rikov.evgeniy.pdfsheetmusicreader.di.mainScreenModule
//import ru.rikov.evgeniy.speech_recognizer.impl.android.di.speechRecognizerModule
import ru.rikov.evgeniy.speech_recognizer.impl.sphinx.di.speechRecognizerModule
//import ru.rikov.evgeniy.speech_recognizer.impl.vosk.di.speechRecognizerModule


class MainFragment private constructor() : BaseFragment() {
    
    private val viewModel by viewModel<MainViewModelImpl>()
    private val pdfRenderer by inject<AppPdfRenderer>()

    private var uiStateJob: Job? = null

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val fileUri: Uri? by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getParcelable(FILE_URI_ARG) as Uri?
    }

    override val diModules = listOf(
        speechRecognizerModule,
        mainScreenModule)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPdfRenderer()
        checkPermission()
    }

    private fun checkPermission() {
        lifecycleScope.launch {
            val result = Peko.requestPermissionsAsync(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            )

            if (result is PermissionResult.Granted) {
                observeViewModel()
            }
        }
    }

    private fun initPdfRenderer() {
        pdfRenderer.init(
            activity = requireActivity(),
            pages = binding.pages,
            waiter = binding.waiter
        )

        lifecycleScope.launch {
            val pageCount = pdfRenderer.showPdf(fileUri)
            viewModel.init(pageCount)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.currentPage.collect(pdfRenderer::scrollToPage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        uiStateJob?.cancel()
    }

    
    
    companion object {

        private const val FILE_URI_ARG = "FILE_URI_ARG"
        
        fun newInstance(fileUri: Uri) = MainFragment().apply {
            arguments = Bundle().apply {
                putParcelable(FILE_URI_ARG, fileUri)
            }
        }
        
    }
    
}