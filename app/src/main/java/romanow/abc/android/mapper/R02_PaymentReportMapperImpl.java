package romanow.abc.android.mapper;

import java.util.ArrayList;

import firefighter.core.reports.PaymentReportItem;
import firefighter.core.reports.R02_PaymentReport;
import firefighter.core.reports.TableCol;
import firefighter.core.reports.TechnicianReportItem;
import firefighter.core.utils.OwnDateTime;
import romanow.abc.android.TableStruct;
import romanow.abc.android.service.AppData;

public class R02_PaymentReportMapperImpl implements MapperToTable{
    @Override
    public TableStruct[][] toTable(Object object, AppData ctx) {
        final int WIDTH = 300;
        final int HEIGHT = 200;
        final int HEADHEIGHT = 400;
        final int STYLEHEAD = 42;
        int dataCols;
        int i;
        int j;

        R02_PaymentReport report = (R02_PaymentReport) object;
        ArrayList<PaymentReportItem> dataList = report.data;
        ArrayList<TableCol> listHeader = report.createHeader();
        dataCols=report.end.monthDifference(report.begin);
        TableStruct[][] tbl = new TableStruct[dataList.size()+5][listHeader.size() + dataCols];
        for (i = 0; i< dataList.size()+5; i++) {
            for (j = 0; j < listHeader.size() + dataCols; j++) {
                tbl[i][j] = new TableStruct();
                tbl[i][j].setHeight(HEIGHT);
                tbl[i][j].setWidth(WIDTH);
            }
        }

        i = 0;
        j = 0;
        for (TableCol col : listHeader) {
            tbl[i][j].setHeight(HEADHEIGHT);
            tbl[i][j].setWidth(WIDTH);
            tbl[i][j].setStyle(STYLEHEAD);
            tbl[i][j++].setName(col.getName());
        }

        tbl[0][3].setGraph(1);
        tbl[0][3].setIndexName(0);

        OwnDateTime dd = new OwnDateTime(report.begin.timeInMS());
        dd.incMonth();
        OwnDateTime cc = new OwnDateTime(dd.timeInMS());
        for (int k = 0; k < dataCols; k++) {
            tbl[i][j].setHeight(HEADHEIGHT);
            tbl[i][j].setWidth(WIDTH);
            tbl[i][j].setStyle(STYLEHEAD);
            tbl[i][j++].setName(cc.monthToString());
            cc.incMonth();
        }


        i = 1;
        for (PaymentReportItem item: dataList) {
            j = 0;

            tbl[i][j++].setName(Integer.toString(i));
            tbl[i][j++].setName(item.name);
            if (item.sumContract.getSum()!=0) {
                tbl[i][j].setValue(item.sumContract.getSum() / 100L);
                tbl[i][j++].setName(item.sumContract.getSum() / 100 + "." + item.sumContract.getSum() % 100 + "р.");
            } else {
                tbl[i][j++].setName("0.0р.");
            }
            if (item.sumToSend.getSum()!=0) {
                tbl[i][j].setValue(item.sumToSend.getSum() / 100L);
                tbl[i][j++].setName(item.sumToSend.getSum() / 100 + "." + item.sumToSend.getSum() % 100 + "р.");
            } else {
                tbl[i][j++].setName("0.0р.");
            }
            if (item.sumWasPay.getSum()!=0) {
                tbl[i][j].setValue(item.sumWasPay.getSum() / 100L);
                tbl[i][j++].setName(item.sumWasPay.getSum() / 100 + "." + item.sumWasPay.getSum() % 100 + "р.");
            } else {
                tbl[i][j++].setName("0.0р.");
            }
            if (item.dept.getSum()!=0) {
                tbl[i][j].setValue(item.dept.getSum() / 100L);
                tbl[i][j++].setName(item.dept.getSum() / 100 + "." + item.dept.getSum() % 100 + "р.");
            } else {
                tbl[i][j++].setName("0.0р.");
            }
            for(int k = 1; k < dataCols + 1; k++) {
                tbl[i][j].setStyle(item.states[k] == 1 ? 3 :
                        item.states[k] == 2 ? 1 :
                        item.states[k] == 3 ? 2 : 0);
                if (item.pay[k].getSum()!=0) {
                    tbl[i][j++].setName(item.pay[k].getSum() / 100 + "." + item.pay[k].getSum() % 100 +"р.");
                } else {
                    tbl[i][j++].setName("0.0р.");
                }
            }
            i++;
        }

        tbl[dataList.size()+1][1].setName("Цветовые обозначения:");
        tbl[dataList.size()+2][1].setName("оплачено");
        tbl[dataList.size()+2][1].setStyle(2);
        tbl[dataList.size()+3][1].setName("выставлено");
        tbl[dataList.size()+3][1].setStyle(1);
        tbl[dataList.size()+4][1].setName("не выставлено");
        tbl[dataList.size()+4][1].setStyle(3);

        return tbl;
    }
}
