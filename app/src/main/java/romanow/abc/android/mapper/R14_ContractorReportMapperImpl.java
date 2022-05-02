package romanow.abc.android.mapper;

import java.util.ArrayList;

import firefighter.core.entity.subjectarea.Contractor;
import firefighter.core.entity.subjectarea.Facility;
import firefighter.core.reports.R04_FacilityReport;
import firefighter.core.reports.R14_ContractorReport;
import firefighter.core.reports.ServiceCompanyItem;
import firefighter.core.reports.TableCol;
import firefighter.core.utils.Address;
import romanow.abc.android.TableStruct;
import romanow.abc.android.service.AppData;

public class R14_ContractorReportMapperImpl implements MapperToTable{


    @Override
    public TableStruct[][] toTable(Object object, AppData ctx) {
        final int WIDTH = 300;
        final int HEIGHT = 200;
        final int HEADHEIGHT = 400;
        final int STYLEHEAD = 42;
        int i;
        int j;

        R14_ContractorReport report = (R14_ContractorReport) object;
        ArrayList<Contractor> dataList = report.data;
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
        for (Contractor item: dataList) {
            j = 0;

            tbl[i][j++].setName(Integer.toString(i));
            tbl[i][j++].setName(Long.toString(item.getOid()));
            tbl[i][j++].setName(item.getName());
            tbl[i][j++].setName(item.getINN());
            tbl[i][j++].setName(Long.toString(item.getAddress().getOid()));
            Address zz = item.getAddress().getRef();
            tbl[i][j++].setName(zz==null ? "" : zz.toShortString());
            tbl[i][j++].setName(Long.toString(zz==null ? 0 : zz.getLocation().getOid()));
            tbl[i][j++].setName(zz==null ? "" : zz.getLocation().getTitle());

            i++;
        }

        return tbl;
    }
}
