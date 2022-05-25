package naumov.abc.android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Date;
import java.util.GregorianCalendar;

import firefighter.core.UniException;
import firefighter.core.reports.R01_TechnicianReport;
import firefighter.core.reports.R02_PaymentReport;
import firefighter.core.reports.R03_ServiceCompanyReport;
import firefighter.core.reports.R04_FacilityReport;
import firefighter.core.reports.R05_08_PaymentReportCommon;
import firefighter.core.reports.R09_13_DeptReportCommon;
import firefighter.core.reports.R14_ContractorReport;
import firefighter.core.reports.R15_TechnicianPlanReport;
import firefighter.core.utils.OwnDateTime;
import naumov.abc.android.MainActivity;
import naumov.abc.android.R;
import naumov.abc.android.ReportType;
import naumov.abc.android.TableStruct;
import naumov.abc.android.mapper.MapperToTable;
import naumov.abc.android.mapper.R01_TechnicianReportMapperImpl;
import naumov.abc.android.mapper.R02_PaymentReportMapperImpl;
import naumov.abc.android.mapper.R03_ServiceCompanyReportMapperImpl;
import naumov.abc.android.mapper.R04_FacilityReportMapperImpl;
import naumov.abc.android.mapper.R05_08_PaymentReportCommonMapperImpl;
import naumov.abc.android.mapper.R09_13_DeptReportCommonMapperImpl;
import naumov.abc.android.mapper.R14_ContractorReportMapperImpl;
import naumov.abc.android.mapper.R15_TechnicianPlanReportMapperImpl;
import naumov.abc.android.service.AppData;
import naumov.abc.android.service.NetBack;
import naumov.abc.android.service.NetCall;
import naumov.abc.android.service.ReportService;

public class FragmentGenerateReport extends Fragment {

    private MainActivity parent;
    private AppData ctx;
    private String sessionToken;
    private Button buttonGenerateReport;
    private ReportService reportService;
    private FragmentTransaction fragmentTransaction;
    private ReportType reportType;
    private CalendarView calendarDateStart;
    private CalendarView calendarDateEnd;
    private TextView textViewDateStart;
    private TextView textViewDateEnd;

    public FragmentGenerateReport(ReportType reportType) {
        this.reportType = reportType;
    }

    private long dateMS1 = 1609459200000L;
    private long dateMS2 = 1622332800000L;
    private int year = 2021;


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
        reportService = new ReportService();
        sessionToken = ctx.loginSettings().getSessionToken();
        calendarDateStart = (CalendarView) view.findViewById(R.id.calendarView);
        calendarDateEnd = (CalendarView) view.findViewById(R.id.calendarView2);
        textViewDateStart = (TextView) view.findViewById(R.id.textViewDateStart);
        textViewDateEnd = (TextView) view.findViewById(R.id.textViewDateEnd);
        //----------------слушатели на календарь------------
        calendarDateStart.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year,
                                            int month, int dayOfMonth) {
                Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
                dateMS1 = date.getTime();
            }
        });
        calendarDateEnd.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year,
                                            int month, int dayOfMonth) {
                Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
                dateMS2 = date.getTime();
            }
        });
        //--------------------------------------

        //-------------------- выбор параметров генерации от отчёта--------------------------

        switch (reportType) {
            case TECHNICIANREPORT:
            case PAYMENTREPORT:
            case DEPT1REPORT:
            case DEPT2REPORT:
            case DEPT3REPORT:
            case DEPT4REPORT:
            case DEPT5REPORT:
                break;
            case SERVICECOMPANYREPORT:
            case TECHNICIANPLANREPORT:
                textViewDateStart.setText("Выберите дату отчёта");
                calendarDateEnd.setVisibility(View.GONE);
                textViewDateEnd.setVisibility(View.GONE);
                break;
            case FACILITYREPORT:
            case CONTRACTORREPORT:
                textViewDateStart.setText("Для данного отчёта нет параметров");
                calendarDateStart.setVisibility(View.GONE);
                calendarDateEnd.setVisibility(View.GONE);
                textViewDateEnd.setVisibility(View.GONE);
                break;
            case PAYMENT1REPORT:
            case PAYMENT2REPORT:
            case PAYMENT3REPORT:
            case PAYMENT4REPORT:
                textViewDateStart.setText("Выберите год отчёта");
                calendarDateEnd.setVisibility(View.GONE);
                textViewDateEnd.setVisibility(View.GONE);
                break;
        }

        //-------------------------------------------------------------------------------------

        buttonGenerateReport = (Button) view.findViewById(R.id.buttonGenerateReport);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.showDialogProgressBar();
                switch (reportType) {
                    case TECHNICIANREPORT:
                        reportService.getTechnicianReport(parent, ctx, sessionToken, dateMS1, dateMS2);
                        break;
                    case PAYMENTREPORT:
                        reportService.getPaymentReport(parent, ctx, sessionToken, dateMS1, dateMS2);
                        break;
                    case SERVICECOMPANYREPORT:
                        reportService.getServiceCompanyReport(parent, ctx, sessionToken, dateMS1);
                        break;
                    case FACILITYREPORT:
                        reportService.getFacilityReport(parent, ctx, sessionToken);
                        break;
                    case PAYMENT1REPORT:
                        reportService.getPayment1Report(parent, ctx, sessionToken, year);
                        break;
                    case PAYMENT2REPORT:
                        reportService.getPayment2Report(parent, ctx, sessionToken, year);
                        break;
                    case PAYMENT3REPORT:
                        reportService.getPayment3Report(parent, ctx, sessionToken, year);
                        break;
                    case PAYMENT4REPORT:
                        reportService.getPayment4Report(parent, ctx, sessionToken, year);
                        break;
                    case DEPT1REPORT:
                        reportService.getDept1Report(parent, ctx, sessionToken, dateMS1, dateMS2);
                        break;
                    case DEPT2REPORT:
                        reportService.getDept2Report(parent, ctx, sessionToken, dateMS1, dateMS2);
                        break;
                    case DEPT3REPORT:
                        reportService.getDept3Report(parent, ctx, sessionToken,dateMS1, dateMS2);
                        break;
                    case DEPT4REPORT:
                        reportService.getDept4Report(parent, ctx, sessionToken, dateMS1, dateMS2);
                        break;
                    case DEPT5REPORT:
                        reportService.getDept5Report(parent, ctx, sessionToken, dateMS1, dateMS2);
                        break;
                    case CONTRACTORREPORT:
                        reportService.getContractorReport(parent, ctx, sessionToken);
                        break;
                    case TECHNICIANPLANREPORT:
                        reportService.getTechnicianPlanReport(parent, ctx, sessionToken, dateMS1);
                        break;
                }
            }
        };
        buttonGenerateReport.setOnClickListener(listener);
    }

}
