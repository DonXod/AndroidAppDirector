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
        final int HEIGHT = 130;
        final int HEADHEIGHT = 400;
        int dataCols;
        int i;
        int j;

        R02_PaymentReport report = (R02_PaymentReport) object;
        ArrayList<PaymentReportItem> dataList = report.data;
        ArrayList<TableCol> listHeader = report.createHeader();
        dataCols=report.end.monthDifference(report.begin)+1;
        TableStruct[][] tbl = new TableStruct[dataList.size()+5][listHeader.size() + dataCols];
        for (i = 0; i< dataList.size()+5; i++) {
            for (j = 0; j < listHeader.size() + dataCols; j++) {
                tbl[i][j] = new TableStruct();
            }
        }

        i = 0;
        j = 0;
        for (TableCol col : listHeader) {
            tbl[i][j].setHeight(HEADHEIGHT);
            tbl[i][j].setWidth(WIDTH);
            tbl[i][j].setStyle(5);
            tbl[i][j++].setName(col.getName());
        }
        OwnDateTime cc = new OwnDateTime(report.begin.timeInMS());
        for (int k = 0; k < dataCols; k++) {
            tbl[i][j].setHeight(HEADHEIGHT);
            tbl[i][j].setWidth(WIDTH);
            tbl[i][j].setStyle(5);
            tbl[i][j++].setName(cc.monthToString());
            cc.incMonth();
        }

        i = 1;
        for (PaymentReportItem item: dataList) {
            j = 0;

            tbl[i][j++].setName(Integer.toString(i));
            tbl[i][j++].setName(item.name);
            if (item.sumContract.getSum()!=0) {
                tbl[i][j++].setName(Integer.toString(item.sumContract.getSum()));
            }
            if (item.sumToSend.getSum()!=0) {
                tbl[i][j++].setName(Integer.toString(item.sumToSend.getSum()));
            }
            if (item.sumWasPay.getSum()!=0) {
                tbl[i][j++].setName(Integer.toString(item.sumWasPay.getSum()));
            }
            if (item.dept.getSum()!=0) {
                tbl[i][j++].setName(Integer.toString(item.dept.getSum()));
            }
            for(int k = 0; j < dataCols; k++) {
                tbl[i][j].setStyle(item.states[k]);
                if (item.pay[k].getSum()!=0) {
                    tbl[i][j++].setName(Integer.toString(item.pay[j].getSum()));
                }
            }

            for (j = 0; j < listHeader.size() + dataCols; j++) {
                tbl[i][j].setHeight(HEIGHT);
                tbl[i][j].setWidth(WIDTH);
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
        for (i = dataList.size()+1; i < dataList.size()+5; i++) {
            for (j = 0; j < listHeader.size() + dataCols; j++) {
                tbl[i][j].setHeight(HEIGHT);
                tbl[i][j].setWidth(WIDTH);
            }
        }

        return tbl;
    }
}
