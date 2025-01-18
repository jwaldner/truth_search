package com.wfs.truthsearch.ui.compare

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CompareViewModel : ViewModel() {

    private val _texts = MutableLiveData<List<String>>().apply {
        value = listOf(
            "Holiness: A lifestyle dedicated to reflecting God's character.",
            "Shalom: Living in peace with oneself, others, and God.",
            "Joy: Finding delight and gratitude in God's presence.",
            "Temple: Caring for one's body as a sacred vessel.",
            "Sanctification: A process of becoming more Christ-like.",
            "Agape: Loving others selflessly and sacrificially.",
            "Stewardship: Honoring God through wise management of resources.",
            "Prayer: A heartfelt conversation with the Creator.",
            "Faith: Believing and relying on God's promises.",
            "Sabbath: Finding renewal and balance through dedicated rest.",
            "Wisdom: Applying God's Word to daily life.",
            "Service: Living out faith through acts of service.",
            "Hope: Anticipating God's goodness in every situation.",
            "Forgiveness: Extending grace to others as God has given to us.",
            "Courage: Overcoming fear with God's strength.",
            "Truth: Aligning life with the truth of Scripture.",
            "Compassion: Showing kindness and empathy to those in need.",
            "Humility: Putting others before oneself in all situations.",
            "Gratitude: Recognizing and appreciating God's blessings.",
            "Unity: Living in harmony with the body of believers.",
            "Patience: Waiting and persevering with a hopeful heart.",
            "Spiritual Warfare: Battling challenges with God's armor."
        )
    }


    val texts: LiveData<List<String>> = _texts
}