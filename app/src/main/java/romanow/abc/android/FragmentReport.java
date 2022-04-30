package romanow.abc.android;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import romanow.abc.android.service.AppData;

public class FragmentReport extends Fragment {
    private MainActivity parent;
    private AppData ctx;
    private String sessionToken;
    private String title;
    private TableStruct[][] tbl;
    private TextView fld[][]=null;

    public FragmentReport(TableStruct[][] tableStruct, String title) {
        this.tbl = tableStruct;
        this.title = title;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parent = (MainActivity) this.getActivity();
        ctx = AppData.ctx();
        sessionToken = ctx.loginSettings().getSessionToken();
        createTable(view);

    }

    private void createTable(@NonNull View view){
        TextView textViewTitle = (TextView) view.findViewById(R.id.textReportName);
        textViewTitle.setText(title);
        LayoutInflater gen = this.getLayoutInflater();
        fld=new TextView[tbl.length][tbl[0].length];
        for(int i = 0; i < tbl.length; i++) fld[i] = new TextView[tbl[0].length];
        LinearLayout list;
        LinearLayout item;
        LinearLayout table = (LinearLayout) view.findViewById(R.id.tableEntry);
        list = (LinearLayout) gen.inflate(R.layout.table_list_items, null);

        for(int j = 0; j < tbl[0].length; j++) {
            item = (LinearLayout) gen.inflate(R.layout.table_head_item, null);
            TextView textView = (TextView) item.findViewById(R.id.textViewHead);
            textView.setText(tbl[0][j].getName());
            textView.setWidth(tbl[0][j].getWidth());
            textView.setHeight(tbl[0][j].getHeight());
            textView.setGravity(Gravity.CENTER);
            list.addView(item);
        }
        table.addView(list);

        for (int i = 1; i < tbl.length; i++) {
            list = (LinearLayout) gen.inflate(R.layout.table_list_items, null);
            for(int j = 0; j < tbl[0].length; j++) {
                item = (LinearLayout) gen.inflate(R.layout.table_item, null);
                TextView textView = (TextView) item.findViewById(R.id.textViewItem);
                textView.setText(tbl[i][j].getName());
                textView.setWidth(tbl[i][j].getWidth());
                textView.setHeight(tbl[i][j].getHeight());
                list.addView(item);
            }
            table.addView(list);
        }
    }
}
