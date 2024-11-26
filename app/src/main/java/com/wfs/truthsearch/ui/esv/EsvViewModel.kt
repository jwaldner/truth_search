package com.wfs.truthsearch.ui.esv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EsvViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is ESV Fragment"
    }
    val text: LiveData<String> = _text
}