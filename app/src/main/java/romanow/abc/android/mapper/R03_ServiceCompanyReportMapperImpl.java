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
        int i;
        int j;


        R03_ServiceCompanyReport report = (R03_ServiceCompanyReport) object;
        ArrayList<ServiceCompanyItem> dataList = report.data;
        ArrayList<TableCol> listHeader = report.createHeader();
        TableStruct[][] tbl = new TableStruct[dataList.size() + 1][listHeader.size()];
        for (i = 0; i< dataList.size() + 1; i++) {
            for (j = 0; j < listHeader.size(); j++) {
                tbl[i][j] = new TableStruct();
            }
        }

        i = 0;
        j = 0;
        for (TableCol col : listHeader) {
            tbl[i][j].setStyle(STYLEHEAD);
            tbl[i][j++].setName(col.getName());
        }

        //-------------------------------Гистограммы-------------------------
        tbl[0][2].setGraph(1);
        tbl[0][2].setIndexName(0);
        tbl[0][2].setStyle(STYLEHEADGIST);
        tbl[0][2].setDataSize(dataList.size() - 1);
        tbl[0][2].setLabelY("шт.");
        tbl[0][3].setGraph(1);
        tbl[0][3].setIndexName(0);
        tbl[0][3].setStyle(STYLEHEADGIST);
        tbl[0][3].setDataSize(dataList.size() - 1);
        tbl[0][3].setLabelY("р.");
        //----------------------------------------------------------------

        i = 1;
        for (ServiceCompanyItem item: dataList) {
            j = 0;

            tbl[i][j++].setName(i==dataList.size() ? "" : ""+(i));
            tbl[i][j++].setName(item.company.getTitle());
            tbl[i][j].setValue((long) item.contractCount);
            tbl[i][j++].setName(item.contractCount+" шт.");
            tbl[i][j].setValue(item.contractSum.getSum() / 100L);
            tbl[i][j++].setName(item.contractSum.getSum() / 100 + "." + item.contractSum.getSum() % 100 +"р.");
            i++;
        }

        setTblSize(tbl);
        return tbl;
    }
}
