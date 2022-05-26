package naumov.abc.android.fragment;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import naumov.abc.android.MainActivity;
import naumov.abc.android.dialog.MultiListBoxDialogGraph;
import naumov.abc.android.MultiListBoxListener;
import naumov.abc.android.R;
import naumov.abc.android.TableStruct;
import naumov.abc.android.service.AppData;

public class FragmentReport extends Fragment {
    private MainActivity parent;
    private AppData ctx;
    private String sessionToken;
    private String title;
    private TableStruct[][] tbl;
    private TextView fld[][]=null;
    private Button buttonOrientation;
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
        buttonOrientation = (Button) view.findViewById(R.id.buttonOrientation);
        buttonOrientation.setClickable(true);
        buttonOrientation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    parent.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    parent.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        });
        createTable(view);
        parent.hideDialogProgressBar();
    }

    @Override
    public void onPause() {
        parent.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onPause();
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

            final int iSelect = 0;
            final int jSelect = j;
            // слушатель на выделение колонки
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
                    addSelectCol();
                }
            });
            // создание гистограммы слушатель
            if (tbl[0][j].getGraph() == 1) {
                item.setClickable(true);
                item.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        int ind = tbl[0][jSelect].getIndexName();
                        ArrayList<String> list = (ArrayList<String>) Arrays.stream(tbl)
                                .skip(1)
                                .limit(tbl[0][jSelect].getDataSize())
                                .map(e -> Arrays.stream(e)
                                        .skip(ind)
                                        .map(ee -> ee.getName())
                                        .findFirst()
                                        .get())
                                .collect(Collectors.toList());
                        new MultiListBoxDialogGraph(parent,
                                "Параметры для гистограммы",
                                list,
                                new MultiListBoxListener() {
                                    @Override
                                    public void onSelect(boolean[] selected) {
                                        listIndex.clear();
                                        for (int i = 0; i < selected.length; i++) {
                                            if (selected[i])listIndex.add(i);
                                        }
                                    }},
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String title = tbl[0][jSelect].getName();
                                        Long[] tblTemp = new Long[listIndex.size()];
                                        String[] names = new String[listIndex.size()];
                                        int l = 0;
                                        for (int i : listIndex) {
                                            tblTemp[l] = tbl[i + 1][jSelect].getValue();
                                            names[l++] = tbl[i + 1][tbl[0][jSelect].getIndexName()].getName();
                                        }
                                        fragmentGraph = new FragmentGraph(parent,
                                                tblTemp, 1, names, title, tbl[0][jSelect].getUnit());
                                        fragmentTransaction = parent.getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.layoutMain, fragmentGraph);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                    }});
                        return true;
                    }
                });
            }
            // ----------------------------------------------------------------------------------
            //------------- Создания Круговой диаграммы ------------------------------------------
            if (tbl[0][j].getGraph() == 2) {
                item.setClickable(true);
                item.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        int ind = tbl[0][jSelect].getIndexName();
                        ArrayList<String> list = (ArrayList<String>) Arrays.stream(tbl)
                                .skip(1)
                                .limit(tbl[0][jSelect].getDataSize())
                                .map(e -> Arrays.stream(e)
                                        .skip(ind)
                                        .map(ee -> ee.getName())
                                        .findFirst()
                                        .get())
                                .collect(Collectors.toList());
                        new MultiListBoxDialogGraph(parent,
                                "Параметры для круговой диаграммы",
                                list,
                                new MultiListBoxListener() {
                                    @Override
                                    public void onSelect(boolean[] selected) {
                                        listIndex.clear();
                                        for (int i = 0; i < selected.length; i++) {
                                            if (selected[i])listIndex.add(i);
                                        }
                                    }},
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String title = tbl[0][jSelect].getName();
                                        Long[] tblTemp = new Long[listIndex.size()];
                                        String[] names = new String[listIndex.size()];
                                        int l = 0;
                                        for (int i : listIndex) {
                                            tblTemp[l] = tbl[i + 1][jSelect].getValue();
                                            names[l++] = tbl[i + 1][tbl[0][jSelect].getIndexName()].getName();
                                        }
                                        fragmentGraph = new FragmentGraph(parent, tblTemp, 2, names, title, tbl[0][jSelect].getUnit());
                                        fragmentTransaction = parent.getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.layoutMain, fragmentGraph);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                    }});
                        return true;
                    }
                });
            }
            //-------------------------------------------------------------------------
            list.addView(item);
        }
        table.addView(list);

        for (int i = 1; i < tbl.length; i++) {
            list = (LinearLayout) gen.inflate(R.layout.table_list_items, null);
            // создание колонок с данными
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
                if (j == 0) {
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
                            addSelectRow();
                        }
                    });
                } else {
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
                }

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
    private void addSelectRow() {
        for (int j = 0; j < tbl[0].length; j++) {
            fld[iSelected][j].setBackgroundResource(R.drawable.back_table_selected);
        }
    }
    private void addSelectCol() {
        for (int i = 1; i < tbl.length; i++) {
            fld[i][jSelected].setBackgroundResource(R.drawable.back_table_selected);
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
            case 44:
                textView.setBackgroundResource(R.drawable.back_table_head_pie);
                break;
            default:
                textView.setBackgroundResource(R.drawable.back_table);
        }
    }
}
