package naumov.abc.android.fragment;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import naumov.abc.android.MainActivity;
import naumov.abc.android.R;
import naumov.abc.android.service.AppData;

public class FragmentGraph extends Fragment {

    private MainActivity parent;
    private AppData ctx;
    private Long[] tbl;
    private String[] names;
    private String title;
    private int type;
    private String unit;
    private int maxString = 15;
    AlertDialog alertDialog;


    public FragmentGraph(MainActivity parent, Long[] tbl, int type, String[] names, String title, String unit) {
        this.parent = parent;
        this.tbl = tbl;
        this.type = type;
        this.names = names;
        this.title = title;
        this.unit = unit;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        StringBuilder stringBuilder = new StringBuilder();
        Description description = new Description();
        switch (type) {
            case 1:
                view = inflater.inflate(R.layout.fragment_graph, container, false);
                BarChart barChart = (BarChart) view.findViewById(R.id.barChart);
                ArrayList<BarEntry> barEntries;
                ArrayList<String> labelStrings;
                barEntries = new ArrayList<>();
                labelStrings = new ArrayList<>();
                for (int i = 0; i < tbl.length; i++) {
                    stringBuilder.setLength(0);
                    barEntries.add(new BarEntry(i, tbl[i]));
                    if (names[i].length() > maxString) {
                        stringBuilder.append(names[i]);
                        stringBuilder.setLength(maxString);
                        stringBuilder.append("..");
                        labelStrings.add(stringBuilder.toString());
                    } else {
                        labelStrings.add(names[i]);
                    }

                }
                BarDataSet barDataSet = new BarDataSet(barEntries, title + "(" + unit + ")");
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                description.setText("");
                barChart.setDescription(description);
                BarData barData = new BarData(barDataSet);
                barChart.setData(barData);

                XAxis xAxis = barChart.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(labelStrings));
                xAxis.setPosition(XAxis.XAxisPosition.TOP);
                xAxis.setDrawGridLines(false);
                xAxis.setDrawAxisLine(false);
                xAxis.setDrawGridLinesBehindData(true);
                xAxis.setTextSize(15f);
                xAxis.setGranularity(1f);
                xAxis.setLabelCount(labelStrings.size());
                xAxis.setLabelRotationAngle(270);

                YAxis leftAxis = barChart.getAxisLeft();
                YAxis rightAxis = barChart.getAxisRight();
                leftAxis.setTextSize(15f);
                rightAxis.setEnabled(false);

                barChart.getLegend().setTextSize(15f);
                barChart.getBarData().setValueTextSize(15f);

                barChart.animateY(2000);
                barChart.setVisibleXRangeMaximum(5);
                barChart.moveViewToX(1);
                barChart.invalidate();
                barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                    @Override
                    public void onValueSelected(Entry e, Highlight h) {
                        int x = barChart.getBarData().getDataSetForEntry(e).getEntryIndex((BarEntry) e);

                        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
                        builder.setCancelable(true);
                        View nview = LayoutInflater.from(parent).inflate(R.layout.additional_info, null);
                        TextView label = nview.findViewById(R.id.label_graph);
                        TextView value = nview.findViewById(R.id.value_graph);
                        label.setText(names[x]);
                        value.setText(tbl[x] + " " + unit);
                        builder.setView(nview);
                        alertDialog = builder.create();
                        alertDialog.show();
                    }

                    @Override
                    public void onNothingSelected() {

                    }
                });

                break;

            default:
            case 2:
                view = inflater.inflate(R.layout.fragment_graph_pie, container, false);
                PieChart pieChart = (PieChart) view.findViewById(R.id.pieChart);
                ArrayList<PieEntry> pieEntries;
                pieEntries = new ArrayList<>();
                stringBuilder = new StringBuilder();
                Long sum = 0L;
                for (int i = 0; i < tbl.length; i++) {
                    stringBuilder.setLength(0);
                    sum += tbl[i];
                    if (names[i].length() > maxString) {
                        stringBuilder.append(names[i]);
                        stringBuilder.setLength(maxString);
                        stringBuilder.append("..");
                        pieEntries.add(new PieEntry(tbl[i], stringBuilder.toString()));
                    } else {
                        pieEntries.add(new PieEntry(tbl[i], names[i]));
                    }
                }
                PieDataSet pieDataSet = new PieDataSet(pieEntries, title + "(" + unit + ")");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setSliceSpace(3);
                description.setText("");
                pieChart.setDescription(description);

                PieData pieData = new PieData(pieDataSet);
                pieData.setValueTextSize(15f);
                pieData.setValueTextColor(Color.BLACK);

                pieChart.setData(pieData);

                Legend legend = pieChart.getLegend();
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                legend.setOrientation(Legend.LegendOrientation.VERTICAL);
                legend.setTextSize(10f);
                legend.setTextColor(Color.rgb(0,0,0));
                legend.setWordWrapEnabled(true);
                legend.setDrawInside(true);


                pieChart.setBackgroundColor(Color.rgb(240,240,240));
                pieChart.setEntryLabelTextSize(14f);
                pieChart.setEntryLabelColor(Color.rgb(40,40,40));
                pieChart.setCenterText("Сумма\n" + sum + unit);
                pieChart.setCenterTextSize(15f);
                pieChart.animateXY(2000, 2000);
                pieChart.invalidate();
                pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                    @Override
                    public void onValueSelected(Entry e, Highlight h) {
                        int x = pieChart.getData().getDataSet().getEntryIndex((PieEntry) e);

                        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
                        builder.setCancelable(true);
                        View nview = LayoutInflater.from(parent).inflate(R.layout.additional_info, null);
                        TextView label = nview.findViewById(R.id.label_graph);
                        TextView value = nview.findViewById(R.id.value_graph);
                        label.setText(names[x]);
                        value.setText(tbl[x] + " " + unit);
                        builder.setView(nview);
                        alertDialog = builder.create();
                        alertDialog.show();
                    }

                    @Override
                    public void onNothingSelected() {

                    }
                });
                break;

        }


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ctx = AppData.ctx();
    }
}
