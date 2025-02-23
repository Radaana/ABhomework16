package ru.sigenna.bgcounter.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.sigenna.bgcounter.data.Repository
import javax.inject.Inject

class SummaryViewModel @Inject constructor(repository: Repository) : ViewModel() {
    var data = MutableLiveData(repository.getStatistics())
}