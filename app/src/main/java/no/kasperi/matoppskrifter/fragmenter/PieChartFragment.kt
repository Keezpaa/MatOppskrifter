package no.kasperi.matoppskrifter.fragmenter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import no.kasperi.matoppskrifter.R
import no.kasperi.matoppskrifter.databinding.FragmentKategorierBinding
import no.kasperi.matoppskrifter.databinding.FragmentPiechartBinding


class PieChartFragment : Fragment(R.layout.fragment_piechart){

    lateinit var binding:FragmentPiechartBinding

    private var chart: AnyChartView? = null

    private val kategorier = listOf("Biff", "Kylling", "Dessert", "Lam", "Diverse", "Pasta", "Svin", "Sjømat", "Siderett", "Starter", "Vegansk", "Vegetarisk", "Frokost", "Geit")
    private val oppskrifter = listOf(42,35,64,14,11,9,19,28,16,4,3,32,7,1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentPiechartBinding.inflate(layoutInflater)
        chart = binding.pieChart
        konfigurerChartView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    private fun konfigurerChartView() {
        val pie : Pie = AnyChart.pie()
        val dataPieChart: MutableList<DataEntry> = mutableListOf()

        for(index in oppskrifter.indices){
            dataPieChart.add(ValueDataEntry(kategorier.elementAt(index), oppskrifter.elementAt(index)))
        }
        pie.data(dataPieChart)
        pie.title("Antall oppskrifter i hver kategori: ")
        chart!!.setChart(pie)
    }


}