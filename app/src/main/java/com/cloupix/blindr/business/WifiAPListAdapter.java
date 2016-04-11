package com.cloupix.blindr.business;

import android.content.Context;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloupix.blindr.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.FillFormatter;
import com.github.mikephil.charting.interfaces.LineDataProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by AlonsoUSA on 12/5/15.
 *
 */
public class WifiAPListAdapter extends BaseAdapter {

    private ArrayList<WifiAPRow> listWifiAPRow;
    private LayoutInflater mInflater;
    private Context context;

    public WifiAPListAdapter(Context context) {
        this.context = context;
        this.listWifiAPRow = new ArrayList<>();
        this.mInflater = LayoutInflater.from(context);
        this.listWifiAPRow = new ArrayList<>();
    }
    @Override
    public int getCount() {
        return listWifiAPRow.size();
    }

    @Override
    public Object getItem(int position) {
        return listWifiAPRow.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.row_wifi_ap, null);
            holder = new ViewHolder();
            holder.textViewSSID = (TextView) convertView.findViewById(R.id.textViewSSID);
            holder.textViewBSSID = (TextView) convertView.findViewById(R.id.textViewBSSID);
            holder.textViewRSSIValue = (TextView) convertView.findViewById(R.id.textViewRSSIValue);
            holder.textViewApNumber = (TextView) convertView.findViewById(R.id.textViewApNumber);
            holder.imgViewCircle = (ImageView) convertView.findViewById(R.id.imgViewCircle);
            holder.lineChart = (LineChart) convertView.findViewById(R.id.chart);

            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        WifiAPRow wifiApRow = listWifiAPRow.get(position);

        holder.textViewSSID.setText(wifiApRow.getSSSID());
        holder.textViewBSSID.setText(wifiApRow.getBSSID());
        holder.textViewApNumber.setText(Integer.toString(wifiApRow.getApNumber()));
        holder.imgViewCircle.setImageResource(wifiApRow.getImgCircleRes());

        ArrayList<WifiAPLecture> wifiAPLectures = wifiApRow.getLectures();
        if(wifiAPLectures!=null && !wifiAPLectures.isEmpty()) {
            WifiAPLecture lecture = wifiAPLectures.get(wifiAPLectures.size()-1);
            holder.textViewRSSIValue.setText(lecture.getScanResult().level + "dB");


            // Chart
            setupChart(holder.lineChart, wifiApRow);

        }
        return convertView;
    }

    static class ViewHolder {
        TextView textViewSSID, textViewBSSID, textViewRSSIValue, textViewApNumber;
        ImageView imgViewCircle;
        LineChart lineChart;
    }

    private void setupChart(LineChart mChart, WifiAPRow wifiApRow) {
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
        setData(mChart, wifiApRow);

        mChart.setDescription("");    // Hide the description
        mChart.getAxisLeft().setDrawLabels(false);
        mChart.getAxisRight().setDrawLabels(false);
        mChart.getXAxis().setDrawLabels(false);

        mChart.getLegend().setEnabled(false);   // Hide the legend

        mChart.getAxisRight().setEnabled(false);

        // dont forget to refresh the drawing
        mChart.invalidate();
    }

    private void setData(LineChart mChart, WifiAPRow wifiApRow){

        // Sacamos los valores de las x
        ArrayList<String> xVals = new ArrayList<String>();
        for (WifiAPLecture lecture : wifiApRow.getLectures()) {

            xVals.add(lecture.getScanResult().timestamp + "");
        }

        //Sacamos los valores de Y
        ArrayList<Entry> vals1 = new ArrayList<Entry>();

        for (int i = 0; i < wifiApRow.getLectures().size(); i++) {
            vals1.add(new Entry(wifiApRow.getLectures().get(i).getScanResult().level, i));
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
        set1.setFillFormatter(new FillFormatter() {
            @Override
            public float getFillLinePosition(LineDataSet dataSet, LineDataProvider dataProvider) {
                return -10;
            }
        });

        // create a data object with the datasets
        LineData data = new LineData(xVals, set1);
        //data.setValueTypeface(tf);
        data.setValueTextSize(9f);
        data.setDrawValues(false);

        // set data
        mChart.setData(data);
    }

    /*
    public void addWifiAP(WifiAP wifiAP) {
        boolean exists = false;
        for (WifiAPRow cell : listWifiAPRow) {
            // List.Cell.Mac = NewWifi.Mac
            if(cell.getMac().equals(wifiAP.getMac())){
                cell.addWifiAPLectures(wifiAP.getLectures());
                exists = true;
                break;
            }
        }
        // Si no existía lo creamos y le asignamos yn numero y un color de circulo
        if(!exists) {
            // TODO Echar un opjo a esto a ver si da lo que tiene que dar
            int circleRes = WifiAPRow.allCircleRes[listWifiAPRow.size() % WifiAPRow.allCircleRes.length];
            WifiAPRow newCell = new WifiAPRow(wifiAP.getSsid(), wifiAP.getMac(), wifiAP.getLectures(), circleRes, listWifiAPRow.size()+1);
            listWifiAPRow.add(newCell);
        }

    }
    */


    public void addScanResultList(List<ScanResult> scanResultList) {

        for(ScanResult scanResult : scanResultList) {
            // Creamos el envelope
            WifiAPLecture wifiAPLecture = new WifiAPLecture(scanResult);


            boolean exists = false;
            for (WifiAPRow cell : listWifiAPRow) {
                // List.Cell.Mac = NewWifi.Mac
                if(cell.getBSSID().equals(scanResult.BSSID)){

                    // Lo metemos en la lista de lecturas de la celda
                    cell.addWifiAPLecture(wifiAPLecture);
                    exists = true;
                    break;
                }
            }
            // Si no existía lo creamos y le asignamos un numero y un color de circulo
            if(!exists) {
                // TODO Echar un opjo a esto a ver si da lo que tiene que dar
                int circleRes = WifiAPRow.allCircleRes[listWifiAPRow.size() % WifiAPRow.allCircleRes.length];
                // Aqui es donde sacamos todo lo que queremos del scanResult
                WifiAPRow newRow = new WifiAPRow(scanResult.SSID, scanResult.BSSID, wifiAPLecture, circleRes, listWifiAPRow.size()+1);
                listWifiAPRow.add(newRow);
            }
        }


        // Ordenamos la lista
        Collections.sort(listWifiAPRow, new WifiAPComparator());

    }


}
