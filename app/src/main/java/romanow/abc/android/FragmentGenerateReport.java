package romanow.abc.android;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import firefighter.core.UniException;
import firefighter.core.reports.R01_TechnicianReport;
import romanow.abc.android.mapper.MapperToTable;
import romanow.abc.android.mapper.R01_TechnicianReportMapperImpl;
import romanow.abc.android.service.AppData;
import romanow.abc.android.service.NetBack;
import romanow.abc.android.service.NetCall;

public class FragmentGenerateReport extends Fragment {

    private MainActivity parent;
    private AppData ctx;
    private String sessionToken;
    private Button buttonGenerateReport;
    private MapperToTable mapperToTable;
    private FragmentReport fragmentReport;
    private FragmentTransaction fragmentTransaction;
    private ReportType reportType;
    private EditText editTextDateStart;
    private EditText editTextDateEnd;

    public FragmentGenerateReport(ReportType reportType) {
        this.reportType = reportType;
    }

    private long dateMS1 = 1L;
    private long dateMS2 = 0L;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_generate_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parent = (MainActivity) this.getActivity();
        ctx = AppData.ctx();
        sessionToken = ctx.loginSettings().getSessionToken();
        editTextDateStart = (EditText) view.findViewById(R.id.editTextDateStart);
        editTextDateEnd = (EditText) view.findViewById(R.id.editTextDateEnd);

        // формат editText
        TextWatcher tw1 = new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    editTextDateStart.setText(current);
                    editTextDateStart.setSelection(sel < current.length() ? sel : current.length());
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        };
        TextWatcher tw2 = new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    editTextDateEnd.setText(current);
                    editTextDateEnd.setSelection(sel < current.length() ? sel : current.length());
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        };
        editTextDateStart.addTextChangedListener(tw1);
        editTextDateEnd.addTextChangedListener(tw2);
        // end format editText

        buttonGenerateReport = (Button) view.findViewById(R.id.buttonGenerateReport);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = format.parse(editTextDateStart.getText().toString());
                    dateMS1 = date.getTime();
                } catch (ParseException ignore) {
                }
                try {
                    Date date = format.parse(editTextDateEnd.getText().toString());
                    dateMS2 = date.getTime();
                } catch (ParseException ignore) {
                }

                switch (reportType) {
                    case TECHNICIANREPORT:
                        getReportTechnicianReport();
                        break;
                }
            }
        };
        buttonGenerateReport.setOnClickListener(listener);
    }

    private void createFragmentReport(TableStruct tbl[][], String title) {
        fragmentReport = new FragmentReport(tbl, title);
        fragmentTransaction = parent.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.layoutMain, fragmentReport);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void getReportTechnicianReport(){

        new NetCall<R01_TechnicianReport>().call(parent, ctx.getService().createTechnicianReport(sessionToken, dateMS1, dateMS2, 0, 0), new NetBack() {
            @Override
            public void onError(int code, String mes) {
                ctx.toLog(false, "Ошибка keep alive: " + mes + "сервер недоступен");
                parent.popupInfo("Ошибка " + mes);
            }

            @Override
            public void onError(UniException ee) {
                ctx.toLog(false, "Ошибка keep alive: " + ee.toString() + "сервер недоступен");
                parent.popupInfo(ee.toString());
            }

            @Override
            public void onSuccess(Object val) {
                R01_TechnicianReport report = (R01_TechnicianReport) val;
                mapperToTable = new R01_TechnicianReportMapperImpl();
                TableStruct tbl[][] = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(tbl, title);
            }
        });
    }

}
