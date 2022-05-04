package romanow.abc.android;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

//import com.jjoe64.graphview.BarGraphView;
//import com.jjoe64.graphview.GraphView;
//import com.jjoe64.graphview.GraphViewSeries;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Arrays;

import romanow.abc.android.service.AppData;

public class FragmentGraph extends Fragment {

    private MainActivity parent;
    private AppData ctx;
    private Long[] tbl;
    private String[] names;
    private String title;
    private int type;


    public FragmentGraph(MainActivity parent, Long[] tbl, int type, String[] names, String title) {
        this.parent = parent;
        this.tbl = tbl;
        this.type = type;
        this.names = names;
        this.title = title;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        switch (type) {
            case 1:
                DataPoint[] data;
                if(tbl.length > 3){
                    data = new DataPoint[8];
                    data[0] = new DataPoint(0, 0);
                    for (int i = 1; i < tbl.length + 1; i++) {
                        data[i] = new DataPoint(i, tbl[i - 1]);
                    }
                    if (tbl.length < 6) {
                        for (int i = tbl.length + 1; i < 8; i++) {
                            data[i] = new DataPoint(i, 0);
                        }
                    }
                    data[7] = new DataPoint(7, 0);
                } else {
                    data = new DataPoint[5];
                    data[0] = new DataPoint(0, 0);
                    for (int i = 1; i < tbl.length + 1; i++) {
                        data[i] = new DataPoint(i, tbl[i - 1]);
                    }
                    if (tbl.length < 3) {
                        for (int i = tbl.length + 1; i < 5; i++) {
                            data[i] = new DataPoint(i, 0);
                        }
                    }
                    data[4] = new DataPoint(4, 0);
                }
                BarGraphSeries<DataPoint> seriesBar = new BarGraphSeries(data);
                //изменения цвета гистограмм
                seriesBar.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                    @Override
                    public int get(DataPoint data) {
                        return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
                    }
                });
                // изменение промежутков
                seriesBar.setSpacing(15);

                //подписи сверху
                seriesBar.setDrawValuesOnTop(true);
                seriesBar.setValuesOnTopColor(Color.RED);


                //установка данных в граф
                graph.addSeries(seriesBar);

                //колличество столбцов
                if(tbl.length > 3) {
                    graph.getGridLabelRenderer().setNumHorizontalLabels(8);
                } else {
                    graph.getGridLabelRenderer().setNumHorizontalLabels(5);
                }

                // титул графа
                graph.setTitle(title);


                // изменение названий x
                graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if (isValueX) {
                            if((int) value == 0) {
                                return "";
                            }
                            if((int) value < tbl.length + 1) {
                                return names[((int) value) - 1];
                            }
                            return "";
                        } else {
                            // show currency for y values
                            return super.formatLabel(value, isValueX) + "р";
                        }
                    }
                });

                break;


            default:
                LineGraphSeries<DataPoint> seriesLine = new LineGraphSeries<DataPoint>(new DataPoint[] {
                        new DataPoint(0, 1),
                        new DataPoint(1, 5),
                        new DataPoint(2, 3),
                        new DataPoint(3, 2),
                        new DataPoint(4, 6)
                });
                graph.addSeries(seriesLine);
        }


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ctx = AppData.ctx();
    }
}
