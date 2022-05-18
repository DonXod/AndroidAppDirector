package naumov.abc.android.service;

import androidx.fragment.app.FragmentTransaction;

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
import naumov.abc.android.TableStruct;
import naumov.abc.android.fragment.FragmentReport;
import naumov.abc.android.mapper.MapperToTable;
import naumov.abc.android.mapper.R01_TechnicianReportMapperImpl;
import naumov.abc.android.mapper.R02_PaymentReportMapperImpl;
import naumov.abc.android.mapper.R03_ServiceCompanyReportMapperImpl;
import naumov.abc.android.mapper.R04_FacilityReportMapperImpl;
import naumov.abc.android.mapper.R05_08_PaymentReportCommonMapperImpl;
import naumov.abc.android.mapper.R09_13_DeptReportCommonMapperImpl;
import naumov.abc.android.mapper.R14_ContractorReportMapperImpl;
import naumov.abc.android.mapper.R15_TechnicianPlanReportMapperImpl;

public class ReportService {

    private MapperToTable mapperToTable;

    public void getTechnicianReport(MainActivity parent, AppData ctx, String sessionToken, long dateMS1, long dateMS2){

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
                TableStruct[][] tbl = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(parent, tbl, title);
            }
        });
    }

    public void getPaymentReport(MainActivity parent, AppData ctx, String sessionToken, long dateMS1, long dateMS2){

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
                TableStruct[][] tbl = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(parent, tbl, title);
            }
        });
    }

    public void getServiceCompanyReport(MainActivity parent, AppData ctx, String sessionToken, long dateMS1){

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
                TableStruct[][] tbl = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(parent, tbl, title);
            }
        });
    }

    public void getFacilityReport(MainActivity parent, AppData ctx, String sessionToken){

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
                TableStruct[][] tbl = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(parent, tbl, title);
            }
        });
    }

    public void getPayment1Report(MainActivity parent, AppData ctx, String sessionToken, int year){

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
                TableStruct[][] tbl = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(parent, tbl, title);
            }
        });
    }

    public void getPayment2Report(MainActivity parent, AppData ctx, String sessionToken, int year){

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
                TableStruct[][] tbl = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(parent, tbl, title);
            }
        });
    }

    public void getPayment3Report(MainActivity parent, AppData ctx, String sessionToken, int year){

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
                TableStruct[][] tbl = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(parent, tbl, title);
            }
        });
    }

    public void getPayment4Report(MainActivity parent, AppData ctx, String sessionToken, int year){

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
                TableStruct[][] tbl = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(parent, tbl, title);
            }
        });
    }

    public void getDept1Report(MainActivity parent, AppData ctx, String sessionToken, long dateMS1, long dateMS2){
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
                TableStruct[][] tbl = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(parent, tbl, title);
            }
        });
    }

    public void getDept2Report(MainActivity parent, AppData ctx, String sessionToken, long dateMS1, long dateMS2){
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
                TableStruct[][] tbl = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(parent, tbl, title);
            }
        });
    }

    public void getDept3Report(MainActivity parent, AppData ctx, String sessionToken, long dateMS1, long dateMS2){
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
                TableStruct[][] tbl = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(parent, tbl, title);
            }
        });
    }

    public void getDept4Report(MainActivity parent, AppData ctx, String sessionToken, long dateMS1, long dateMS2){
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
                TableStruct[][] tbl = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(parent, tbl, title);
            }
        });
    }

    public void getDept5Report(MainActivity parent, AppData ctx, String sessionToken, long dateMS1, long dateMS2){
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
                TableStruct[][] tbl = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(parent, tbl, title);
            }
        });
    }

    public void getContractorReport(MainActivity parent, AppData ctx, String sessionToken){

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
                TableStruct[][] tbl = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(parent, tbl, title);
            }
        });
    }

    public void getTechnicianPlanReport(MainActivity parent, AppData ctx, String sessionToken, long dateMS1){

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
                TableStruct[][] tbl = mapperToTable.toTable(report, ctx);
                String title = report.getTitle();
                createFragmentReport(parent, tbl, title);
            }
        });
    }

    private void createFragmentReport(MainActivity parent, TableStruct[][] tbl, String title) {
        FragmentReport fragmentReport = new FragmentReport(tbl, title);
        FragmentTransaction fragmentTransaction = parent.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.layoutMain, fragmentReport);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
