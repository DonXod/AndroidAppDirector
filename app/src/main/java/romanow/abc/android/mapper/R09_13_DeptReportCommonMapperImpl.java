package romanow.abc.android.mapper;

import java.util.ArrayList;

import firefighter.core.entity.subjectarea.Contractor;
import firefighter.core.entity.subjectarea.Facility;
import firefighter.core.reports.R04_FacilityReport;
import firefighter.core.reports.R09_13_DeptReportCommon;
import firefighter.core.reports.SumMapEntity;
import firefighter.core.reports.SumMapMonth;
import firefighter.core.reports.TableCol;
import firefighter.core.utils.OwnDateTime;
import romanow.abc.android.TableStruct;
import romanow.abc.android.service.AppData;

public class R09_13_DeptReportCommonMapperImpl implements MapperToTable{

    @Override
    public TableStruct[][] toTable(Object object, AppData ctx) {
        final int WIDTH = 300;
        final int HEIGHT = 130;
        final int HEADHEIGHT = 400;
        final int STYLEHEAD = 42;
        int i;
        int j;

        R09_13_DeptReportCommon report = (R09_13_DeptReportCommon) object;
        SumMapEntity data = report.data;
        ArrayList<TableCol> listHeader = report.createHeader();
        TableStruct[][] tbl = new TableStruct[data.size() + 2][listHeader.size()];
        for (i = 0; i< data.size() + 2; i++) {
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

        int sums[] = new int[report.nMonth];
        for (i = 0; i < report.nMonth; i++) {
            sums[i] = 0;
        }

        for (i = 1; i < data.size(); i++) {
            j = 0;
            SumMapMonth item = data.getData().get(i-1);
            tbl[i][j++].setName(Integer.toString(i));
            tbl[i][j++].setName(item.getEntity().getTitle());
            OwnDateTime xx = new OwnDateTime(report.firstMonth.timeInMS());
            for (int k = 0; k < report.nMonth; k++) {
                int sum = item.getSum(xx.year(), xx.month());
                sums[k] += sum;
                if (sum!=0) {
                    tbl[i][j++].setName(Integer.toString(sum));
                } else {
                    j++;
                }
                xx.incMonth();
            }
        }

        int sz = data.size()+1;
        tbl[sz][1].setName("Итого");
        for (j = 0; j < report.nMonth; j++) {
            if (sums[j]!=0)
                tbl[sz][j+2].setName(Integer.toString(sums[j]));
        }

        return tbl;
    }
}
