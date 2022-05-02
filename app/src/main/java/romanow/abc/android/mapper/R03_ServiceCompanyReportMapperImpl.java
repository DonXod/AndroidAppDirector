package romanow.abc.android.mapper;

import java.util.ArrayList;

import firefighter.core.reports.R03_ServiceCompanyReport;
import firefighter.core.reports.ServiceCompanyItem;
import firefighter.core.reports.TableCol;
import firefighter.core.reports.TechnicianReportItem;
import romanow.abc.android.TableStruct;
import romanow.abc.android.service.AppData;

public class R03_ServiceCompanyReportMapperImpl implements MapperToTable{

    @Override
    public TableStruct[][] toTable(Object object, AppData ctx) {
        final int WIDTH = 300;
        final int HEIGHT = 130;
        final int HEADHEIGHT = 400;
        final int STYLEHEAD = 42;
        int i;
        int j;


        R03_ServiceCompanyReport report = (R03_ServiceCompanyReport) object;
        ArrayList<ServiceCompanyItem> dataList = report.data;
        ArrayList<TableCol> listHeader = report.createHeader();
        TableStruct[][] tbl = new TableStruct[dataList.size() + 1][listHeader.size()];
        for (i = 0; i< dataList.size() + 1; i++) {
            for (j = 0; j < listHeader.size(); j++) {
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

        i = 1;
        for (ServiceCompanyItem item: dataList) {
            j = 0;

            tbl[i][j++].setName(i==dataList.size() ? "" : ""+(i+1));
            tbl[i][j++].setName(item.company.getTitle());
            tbl[i][j++].setName(Integer.toString(item.contractCount));
            tbl[i][j++].setName(Integer.toString(item.contractSum.getSum()));
            i++;
        }

        return tbl;
    }
}
