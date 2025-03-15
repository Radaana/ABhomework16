package ru.sigenna.bgcounter.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.sigenna.bgcounter.MainActivity
import ru.sigenna.bgcounter.R
import javax.inject.Inject

@AndroidEntryPoint
class EntryListFragment : Fragment(), Listener {

    @Inject
    lateinit var viewModel: EntryListViewModel

    private val adapter: EntryListAdapter by lazy { EntryListAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_entry_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.entriesList)
        recyclerView.adapter = adapter
        adapter.submitList(viewModel.list)
    }

    override fun onItemClicked(id: String) {
        viewModel.selectEntry(id)
        (requireActivity() as MainActivity).toEntry()
    }
}