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
import androidx.fragment.app.FragmentTransaction;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import romanow.abc.android.service.AppData;

public class FragmentReport extends Fragment {
    private MainActivity parent;
    private AppData ctx;
    private String sessionToken;
    private String title;
    private TableStruct[][] tbl;
    private TextView fld[][]=null;
    private int iSelected;
    private int jSelected;
    private FragmentGraph fragmentGraph;
    private FragmentTransaction fragmentTransaction;
    private ArrayList<Integer> listIndex = new ArrayList<>();


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
        fld = new TextView[tbl.length][tbl[0].length];
        for(int i = 0; i < tbl.length; i++) fld[i] = new TextView[tbl[0].length];
        LinearLayout list;
        LinearLayout item;
        LinearLayout table = (LinearLayout) view.findViewById(R.id.tableEntry);
        list = (LinearLayout) gen.inflate(R.layout.table_list_items, null);

        for(int j = 0; j < tbl[0].length; j++) {
            item = (LinearLayout) gen.inflate(R.layout.table_head_item, null);
            TextView textView = (TextView) item.findViewById(R.id.textViewHead);
            fld[0][j] = textView;
            textView.setText(tbl[0][j].getName());
            textView.setWidth(tbl[0][j].getWidth());
            textView.setHeight(tbl[0][j].getHeight());
            textView.setGravity(Gravity.CENTER);
            setBackgroundText(textView, tbl[0][j]);
            final int jSelect = j;
            if (tbl[0][j].getGraph() == 1) {
                item.setClickable(true);
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int ind = tbl[0][jSelect].getIndexName();
                        int selectedCount = 0;
                        ArrayList<String> list = (ArrayList<String>) Arrays.stream(tbl)
                                .skip(1)
                                .map(e -> "№ п/п " + Arrays.stream(e)
                                        .skip(ind)
                                        .map(ee -> ee.getName())
                                        .findFirst()
                                        .get())
                                .collect(Collectors.toList());
                        new MultiListBoxDialogGraph(parent, "Параметры для гистограммы", list, new MultiListBoxListener() {
                            @Override
                            public void onSelect(boolean[] selected) {
                                listIndex.clear();
                                for (int i = 0; i < selected.length; i++) {
                                    if (selected[i])listIndex.add(i);
                                }
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String title = "Гистограмма по столбцу \"" + tbl[0][jSelect].getName().toLowerCase()+"\"";
                                Long[] tblTemp = new Long[listIndex.size()];
                                String[] names = new String[listIndex.size()];
                                int l = 0;
                                for (int i : listIndex) {
                                    tblTemp[l] = tbl[i + 1][jSelect].getValue();
                                    names[l++] = "№" + tbl[i + 1][tbl[0][jSelect].getIndexName()].getName();
                                }
                                fragmentGraph = new FragmentGraph(parent, tblTemp, 1, names, title);
                                fragmentTransaction = parent.getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.layoutMain, fragmentGraph);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        }, 6);

//                        new MultiListBoxDialog(parent,
//                                "Выберите данные для гистограммы",
//                                list
//                                , new MultiListBoxListener() {
//                            @Override
//                            public void onSelect(boolean[] selected) {
//                                ctx.toLog(false,"ура");
//                            }
//                        });
//                        String title = "Гистограмма по столбцу \"" + tbl[0][jSelect].getName().toLowerCase()+"\"";
//                        Long[] tblTemp = new Long[6];
//                        String[] names = new String[6];
//                        for (int i = 0; i < 6; i++) {
//                            tblTemp[i] = tbl[i + 1][jSelect].getValue();
//                            names[i] = "№" + tbl[i + 1][tbl[0][jSelect].getIndexName()].getName();
//                        }
//                        fragmentGraph = new FragmentGraph(parent, tblTemp, 1, names, title);
//                        fragmentTransaction = parent.getSupportFragmentManager().beginTransaction();
//                        fragmentTransaction.replace(R.id.layoutMain, fragmentGraph);
//                        fragmentTransaction.addToBackStack(null);
//                        fragmentTransaction.commit();
                    }
                });
            }
            list.addView(item);
        }
        table.addView(list);

        for (int i = 1; i < tbl.length; i++) {
            list = (LinearLayout) gen.inflate(R.layout.table_list_items, null);
            for(int j = 0; j < tbl[0].length; j++) {
                final int iSelect = i;
                final int jSelect = j;
                item = (LinearLayout) gen.inflate(R.layout.table_item, null);
                item.setClickable(true);
                TextView textView = (TextView) item.findViewById(R.id.textViewItem);
                fld[i][j] = textView;
                textView.setText(tbl[i][j].getName());
                textView.setWidth(tbl[i][j].getWidth());
                textView.setHeight(tbl[i][j].getHeight());
                setBackgroundText(textView, tbl[i][j]);
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clearSelect();
                        if ((iSelected == iSelect) && (jSelected == jSelect)) {
                            iSelected = 0;
                            jSelected = 0;
                            return;
                        }
                        iSelected = iSelect;
                        jSelected = jSelect;
                        addSelect();
                    }
                });
                list.addView(item);
            }
            table.addView(list);
        }
    }

    private void clearSelect() {
        for (int i = 1; i < tbl.length; i++) {
            setBackgroundText(fld[i][jSelected], tbl[i][jSelected]);
        }
        for (int j = 0; j < tbl[0].length; j++) {
            setBackgroundText(fld[iSelected][j], tbl[iSelected][j]);
        }
    }

    private void addSelect() {
        for (int i = 1; i < tbl.length; i++) {
            fld[i][jSelected].setBackgroundResource(R.drawable.back_table_selected);
        }
        for (int j = 0; j < tbl[0].length; j++) {
            fld[iSelected][j].setBackgroundResource(R.drawable.back_table_selected);
        }
    }

    private void setBackgroundText(TextView textView, TableStruct tableStruct) {
        switch (tableStruct.getStyle()) {
            case 1:
                textView.setBackgroundResource(R.drawable.back_table_was_sended);
                break;
            case 2:
                textView.setBackgroundResource(R.drawable.back_table_done);
                break;
            case 3:
                textView.setBackgroundResource(R.drawable.back_table_need_to_pay);
                break;
            case 4:
                textView.setBackgroundResource(R.drawable.back_table_selected);
                break;
            case 7:
                textView.setBackgroundResource(R.drawable.back_table_brown);
                break;
            case 42:
                textView.setBackgroundResource(R.drawable.back_table_head);
                break;
            case 43:
                textView.setBackgroundResource(R.drawable.back_table_head_gist);
                break;
            default:
                textView.setBackgroundResource(R.drawable.back_table);
        }
    }
}
