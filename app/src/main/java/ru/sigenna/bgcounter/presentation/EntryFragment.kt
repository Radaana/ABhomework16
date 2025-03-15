package ru.sigenna.bgcounter.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import ru.sigenna.bgcounter.MainActivity
import ru.sigenna.bgcounter.R
import ru.sigenna.bgcounter.model.BgWeight
import ru.sigenna.bgcounter.utils.Utils.formatDaleFromLong
import javax.inject.Inject

@AndroidEntryPoint
class EntryFragment : Fragment() {

    @Inject
    lateinit var viewModel: EntryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.pullEntryData()
        return inflater.inflate(R.layout.fragment_entry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bgNameAutocomplete = view.findViewById<MaterialAutoCompleteTextView>(R.id.bgName)
        val bgNameSelected = view.findViewById<TextView>(R.id.selectedBgName)
        val qty = view.findViewById<TextInputEditText>(R.id.quantity)
        val checkNew = view.findViewById<CheckBox>(R.id.checkboxNew)
        val checkMine = view.findViewById<CheckBox>(R.id.checkboxMine)
        val weightGroup = view.findViewById<MaterialButtonToggleGroup>(R.id.weightToggleButton)
        val saveButton = view.findViewById<Button>(R.id.saveEntry)
        val deleteButton = view.findViewById<Button>(R.id.deleteEntry)

        Log.d(TAG, "bg ${viewModel.bg.value}")

        viewModel.date.observe(viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.dateInput).text = formatDaleFromLong(it)
        }

        viewModel.isEdit.observe(viewLifecycleOwner) { isEdit ->
            if (isEdit) {
                bgNameAutocomplete.visibility = GONE
                bgNameSelected.visibility = VISIBLE
                bgNameSelected.text = viewModel.bgName.value
                qty.text = Editable.Factory.getInstance().newEditable(viewModel.qty.value)
                checkNew.isChecked = viewModel.isNew.value == true
                checkMine.isChecked = viewModel.isMine.value == true
                weightGroup.check(
                    when (viewModel.weight.value) {
                        BgWeight.MIDDLE -> R.id.buttonMiddle
                        BgWeight.HEAVY -> R.id.buttonHeavy
                        else -> R.id.buttonEasy
                    }
                )
                deleteButton.visibility = VISIBLE
            } else {
                bgNameAutocomplete.visibility = VISIBLE
                bgNameSelected.visibility = GONE
                deleteButton.visibility = GONE
            }
        }

        qty.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.qty.value = s.toString()
            }
        })

        view.findViewById<TextView>(R.id.editDateButton).setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .build()

            datePicker.addOnPositiveButtonClickListener {
                viewModel.date.value = it
            }

            datePicker.show(parentFragmentManager, null)
        }

        view.findViewById<MaterialAutoCompleteTextView>(R.id.bgName)
            .addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    Log.d(TAG, "afterTextChanged ${s.toString()}")
                    viewModel.loadSuggestions(s.toString())
                    viewModel.bgName.value = s.toString()
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

        viewModel.bgSuggestions.observe(viewLifecycleOwner) { suggestions ->
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                suggestions.map { it.title }
            )

            bgNameAutocomplete.setAdapter(adapter)
            bgNameAutocomplete.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    viewModel.bg.value = suggestions[position]
                    bgNameAutocomplete.dismissDropDown()
                }

            if (suggestions.isNotEmpty() && viewModel.bg.value == null) {
                bgNameAutocomplete.showDropDown()
            }
        }

        bgNameAutocomplete.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                bgNameAutocomplete.showDropDown()
            }
        }

        checkNew.setOnCheckedChangeListener { _, isChecked ->
            viewModel.isNew.value = isChecked
        }

        checkMine.setOnCheckedChangeListener { _, isChecked ->
            viewModel.isMine.value = isChecked
        }

        weightGroup.addOnButtonCheckedListener { _, checkedId, _ ->
            when (checkedId) {
                R.id.buttonEasy -> viewModel.weight.value = BgWeight.EASY
                R.id.buttonMiddle -> viewModel.weight.value = BgWeight.MIDDLE
                R.id.buttonHeavy -> viewModel.weight.value = BgWeight.HEAVY
            }
        }

        saveButton.setOnClickListener {
            if (!viewModel.checkDataValidity()) {
                Toast.makeText(requireContext(), getString(R.string.entry_form_invalid), LENGTH_SHORT ).show()
                return@setOnClickListener
            }
            viewModel.saveData()
            (requireActivity() as MainActivity).toList()
        }

        deleteButton.setOnClickListener {
            viewModel.deleteEntry()
            (requireActivity() as MainActivity).toList()
        }
    }

    companion object {
        private val TAG = EntryFragment::class.simpleName
    }
}