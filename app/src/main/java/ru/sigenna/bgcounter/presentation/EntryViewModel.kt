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

class EntryViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {
    val qty = MutableLiveData<String>()
    val date = MutableLiveData(Date().time)
    val bg = MutableLiveData<BgData>()
    val bgName = MutableLiveData<String>()
    val bgSuggestions = MutableLiveData<List<BgData>>()
    val isMine = MutableLiveData<Boolean>()
    val isNew = MutableLiveData<Boolean>()
    val weight = MutableLiveData<BgWeight>()
    val isEdit = MutableLiveData<Boolean>()

    private var loadSuggestJob: Job? = null

    fun pullEntryData() {
        val selectedEntry = repository.getSelectedEntry() ?: return
        qty.value = selectedEntry.qty.toString()
        date.value = selectedEntry.date
        bg.value = BgData(
            id = selectedEntry.id,
            teseraStringId = selectedEntry.teseraStringId,
            title = selectedEntry.title
        )
        bgName.value = selectedEntry.title
        isMine.value = selectedEntry.isMine
        isNew.value = selectedEntry.isNew
        weight.value = selectedEntry.weight as BgWeight
        isEdit.value = true
    }

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
        repository.editEntry(
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

    fun checkDataValidity(): Boolean {
        return qty.value != null &&
                date.value != null &&
                bg.value != null &&
                weight.value != null
    }

    fun deleteEntry() {
        bg.value?.let {
            repository.deleteEntry(it.id)
        }
    }

    companion object {
        private val TAG = EntryViewModel::class.simpleName
    }
}