package romanow.abc.android.mapper;

import java.lang.reflect.Field;
import java.util.ArrayList;

import firefighter.core.constants.Values;
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
        final int HEIGHT = 130;
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

        int sums[] = new int[17];
        for (i = 0; i < 17; i++) {
            sums[i] = 0;
        }
        int commonDepts=0;
        int commonSets=0;
        int commonPlans=0;
        int commonManual=0;

        for (i = 1; i < report.pays.size() + 1; i++, j = 0) {
            CalcMapYear item = report.pays.getData().get(i);
            CalcMapYear item2 = report.depts.get(item.getId());

            tbl[i][j++].setName(Integer.toString(i));
            tbl[i][j++].setName(item.getEntity().getTitle());
            if (params!=null) {
                Object oo = params.getName2(item);
                if (oo instanceof String)
                    tbl[i][j++].setName((String)oo );
                else
                    tbl[i][j++].setName(Integer.toString(((MoneySum)oo).getSum()));
            }
            if (item2!=null) {
                CalcMapMonth hh = item2.get(report.year-1);
                if (hh!=null){
                    tbl[i][j++].setName(Integer.toString(hh.getSumWasSend()));
                    sums[0]+=hh.getSumWasSend();
                } else {
                    tbl[i][j++].setName("");
                }
            } else {
                tbl[i][j++].setName("");
            }
            int val = item.get(report.year).getSumNeedTo();
            MoneySum vv = new MoneySum(item.get(report.year).getSumNeedTo());
            report.sumContract.add(vv);
            if (vv.getSum()!=0) {
                tbl[i][j++].setName(Integer.toString(vv.getSum()));
            } else {
                tbl[i][j++].setName("");
            }
            sums[1]+=vv.getSum();
            MoneySum vv2 = new MoneySum(item.get(report.year).getSumWasSend());
            report.sumContract.add(vv2);
            report.sumToSend.add(vv2);
            if (vv2.getSum()!=0) {
                tbl[i][j++].setName(Integer.toString(vv2.getSum()));
            } else {
                tbl[i][j++].setName("");
            }
            sums[2]+=vv2.getSum();
            MoneySum vv3 = new MoneySum(item.get(report.year).getSumDone());
            report.sumContract.add(vv3);
            report.sumWasPay.add(vv3);
            if (vv3.getSum()!=0) {
                tbl[i][j++].setName(Integer.toString(vv3.getSum()));
            } else {
                tbl[i][j++].setName("");
            }
            sums[3]+=vv3.getSum();
            vv.add(vv2);
            vv.add(vv3);
            if (vv.getSum()!=0) {
                tbl[i][j++].setName(Integer.toString(vv.getSum()));
            } else {
                tbl[i][j++].setName("");
            }
            sums[4]+=vv.getSum();
            CalcMapMonth zz = item.get(report.year);

            for(int k=0;k<12;k++){         // Цвет по состоянию
                ArrayList<PaymentItem> list =zz.get(k+1);
                if (list==null || list.size()==0)
                    continue;
                PaymentItem pp = list.get(0);
                int sum = 0;
                int state=pp.getPaymentState();
                boolean mix=false;              // Смешанные состояния
                boolean manual=false;
                for(PaymentItem dd : list){
                    int ss = dd.getCurrentPay().getSum();
                    sum += ss;
                    sums[5+k]+=ss;
                    if (dd.getPaymentState()!=state)
                        mix=true;
                    int dstate = dd.getPaymentState();
                    if (dstate== Values.PIDone)
                        commonDepts++;
                    if (dstate==Values.PINeedToPay)
                        commonPlans++;
                    if (dstate==Values.PIWasSended)
                        commonSets++;
                    if (dd.isManual()){
                        commonManual++;
                        manual=true;
                    }

                }
                int size = list.size();
                if (size!=0){
                    tbl[i][j].setStyle(mix ? Values.ColorBrown : Values.PIStateColors[pp.getPaymentState()]);
                    tbl[i][j].setName(Integer.toString(sum));
                }
            }
            i++;
        }

        int sz = report.pays.size()+1;
        tbl[sz][1].setName("Итого");
        for (j = 0; j < 17; j++) {
            if(sums[j]!=0)
                tbl[sz + 1][j].setName(Integer.toString(sums[j]));
        }
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
