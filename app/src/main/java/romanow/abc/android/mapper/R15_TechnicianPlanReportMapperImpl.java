package romanow.abc.android.mapper;

import java.util.ArrayList;

import firefighter.core.entity.subjectarea.Facility;
import firefighter.core.reports.R04_FacilityReport;
import firefighter.core.reports.R15_TechnicianPlanReport;
import firefighter.core.reports.ServiceCompanyItem;
import firefighter.core.reports.TableCol;
import firefighter.core.reports.TechnicianPlanReportItem;
import romanow.abc.android.TableStruct;
import romanow.abc.android.service.AppData;

public class R15_TechnicianPlanReportMapperImpl implements MapperToTable{

    @Override
    public TableStruct[][] toTable(Object object, AppData ctx) {
        final int WIDTH = 300;
        final int HEIGHT = 130;
        final int HEADHEIGHT = 400;
        final int STYLEHEAD = 42;
        int i;
        int j;

        R15_TechnicianPlanReport report = (R15_TechnicianPlanReport) object;
        ArrayList<TechnicianPlanReportItem> dataList = report.data;
        ArrayList<TableCol> listHeader = report.createHeader();
        TableStruct[][] tbl = new TableStruct[dataList.size() + 2][listHeader.size()];
        for (i = 0; i< dataList.size() + 2; i++) {
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

        int sum[] = new int[7];
        for(i=0;i<7; i++)
            sum[i]=0;

        i = 1;
        for (TechnicianPlanReportItem item: dataList) {
            j = 0;

            tbl[i][j++].setName(Integer.toString(i));
            tbl[i][j++].setName(item.name);
            tbl[i][j++].setName(Integer.toString(item.facilityCount));
            tbl[i][j++].setName(Integer.toString(item.mainFac));
            tbl[i][j++].setName(Integer.toString(item.mainTotal));
            tbl[i][j++].setName(Integer.toString(item.mainDoneFac));
            tbl[i][j++].setName(Integer.toString(item.mainDoneTotal));
            tbl[i][j++].setName(Integer.toString(item.mainDoneBefore));
            tbl[i][j++].setName(Integer.toString(item.mainDoneAfter));
            if (item.mainTotal==0) {
                tbl[i][j++].setName(Integer.toString(0));
            } else {
                tbl[i][j++].setName(Integer.toString(item.mainDoneTotal * 100 / item.mainTotal));
            }
            sum[0]+=item.facilityCount;
            sum[1]+=item.mainFac;
            sum[2]+=item.mainTotal;
            sum[3]+=item.mainDoneFac;
            sum[4]+=item.mainDoneTotal;
            sum[5]+=item.mainDoneBefore;
            sum[6]+=item.mainDoneAfter;

            i++;
        }
        j = 0;
        int sz = dataList.size()+1;
        tbl[sz][j++].setName("");
        tbl[sz][j++].setName("ИТОГО");
        tbl[sz][j++].setName(Integer.toString(sum[0]));
        tbl[sz][j++].setName(Integer.toString(sum[1]));
        tbl[sz][j++].setName(Integer.toString(sum[2]));
        tbl[sz][j++].setName(Integer.toString(sum[3]));
        tbl[sz][j++].setName(Integer.toString(sum[4]));
        tbl[sz][j++].setName(Integer.toString(sum[5]));
        tbl[sz][j++].setName(Integer.toString(sum[6]));
        if (sum[2]==0)
            tbl[sz][j++].setName(Integer.toString(0));
        else
            tbl[sz][j++].setName(Integer.toString(sum[4]*100/sum[2]));
        return tbl;
    }
}
