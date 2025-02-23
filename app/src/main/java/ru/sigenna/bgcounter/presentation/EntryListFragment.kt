package ru.sigenna.bgcounter.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.sigenna.bgcounter.R
import ru.sigenna.bgcounter.data.MockEntriesData
import ru.sigenna.bgcounter.model.BgEntryData
import javax.inject.Inject

@AndroidEntryPoint
class EntryListFragment : Fragment() {

    @Inject
    lateinit var viewModel: EntryListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_entry_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.entriesList)
        recyclerView.adapter = viewModel.adapter
        viewModel.setAdapterList()
    }
}