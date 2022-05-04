package romanow.abc.android;

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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import romanow.abc.android.service.AppData;

public class FragmentGraph extends Fragment {

    private MainActivity parent;
    private AppData ctx;
    private TableStruct[][] tbl;
    private int iSelected;
    private int jSelected;
    private int type;


    public FragmentGraph(MainActivity parent, TableStruct[][] tbl, int iSelected, int jSelected, int type) {
        this.parent = parent;
        this.tbl = tbl;
        this.iSelected = iSelected;
        this.jSelected = jSelected;
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        switch (type) {
            case 1:
                BarGraphSeries<DataPoint> seriesBar = new BarGraphSeries<DataPoint>(new DataPoint[] {
                        new DataPoint(0, 1),
                        new DataPoint(1, 5),
                        new DataPoint(2, 3),
                        new DataPoint(3, 2),
                        new DataPoint(4, 6)
                });
                graph.addSeries(seriesBar);
                graph.setTitle("Гистограмма " + tbl[iSelected][jSelected].getName());
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



        graph.setTitleTextSize(100);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ctx = AppData.ctx();
    }
}
