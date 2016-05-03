package com.cloupix.blindr.logic;

import android.graphics.Color;

import com.cloupix.blindr.business.WifiAP;
import com.cloupix.blindr.business.Lecture;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

/**
 * Created by alonsoapp on 12/04/16.
 *
 */
public class LineChartLogic extends ChartLogic {



    public void setupChart(LineChart mChart, WifiAP wifiAp) {
        //mChart.setViewPortOffsets(0, 20, 0, 0);
        //mChart.setBackgroundColor(Color.rgb(104, 241, 175));

        // no description text
        mChart.setDescription("");

        // enable touch gestures
        mChart.setTouchEnabled(false);

        XAxis x = mChart.getXAxis();
        x.setEnabled(false);

        YAxis y = mChart.getAxisLeft();
        //y.setTypeface(tf);
        y.setLabelCount(6, false);
        y.setStartAtZero(false);
        //y.setTextColor(Color.BLACK);
        //y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawLabels(false);
        y.setDrawGridLines(false);
        y.setAxisLineColor(Color.TRANSPARENT);

        //add Data
        setData(mChart, wifiAp);

        mChart.setDescription("");    // Hide the description
        mChart.getAxisLeft().setDrawLabels(false);
        mChart.getAxisRight().setDrawLabels(false);
        mChart.getXAxis().setDrawLabels(false);

        mChart.getLegend().setEnabled(false);   // Hide the legend

        mChart.getAxisRight().setEnabled(false);

        // dont forget to refresh the drawing
        mChart.invalidate();
    }

    private void setData(LineChart mChart, WifiAP wifiAp){

        // Sacamos los valores de las x
        ArrayList<String> xVals = new ArrayList<String>();
        for (Lecture lecture : wifiAp.getLectures()) {

            xVals.add(lecture.getTimestamp() + "");
        }

        //Sacamos los valores de Y
        ArrayList<Entry> vals1 = new ArrayList<Entry>();

        for (int i = 0; i < wifiAp.getLectures().size(); i++) {
            vals1.add(new Entry(wifiAp.getLectures().get(i).getLevel(), i));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(vals1, "Level");
        set1.setDrawCubic(true);
        set1.setCubicIntensity(0.2f);
        //set1.setDrawFilled(true);
        set1.setDrawCircles(false);
        set1.setLineWidth(1.8f);
        set1.setCircleSize(4f);
        set1.setCircleColor(Color.WHITE);
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setColor(Color.rgb(244, 117, 117));
        set1.setFillColor(Color.WHITE);
        set1.setFillAlpha(100);
        set1.setDrawHorizontalHighlightIndicator(false);

        // create a data object with the datasets
        LineData data = new LineData(xVals, set1);
        //data.setValueTypeface(tf);
        data.setValueTextSize(9f);
        data.setDrawValues(false);

        // set data
        mChart.setData(data);
    }

}
