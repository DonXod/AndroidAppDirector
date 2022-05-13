package naumov.abc.android.mapper;

import java.util.ArrayList;

import firefighter.core.entity.subjectarea.Contractor;
import firefighter.core.reports.R14_ContractorReport;
import firefighter.core.reports.TableCol;
import firefighter.core.utils.Address;
import naumov.abc.android.TableStruct;
import naumov.abc.android.service.AppData;

public class R14_ContractorReportMapperImpl implements MapperToTable{


    @Override
    public TableStruct[][] toTable(Object object, AppData ctx) {
        int i;
        int j;

        R14_ContractorReport report = (R14_ContractorReport) object;
        ArrayList<Contractor> dataList = report.data;
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

        setTblSize(tbl);
        return tbl;
    }
}
