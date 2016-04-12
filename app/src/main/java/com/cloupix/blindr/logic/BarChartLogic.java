package com.cloupix.blindr.logic;

import android.graphics.Color;

import com.cloupix.blindr.business.WifiAP;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by alonsoapp on 12/04/16.
 *
 */
public class BarChartLogic extends ChartLogic {

    private BarData data;

    public void setupChart(BarChart barChart, WifiAP detailedWifiAP) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();
        TreeMap<Integer, Integer> map = detailedWifiAP.getLevelHistogram();

        int chartPosition = 0;
        for(Map.Entry<Integer, Integer> entry : map.entrySet()) {
            entries.add(new BarEntry((float)entry.getValue(), chartPosition));
            labels.add(Integer.toString(entry.getKey()));
            chartPosition++;
        }

        BarDataSet dataset = new BarDataSet(entries, "Same level lectures");


        barChart.setPinchZoom(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawHighlightArrow(false);
        barChart.setHighlightPerTapEnabled(false);
        barChart.setHighlightPerDragEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);
        dataset.setColor(Color.rgb(244, 117, 117));



        data = new BarData(labels, dataset);
        barChart.setData(data);
        // dont forget to refresh the drawing
        barChart.invalidate();
    }
}
