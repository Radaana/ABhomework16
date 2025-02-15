package ru.sigenna.bgcounter.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.sigenna.bgcounter.R
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



//        view.findViewById<Button>(R.id.nextToTags).setOnClickListener {
//            viewModel.saveToCash()
//
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.fragmentContainer, TagsFragment())
//                .addToBackStack(null)
//                .commit()
//
//        }
    }
}