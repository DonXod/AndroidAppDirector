package romanow.abc.android.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import firefighter.core.entity.subjectarea.Contractor;
import firefighter.core.entity.subjectarea.Facility;
import firefighter.core.reports.R04_FacilityReport;
import firefighter.core.reports.R09_13_DeptReportCommon;
import firefighter.core.reports.SumMapEntity;
import firefighter.core.reports.SumMapMonth;
import firefighter.core.reports.SumMonthValue;
import firefighter.core.reports.TableCol;
import firefighter.core.utils.OwnDateTime;
import romanow.abc.android.TableStruct;
import romanow.abc.android.service.AppData;

public class R09_13_DeptReportCommonMapperImpl implements MapperToTable{

    @Override
    public TableStruct[][] toTable(Object object, AppData ctx) {
        int i;
        int j;

        R09_13_DeptReportCommon report = (R09_13_DeptReportCommon) object;
        SumMapEntity data = report.data;
        OwnDateTime dd = new OwnDateTime(report.firstMonth.timeInMS());
        dd.incMonth();
        report.firstMonth = dd;
        ArrayList<TableCol> listHeader = report.createHeader();
        TableStruct[][] tbl = new TableStruct[data.size() + 2][listHeader.size() - 1];
        for (i = 0; i< data.size() + 2; i++) {
            for (j = 0; j < listHeader.size() - 1; j++) {
                tbl[i][j] = new TableStruct();
            }
        }

        i = 0;
        j = 0;
        for (int k = 0; k < listHeader.size() - 1; k++) {
            TableCol col = listHeader.get(k);
            tbl[i][j].setStyle(STYLEHEAD);
            if (col.getName() == null) {
                tbl[i][j++].setName("");
            } else {
                tbl[i][j++].setName(col.getName());
            }
        }

        int sums[] = new int[report.nMonth - 1];
        for (i = 0; i < report.nMonth - 1; i++) {
            sums[i] = 0;
        }

        for (i = 1; i < data.size() + 1; i++) {
            j = 0;

            SumMapMonth item = data.getData().get(i-1);
            tbl[i][j++].setName(Integer.toString(i));
            if (item.getTitle() == null) {
                tbl[i][j++].setName("");
            } else {
                tbl[i][j++].setName(item.getTitle());
            }
            OwnDateTime xx = new OwnDateTime(report.firstMonth.timeInMS());
            for (int k = 0; k < report.nMonth - 1; k++) {
                if (item.getData().stream().filter(e -> e.year == xx.year() && e.month == xx.month()).count() == 0 ) {
                    tbl[i][j++].setName("0.0р.");
                } else {
                    SumMonthValue sumMonthValue = item.getData().stream().filter(e -> e.year == xx.year() && e.month == xx.month()).collect(Collectors.toList()).get(0);
                    tbl[i][j++].setName(sumMonthValue.getSum() / 100 + "." + sumMonthValue.getSum() % 100 + "р.");
                    sums[k] += sumMonthValue.getSum();
                }
                xx.incMonth();
            }
        }

        int sz = data.size()+1;
        tbl[sz][1].setName("Итого");
        for (j = 0; j < report.nMonth - 1; j++) {
            if (sums[j]!=0) {
                tbl[sz][j + 2].setName(sums[j] / 100 + "." + sums[j] % 100 + "р.");
            } else {
                tbl[sz][j + 2].setName("0.0р.");
            }
        }

        setTblSize(tbl);
        return tbl;
    }
}
