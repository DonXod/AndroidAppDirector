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
import naumov.abc.android.enumeration.ReportType;
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

public class FragmentGenerateReport extends Fragment {

    private MainActivity parent;
    private AppData ctx;
    private String sessionToken;
    private Button buttonGenerateReport;
    private MapperToTable mapperToTable;
    private FragmentReport fragmentReport;
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
                switch (reportType) {
                    case TECHNICIANREPORT:
                        parent.showDialogProgressBar();
                        getTechnicianReport();
                        break;
                    case PAYMENTREPORT:
                        parent.showDialogProgressBar();
                        getPaymentReport();
                        break;
                    case SERVICECOMPANYREPORT:
                        parent.showDialogProgressBar();
                        getServiceCompanyReport();
                        break;
                    case FACILITYREPORT:
                        parent.showDialogProgressBar();
                        getFacilityReport();
                        break;
                    case PAYMENT1REPORT:
                        parent.showDialogProgressBar();
                        getPayment1Report();
                        break;
                    case PAYMENT2REPORT:
                        parent.showDialogProgressBar();
                        getPayment2Report();
                        break;
                    case PAYMENT3REPORT:
                        parent.showDialogProgressBar();
                        getPayment3Report();
                        break;
                    case PAYMENT4REPORT:
                        parent.showDialogProgressBar();
                        getPayment4Report();
                        break;
                    case DEPT1REPORT:
                        parent.showDialogProgressBar();
                        getDept1Report();
                        break;
                    case DEPT2REPORT:
                        parent.showDialogProgressBar();
                        getDept2Report();
                        break;
                    case DEPT3REPORT:
                        parent.showDialogProgressBar();
                        getDept3Report();
                        break;
                    case DEPT4REPORT:
                        parent.showDialogProgressBar();
                        getDept4Report();
                        break;
                    case DEPT5REPORT:
                        parent.showDialogProgressBar();
                        getDept5Report();
                        break;
                    case CONTRACTORREPORT:
                        parent.showDialogProgressBar();
                        getContractorReport();
                        break;
                    case TECHNICIANPLANREPORT:
                        parent.showDialogProgressBar();
                        getTechnicianPlanReport();
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

    private void getTechnicianReport(){

        new NetCall<R01_TechnicianReport>().call(parent, ctx.getService().createTechnicianReport(sessionToken, dateMS1, dateMS2, 0, 0), new NetBack() {
            @Override
            public void onError(int code, String mes) {
                ctx.toLog(false, "Ошибка keep alive: " + mes + "сервер недоступен");
                parent.popupInfo("Ошибка " + mes);
                parent.hideDialogProgressBar();
            }

            @Override
            public void onError(UniException ee) {
                ctx.toLog(false, "Ошибка keep alive: " + ee.toString() + "сервер недоступен");
                parent.popupInfo(ee.toString());
                parent.hideDialogProgressBar();
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

    private void getPaymentReport(){

        OwnDateTime temp = new OwnDateTime(dateMS1);
        temp.decMonth();
        dateMS1 = temp.timeInMS();
        new NetCall<R02_PaymentReport>().call(parent, ctx.getService().createPaymentReport(sessionToken, 0,  dateMS1, dateMS2, 0), new NetBack() {
            @Override
            public void onError(int code, String mes) {
                ctx.toLog(false, "Ошибка keep alive: " + mes + "сервер недоступен");
                parent.popupInfo("Ошибка " + mes);
                parent.hideDialogProgressBar();
            }

            @Override
            public void onError(UniException ee) {
                ctx.toLog(false, "Ошибка keep alive: " + ee.toString() + "сервер недоступен");
                parent.popupInfo(ee.toString());
                parent.hideDialogProgressBar();
            }

            @Override
            public void onSuccess(Object val) {
                R02_PaymentReport report = (R02_PaymentReport) val;
                mapperToTable = new R02_PaymentReportMapperImpl();
                TableStruct tbl[][] = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(tbl, title);
            }
        });
    }

    private void getServiceCompanyReport(){

        new NetCall<R03_ServiceCompanyReport>().call(parent, ctx.getService().createServiceCompanyReport(sessionToken, dateMS1, 0), new NetBack() {
            @Override
            public void onError(int code, String mes) {
                ctx.toLog(false, "Ошибка keep alive: " + mes + "сервер недоступен");
                parent.popupInfo("Ошибка " + mes);
                parent.hideDialogProgressBar();
            }

            @Override
            public void onError(UniException ee) {
                ctx.toLog(false, "Ошибка keep alive: " + ee.toString() + "сервер недоступен");
                parent.popupInfo(ee.toString());
                parent.hideDialogProgressBar();
            }

            @Override
            public void onSuccess(Object val) {
                R03_ServiceCompanyReport report = (R03_ServiceCompanyReport) val;
                mapperToTable = new R03_ServiceCompanyReportMapperImpl();
                TableStruct tbl[][] = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(tbl, title);
            }
        });
    }

    private void getFacilityReport(){

        new NetCall<R04_FacilityReport>().call(parent, ctx.getService().createFacilitiesReport(sessionToken, 0, 0), new NetBack() {
            @Override
            public void onError(int code, String mes) {
                ctx.toLog(false, "Ошибка keep alive: " + mes + "сервер недоступен");
                parent.popupInfo("Ошибка " + mes);
                parent.hideDialogProgressBar();
            }

            @Override
            public void onError(UniException ee) {
                ctx.toLog(false, "Ошибка keep alive: " + ee.toString() + "сервер недоступен");
                parent.popupInfo(ee.toString());
                parent.hideDialogProgressBar();
            }

            @Override
            public void onSuccess(Object val) {
                R04_FacilityReport report = (R04_FacilityReport) val;
                mapperToTable = new R04_FacilityReportMapperImpl();
                TableStruct tbl[][] = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(tbl, title);
            }
        });
    }

    private void getPayment1Report(){

        new NetCall<R05_08_PaymentReportCommon>().call(parent, ctx.getService().createPaymentReport1(sessionToken, 0, year, 0), new NetBack() {
            @Override
            public void onError(int code, String mes) {
                ctx.toLog(false, "Ошибка keep alive: " + mes + "сервер недоступен");
                parent.popupInfo("Ошибка " + mes);
                parent.hideDialogProgressBar();
            }

            @Override
            public void onError(UniException ee) {
                ctx.toLog(false, "Ошибка keep alive: " + ee.toString() + "сервер недоступен");
                parent.popupInfo(ee.toString());
                parent.hideDialogProgressBar();
            }

            @Override
            public void onSuccess(Object val) {
                R05_08_PaymentReportCommon report = (R05_08_PaymentReportCommon) val;
                mapperToTable = new R05_08_PaymentReportCommonMapperImpl();
                TableStruct tbl[][] = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(tbl, title);
            }
        });
    }

    private void getPayment2Report(){

        new NetCall<R05_08_PaymentReportCommon>().call(parent, ctx.getService().createPaymentReport2(sessionToken, 0,  year, 0), new NetBack() {
            @Override
            public void onError(int code, String mes) {
                ctx.toLog(false, "Ошибка keep alive: " + mes + "сервер недоступен");
                parent.popupInfo("Ошибка " + mes);
                parent.hideDialogProgressBar();
            }

            @Override
            public void onError(UniException ee) {
                ctx.toLog(false, "Ошибка keep alive: " + ee.toString() + "сервер недоступен");
                parent.popupInfo(ee.toString());
                parent.hideDialogProgressBar();
            }

            @Override
            public void onSuccess(Object val) {
                R05_08_PaymentReportCommon report = (R05_08_PaymentReportCommon) val;
                mapperToTable = new R05_08_PaymentReportCommonMapperImpl();
                TableStruct tbl[][] = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(tbl, title);
            }
        });
    }

    private void getPayment3Report(){

        new NetCall<R05_08_PaymentReportCommon>().call(parent, ctx.getService().createPaymentReport3(sessionToken, year, 0), new NetBack() {
            @Override
            public void onError(int code, String mes) {
                ctx.toLog(false, "Ошибка keep alive: " + mes + "сервер недоступен");
                parent.popupInfo("Ошибка " + mes);
                parent.hideDialogProgressBar();
            }

            @Override
            public void onError(UniException ee) {
                ctx.toLog(false, "Ошибка keep alive: " + ee.toString() + "сервер недоступен");
                parent.popupInfo(ee.toString());
                parent.hideDialogProgressBar();
            }

            @Override
            public void onSuccess(Object val) {
                R05_08_PaymentReportCommon report = (R05_08_PaymentReportCommon) val;
                mapperToTable = new R05_08_PaymentReportCommonMapperImpl();
                TableStruct tbl[][] = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(tbl, title);
            }
        });
    }

    private void getPayment4Report(){

        new NetCall<R05_08_PaymentReportCommon>().call(parent, ctx.getService().createPaymentReport4(sessionToken, year, 0), new NetBack() {
            @Override
            public void onError(int code, String mes) {
                ctx.toLog(false, "Ошибка keep alive: " + mes + "сервер недоступен");
                parent.popupInfo("Ошибка " + mes);
                parent.hideDialogProgressBar();
            }

            @Override
            public void onError(UniException ee) {
                ctx.toLog(false, "Ошибка keep alive: " + ee.toString() + "сервер недоступен");
                parent.popupInfo(ee.toString());
                parent.hideDialogProgressBar();
            }

            @Override
            public void onSuccess(Object val) {
                R05_08_PaymentReportCommon report = (R05_08_PaymentReportCommon) val;
                mapperToTable = new R05_08_PaymentReportCommonMapperImpl();
                TableStruct tbl[][] = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(tbl, title);
            }
        });
    }

    private void getDept1Report(){
        OwnDateTime temp = new OwnDateTime(dateMS1);
        temp.decMonth();
        dateMS1 = temp.timeInMS();
        new NetCall<R09_13_DeptReportCommon>().call(parent, ctx.getService().createDeptContractorReport(sessionToken, 0,  dateMS1, dateMS2, false, 0), new NetBack() {
            @Override
            public void onError(int code, String mes) {
                ctx.toLog(false, "Ошибка keep alive: " + mes + "сервер недоступен");
                parent.popupInfo("Ошибка " + mes);
                parent.hideDialogProgressBar();
            }

            @Override
            public void onError(UniException ee) {
                ctx.toLog(false, "Ошибка keep alive: " + ee.toString() + "сервер недоступен");
                parent.popupInfo(ee.toString());
                parent.hideDialogProgressBar();
            }

            @Override
            public void onSuccess(Object val) {
                R09_13_DeptReportCommon report = (R09_13_DeptReportCommon) val;
                mapperToTable = new R09_13_DeptReportCommonMapperImpl();
                TableStruct tbl[][] = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(tbl, title);
            }
        });
    }

    private void getDept2Report(){
        OwnDateTime temp = new OwnDateTime(dateMS1);
        temp.decMonth();
        dateMS1 = temp.timeInMS();
        new NetCall<R09_13_DeptReportCommon>().call(parent, ctx.getService().createDeptTechnicianReport(sessionToken, dateMS1, dateMS2, false, 0), new NetBack() {
            @Override
            public void onError(int code, String mes) {
                ctx.toLog(false, "Ошибка keep alive: " + mes + "сервер недоступен");
                parent.popupInfo("Ошибка " + mes);
                parent.hideDialogProgressBar();
            }

            @Override
            public void onError(UniException ee) {
                ctx.toLog(false, "Ошибка keep alive: " + ee.toString() + "сервер недоступен");
                parent.popupInfo(ee.toString());
                parent.hideDialogProgressBar();
            }

            @Override
            public void onSuccess(Object val) {
                R09_13_DeptReportCommon report = (R09_13_DeptReportCommon) val;
                mapperToTable = new R09_13_DeptReportCommonMapperImpl();
                TableStruct tbl[][] = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(tbl, title);
            }
        });
    }

    private void getDept3Report(){
        OwnDateTime temp = new OwnDateTime(dateMS1);
        temp.decMonth();
        dateMS1 = temp.timeInMS();
        new NetCall<R09_13_DeptReportCommon>().call(parent, ctx.getService().createDeptServiceReport(sessionToken, dateMS1, dateMS2, false, 0), new NetBack() {
            @Override
            public void onError(int code, String mes) {
                ctx.toLog(false, "Ошибка keep alive: " + mes + "сервер недоступен");
                parent.popupInfo("Ошибка " + mes);
                parent.hideDialogProgressBar();
            }

            @Override
            public void onError(UniException ee) {
                ctx.toLog(false, "Ошибка keep alive: " + ee.toString() + "сервер недоступен");
                parent.popupInfo(ee.toString());
                parent.hideDialogProgressBar();
            }

            @Override
            public void onSuccess(Object val) {
                R09_13_DeptReportCommon report = (R09_13_DeptReportCommon) val;
                mapperToTable = new R09_13_DeptReportCommonMapperImpl();
                TableStruct tbl[][] = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(tbl, title);
            }
        });
    }

    private void getDept4Report(){
        OwnDateTime temp = new OwnDateTime(dateMS1);
        temp.decMonth();
        dateMS1 = temp.timeInMS();
        new NetCall<R09_13_DeptReportCommon>().call(parent, ctx.getService().createContractContractorReport(sessionToken, dateMS1, dateMS2, 0), new NetBack() {
            @Override
            public void onError(int code, String mes) {
                ctx.toLog(false, "Ошибка keep alive: " + mes + "сервер недоступен");
                parent.popupInfo("Ошибка " + mes);
                parent.hideDialogProgressBar();
            }

            @Override
            public void onError(UniException ee) {
                ctx.toLog(false, "Ошибка keep alive: " + ee.toString() + "сервер недоступен");
                parent.popupInfo(ee.toString());
                parent.hideDialogProgressBar();
            }

            @Override
            public void onSuccess(Object val) {
                R09_13_DeptReportCommon report = (R09_13_DeptReportCommon) val;
                mapperToTable = new R09_13_DeptReportCommonMapperImpl();
                TableStruct tbl[][] = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(tbl, title);
            }
        });
    }

    private void getDept5Report(){
        OwnDateTime temp = new OwnDateTime(dateMS1);
        temp.decMonth();
        dateMS1 = temp.timeInMS();
        new NetCall<R09_13_DeptReportCommon>().call(parent, ctx.getService().createContractServiceReport(sessionToken, dateMS1, dateMS2, 0), new NetBack() {
            @Override
            public void onError(int code, String mes) {
                ctx.toLog(false, "Ошибка keep alive: " + mes + "сервер недоступен");
                parent.popupInfo("Ошибка " + mes);
                parent.hideDialogProgressBar();
            }

            @Override
            public void onError(UniException ee) {
                ctx.toLog(false, "Ошибка keep alive: " + ee.toString() + "сервер недоступен");
                parent.popupInfo(ee.toString());
                parent.hideDialogProgressBar();
            }

            @Override
            public void onSuccess(Object val) {
                R09_13_DeptReportCommon report = (R09_13_DeptReportCommon) val;
                mapperToTable = new R09_13_DeptReportCommonMapperImpl();
                TableStruct tbl[][] = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(tbl, title);
            }
        });
    }

    private void getContractorReport(){

        new NetCall<R14_ContractorReport>().call(parent, ctx.getService().createContractorReport(sessionToken, 0,  true), new NetBack() {
            @Override
            public void onError(int code, String mes) {
                ctx.toLog(false, "Ошибка keep alive: " + mes + "сервер недоступен");
                parent.popupInfo("Ошибка " + mes);
                parent.hideDialogProgressBar();
            }

            @Override
            public void onError(UniException ee) {
                ctx.toLog(false, "Ошибка keep alive: " + ee.toString() + "сервер недоступен");
                parent.popupInfo(ee.toString());
                parent.hideDialogProgressBar();
            }

            @Override
            public void onSuccess(Object val) {
                R14_ContractorReport report = (R14_ContractorReport) val;
                mapperToTable = new R14_ContractorReportMapperImpl();
                TableStruct tbl[][] = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(tbl, title);
            }
        });
    }

    private void getTechnicianPlanReport(){

        new NetCall<R15_TechnicianPlanReport>().call(parent, ctx.getService().createTechnicianPlanReport(sessionToken, dateMS1, 0), new NetBack() {
            @Override
            public void onError(int code, String mes) {
                ctx.toLog(false, "Ошибка keep alive: " + mes + "сервер недоступен");
                parent.popupInfo("Ошибка " + mes);
                parent.hideDialogProgressBar();
            }

            @Override
            public void onError(UniException ee) {
                ctx.toLog(false, "Ошибка keep alive: " + ee.toString() + "сервер недоступен");
                parent.popupInfo(ee.toString());
                parent.hideDialogProgressBar();
            }

            @Override
            public void onSuccess(Object val) {
                R15_TechnicianPlanReport report = (R15_TechnicianPlanReport) val;
                mapperToTable = new R15_TechnicianPlanReportMapperImpl();
                TableStruct tbl[][] = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(tbl, title);
            }
        });
    }

}
