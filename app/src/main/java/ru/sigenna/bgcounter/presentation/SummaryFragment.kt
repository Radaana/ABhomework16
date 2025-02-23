package ru.sigenna.bgcounter.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.sigenna.bgcounter.R
import javax.inject.Inject


@AndroidEntryPoint
class SummaryFragment : Fragment() {

    @Inject
    lateinit var viewModel: SummaryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_summary, container, false)
    }

    private fun generateDiagramLayoutParams(weight: Int): LinearLayout.LayoutParams {
        val params = LinearLayout.LayoutParams (0, LinearLayout.LayoutParams.MATCH_PARENT)
        params.weight = weight.toFloat()
        return  params
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.data.observe(viewLifecycleOwner) { data ->

            view.findViewById<TextView>(R.id.statsTotal).text = data.total.toString()

            view.findViewById<TextView>(R.id.statsEasy).text = data.easy.toString()
            view.findViewById<TextView>(R.id.statsEasyPercent).text =
                getString(R.string.stats_percent, data.easyPercent)
            view.findViewById<View>(R.id.statsWeightDiagramEasy).layoutParams =
                generateDiagramLayoutParams(data.easyPercent)

            view.findViewById<TextView>(R.id.statsMiddle).text = data.middle.toString()
            view.findViewById<TextView>(R.id.statsMiddlePercent).text =
                getString(R.string.stats_percent, data.middlePercent)
            view.findViewById<View>(R.id.statsWeightDiagramMiddle).layoutParams =
                generateDiagramLayoutParams(data.middlePercent)

            view.findViewById<TextView>(R.id.statsHeavy).text = data.heavy.toString()
            view.findViewById<TextView>(R.id.statsHeavyPercent).text =
                getString(R.string.stats_percent, data.heavyPercent)
            view.findViewById<View>(R.id.statsWeightDiagramHeavy).layoutParams =
                generateDiagramLayoutParams(data.heavyPercent)

            view.findViewById<TextView>(R.id.statsTotalNew).text = data.totalNew.toString()
            view.findViewById<TextView>(R.id.statsTotalMine).text = data.totalMine.toString()
            view.findViewById<TextView>(R.id.statsTotalUnpacked).text =
                data.totalUnpacked.toString()
        }
    }
}