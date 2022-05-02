package romanow.abc.android.mapper;

import java.util.ArrayList;

import firefighter.core.entity.EntityLink;
import firefighter.core.entity.subjectarea.Contract;
import firefighter.core.entity.subjectarea.Facility;
import firefighter.core.entity.subjectarea.FacilityOrder;
import firefighter.core.entity.subjectarea.MaintenanceOrder;
import firefighter.core.reports.R03_ServiceCompanyReport;
import firefighter.core.reports.R04_FacilityReport;
import firefighter.core.reports.ServiceCompanyItem;
import firefighter.core.reports.TableCol;
import firefighter.core.utils.OwnDateTime;
import romanow.abc.android.TableStruct;
import romanow.abc.android.service.AppData;

public class R04_FacilityReportMapperImpl implements MapperToTable{

    @Override
    public TableStruct[][] toTable(Object object, AppData ctx) {
        final int WIDTH = 300;
        final int HEIGHT = 200;
        final int HEADHEIGHT = 400;
        final int STYLEHEAD = 42;
        OwnDateTime today = new OwnDateTime();
        int i;
        int j;

        R04_FacilityReport report = (R04_FacilityReport) object;
        ArrayList<Facility> dataList = report.data;
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
        for (Facility item: dataList) {
            j = 0;

            tbl[i][j++].setName(Integer.toString(i));
            tbl[i][j++].setName(Long.toString(item.getOid()));
            tbl[i][j++].setName(item.getIdentifier());
            tbl[i][j++].setName(item.getName());
            tbl[i][j++].setName(item.getAddress().getTitle());
            tbl[i][j++].setName(item.getGPS().toShortString());
            tbl[i][j++].setName(item.getContractor().getTitle());
            OwnDateTime tt = item.getServiceStartDate();
            if (tt.dateTimeValid()){
                tbl[i][j++].setName(tt.monthToString());
            } else {
                tbl[i][j++].setName("");
            }
            Contract ctr = item.getWorkingContract(today);
            if (ctr!=null){
                tbl[i][j++].setName(ctr.getShortTitle());
                tbl[i][j++].setName(ctr.getCompany().getTitle());
                tbl[i][j++].setName(Integer.toString(ctr.getCostPerYear().getRub()));
            } else {
                tbl[i][j++].setName("");
                tbl[i][j++].setName("");
                tbl[i][j++].setName("");
            }
            tbl[i][j++].setName(item.getTechnician().getTitle());
            String reg="";
            boolean first=true;
            for(EntityLink<FacilityOrder> order : item.getOrder()){
                MaintenanceOrder oo = order.getRef().getOrder().getRef();
                if (oo.isFirstInspection())
                    continue;
                reg += (first ? "": ",")+oo.getName();
                first=false;
            }
            tbl[i][j++].setName(reg);
            tbl[i][j++].setName(item.getAdditionalPhones().toString());
            i++;
        }

        return tbl;
    }
}
