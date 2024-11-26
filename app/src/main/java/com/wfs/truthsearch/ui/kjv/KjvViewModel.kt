package com.wfs.truthsearch.ui.kjv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class KjvViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is KJV Fragment"
    }
    val text: LiveData<String> = _text
}