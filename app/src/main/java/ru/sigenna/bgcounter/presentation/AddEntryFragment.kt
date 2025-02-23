package ru.sigenna.bgcounter.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
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
class AddEntryFragment : Fragment() {

    @Inject
    lateinit var viewModel: AddEntryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_new_entry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "bg ${viewModel.bg.value}")

        view.findViewById<TextInputEditText>(R.id.quantity)
            .addTextChangedListener(object : TextWatcher {
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

        view.findViewById<TextView>(R.id.dateInput).setOnClickListener {
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

        val bgNameAutocomplete = view.findViewById<MaterialAutoCompleteTextView>(R.id.bgName)

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

        viewModel.date.observe(viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.dateInput).text = formatDaleFromLong(it)
        }

        bgNameAutocomplete.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                bgNameAutocomplete.showDropDown()
            }
        }

        view.findViewById<CheckBox>(R.id.checkboxNew).setOnCheckedChangeListener { _, isChecked ->
            viewModel.isNew.value = isChecked
        }

        view.findViewById<CheckBox>(R.id.checkboxMine).setOnCheckedChangeListener { _, isChecked ->
            viewModel.isMine.value = isChecked
        }

        view.findViewById<MaterialButtonToggleGroup>(R.id.weightToggleButton)
            .addOnButtonCheckedListener { _, checkedId, _ ->
                when (checkedId) {
                    R.id.buttonEasy -> viewModel.weight.value = BgWeight.EASY
                    R.id.buttonMiddle -> viewModel.weight.value = BgWeight.MIDDLE
                    R.id.buttonHeavy -> viewModel.weight.value = BgWeight.HEAVY
                }
            }

        val saveButton = view.findViewById<Button>(R.id.saveEntry)


        saveButton.setOnClickListener {
            viewModel.saveData()
            (requireActivity() as MainActivity).toList()
        }
    }

    companion object {
        private val TAG = AddEntryFragment::class.simpleName
    }
}