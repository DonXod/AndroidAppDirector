package romanow.abc.android.mapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import firefighter.core.constants.Values;
import firefighter.core.entity.subjectarea.Contractor;
import firefighter.core.entity.subjectarea.Facility;
import firefighter.core.entity.subjectarea.PaymentItem;
import firefighter.core.entity.subjectarea.WorkSettings;
import firefighter.core.reports.CalcMapMonth;
import firefighter.core.reports.CalcMapYear;
import firefighter.core.reports.I_ReportParams;
import firefighter.core.reports.R04_FacilityReport;
import firefighter.core.reports.R05_08_PaymentReportCommon;
import firefighter.core.reports.ServiceCompanyItem;
import firefighter.core.reports.TableCol;
import firefighter.core.utils.MoneySum;
import romanow.abc.android.TableStruct;
import romanow.abc.android.service.AppData;

public class R05_08_PaymentReportCommonMapperImpl implements MapperToTable{

    @Override
    public TableStruct[][] toTable(Object object, AppData ctx) {
        final int WIDTH = 300;
        final int HEIGHT = 200;
        final int HEADHEIGHT = 400;
        final int STYLEHEAD = 42;
        I_ReportParams params = null;
        int i;
        int j;

        R05_08_PaymentReportCommon report = (R05_08_PaymentReportCommon) object;
        ArrayList<TableCol> listHeader = report.createHeader();
        TableStruct[][] tbl = new TableStruct[report.pays.size() + 7][listHeader.size()];
        for (i = 0; i< report.pays.size() + 7; i++) {
            for (j = 0; j < listHeader.size(); j++) {
                tbl[i][j] = new TableStruct();
                tbl[i][j].setHeight(HEIGHT);
                tbl[i][j].setWidth(WIDTH);
            }
        }

        try {
            Field field = report.getClass().getDeclaredField("params");
            field.setAccessible(true);
            params = (I_ReportParams) field.get(report);
        } catch (NoSuchFieldException | IllegalAccessException ignore) {

        }

        i = 0;
        j = 0;
        for (TableCol col : listHeader) {
            tbl[i][j].setHeight(HEADHEIGHT);
            tbl[i][j].setWidth(WIDTH);
            tbl[i][j].setStyle(STYLEHEAD);
            tbl[i][j++].setName(col.getName());
        }
        if (report.reportType == 12) {
            tbl[0][2].setName("Контрагент");
        }
        if (report.reportType == 13) {
            tbl[0][2].setName("Название");
        }
        if (report.reportType == 14 || report.reportType == 15) {
            tbl[0][2].setName("Договоры");
        }

        int sums[] = new int[20];
        for (i = 0; i < 20; i++) {
            sums[i] = 0;
        }
        int commonDepts=0;
        int commonSets=0;
        int commonPlans=0;
        int commonManual=0;

        for (i = 1; i < report.pays.size() + 1; i++) {
            j = 0;

            CalcMapYear item = report.pays.getData().get(i-1);
            CalcMapYear item2 = report.depts.get(item.getId());

            tbl[i][j++].setName(Integer.toString(i));
            tbl[i][j++].setName(item.getTitle());
            if(report.reportType == 12 || report.reportType == 13) {
                tbl[i][j++].setName(item.getData().get(0).getData().get(0).get(0).getByContractor().getRef() == null ? "" : item.getData().get(0).getData().get(0).get(0).getByContractor().getRef().getName());
            } else {
                tbl[i][j++].setName(item.getOwnMoney()+"");
            }
            sums[j] += 0;
            tbl[i][j++].setName("0.0р.");
            sums[j] += item.getData().get(0).getSumNeedTo();
            tbl[i][j++].setName(item.getData().get(0).getSumNeedTo() / 100+"." + item.getData().get(0).getSumNeedTo() % 100 + "р.");
            sums[j] += item.getData().get(0).getSumWasSend();
            tbl[i][j++].setName(item.getData().get(0).getSumWasSend() / 100 + "." + item.getData().get(0).getSumWasSend() % 100 + "р.");
            sums[j] += item.getData().get(0).getSumDone();
            tbl[i][j++].setName(item.getData().get(0).getSumDone() / 100 + "." + item.getData().get(0).getSumDone() % 100 + "р.");
            sums[j] += item.getData().get(0).getSumNeedTo() + item.getData().get(0).getSumWasSend() + item.getData().get(0).getSumDone();
            tbl[i][j++].setName((item.getData().get(0).getSumNeedTo() + item.getData().get(0).getSumWasSend() + item.getData().get(0).getSumDone()) / 100 +
                    "." + (item.getData().get(0).getSumNeedTo() + item.getData().get(0).getSumWasSend() + item.getData().get(0).getSumDone()) % 100 + "р.");
            final int iFin = i;
            final int jFin = j;
                item.getData().get(0).getData().stream().forEach(e -> {
                    long sum = 0L;
                    List<Long> list = e.stream().map(ee -> Long.parseLong(ee.getCurrentPay().getSum()+"")).collect(Collectors.toList());
                    for (Long l: list) {
                        sum += l;
                    }
                    sums[e.get(0).getMaintenances().get(0).getRef().getReglamentMonth() == 12 ? jFin : jFin + e.get(0).getMaintenances().get(0).getRef().getReglamentMonth()] +=
                            sum;
                    tbl[iFin][e.get(0).getMaintenances().get(0).getRef().getReglamentMonth() == 12 ? jFin : jFin + e.get(0).getMaintenances().get(0).getRef().getReglamentMonth()]
                            .setName( sum/100 + "." + sum % 100 + "р.");
                    tbl[iFin][e.get(0).getMaintenances().get(0).getRef().getReglamentMonth() == 12 ? jFin : jFin + e.get(0).getMaintenances().get(0).getRef().getReglamentMonth()]
                            .setStyle(e.get(0).getMaintenances().get(0).getRef().getPaymentState() == 1 ? 3 :
                                    e.get(0).getMaintenances().get(0).getRef().getPaymentState() == 2 ? 1 :
                                    e.get(0).getMaintenances().get(0).getRef().getPaymentState() == 3 ? 2 : 7);
                });
        }

        int sz = report.pays.size()+1;
        tbl[sz][1].setName("Итого");
        for (j = 0; j < 20; j++) {
            if(sums[j]!=0) {
                tbl[sz][j].setName(sums[j] / 100 + "." + sums[j] % 100 + "р.");
            }
        }
        tbl[sz][7].setName("-");
        tbl[sz+1][1].setName("Цветовые обозначения:");
        tbl[sz+2][1].setName("оплачено");
        tbl[sz+3][1].setName("выставлено");
        tbl[sz+4][1].setName("не выставлено");
        tbl[sz+5][1].setName("разные");
        tbl[sz+2][1].setStyle(Values.PIStateColors[Values.PIDone]);
        tbl[sz+3][1].setStyle(Values.PIStateColors[Values.PIWasSended]);
        tbl[sz+4][1].setStyle(Values.PIStateColors[Values.PINeedToPay]);
        tbl[sz+5][1].setStyle(Values.ColorBrown);

        return tbl;
    }
}
