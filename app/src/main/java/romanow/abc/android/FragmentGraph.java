package romanow.abc.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;

import romanow.abc.android.service.AppData;

public class FragmentGraph extends Fragment {

    private MainActivity parent;
    private AppData ctx;
    private TableStruct[][] tbl;
    private int iSelected;
    private int jSelected;


    public FragmentGraph(MainActivity parent, TableStruct[][] tbl, int iSelected, int jSelected, int type) {
        this.parent = parent;
        this.tbl = tbl;
        this.iSelected = iSelected;
        this.jSelected = jSelected;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);


        GraphViewSeries exampleSeries = new GraphViewSeries(
                new GraphView.GraphViewData[] { new GraphView.GraphViewData(1, 3),
                        new GraphView.GraphViewData(2, 6), new GraphView.GraphViewData(3, 6),
                        new GraphView.GraphViewData(4, 5), new GraphView.GraphViewData(5, 1)
                });
        GraphView graphView = new BarGraphView(parent,
                "Число пойманных мышек за неделю");
        graphView.addSeries(exampleSeries);

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.layoutGraph);
        layout.addView(graphView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ctx = AppData.ctx();
    }
}
