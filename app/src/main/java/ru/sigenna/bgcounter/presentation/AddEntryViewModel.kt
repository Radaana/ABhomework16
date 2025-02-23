package ru.sigenna.bgcounter.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.sigenna.bgcounter.api.TeseraApiService
import ru.sigenna.bgcounter.data.Repository
import ru.sigenna.bgcounter.model.BgData
import ru.sigenna.bgcounter.model.BgEntryData
import ru.sigenna.bgcounter.model.BgWeight
import java.util.Date
import javax.inject.Inject

class AddEntryViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {
    val qty = MutableLiveData<String>()
    val date = MutableLiveData(Date().time)
    val bg = MutableLiveData<BgData>()
    val bgName = MutableLiveData<String>()
    val bgSuggestions = MutableLiveData<List<BgData>>()
    val isMine = MutableLiveData<Boolean>()
    val isNew = MutableLiveData<Boolean>()
    val weight = MutableLiveData<BgWeight>()

    private var loadSuggestJob: Job? = null

    fun loadSuggestions(search: String) {
        if (search.isBlank()) {
            bgSuggestions.value = emptyList()
            return
        }
        loadSuggestJob?.cancel()

        loadSuggestJob = viewModelScope.launch {
            delay(500)

            try {
                TeseraApiService.getBgSuggestions(search, fun(result: List<BgData>) {
                    bgSuggestions.value = result
                    Log.d(TAG, bgSuggestions.value.toString())
                })
            } catch (e: Exception) {
                bgSuggestions.value = emptyList()
            }
        }
    }

    fun saveData() {
        repository.addEntry(
            BgEntryData(
                title = bg.value?.title ?: "",
                id = bg.value?.id ?: "",
                teseraStringId = bg.value?.teseraStringId ?: "",
                qty = if (qty.value != null) qty.value!!.toInt() else 0,
                isNew = isNew.value ?: false,
                isMine = isMine.value ?: false,
                date = date.value ?: 0,
                weight = weight.value
            )
        )
    }

    companion object {
        private val TAG = AddEntryViewModel::class.simpleName
    }
}