package naumov.abc.android.mapper;

import java.lang.reflect.Field;
import java.util.ArrayList;

import firefighter.core.Utils;
import firefighter.core.entity.subjectarea.WorkSettings;
import firefighter.core.reports.R01_TechnicianReport;
import firefighter.core.reports.TableCol;
import firefighter.core.reports.TechnicianReportItem;
import naumov.abc.android.TableStruct;
import naumov.abc.android.service.AppData;

public class R01_TechnicianReportMapperImpl implements MapperToTable{

    @Override
    public TableStruct[][] toTable(Object object, AppData ctx) {
        int i;
        int j;
        WorkSettings ws = null;
        R01_TechnicianReport report = (R01_TechnicianReport) object;
        ArrayList<TechnicianReportItem> dataList = report.data;

        ArrayList<TableCol> listHeader = report.createHeader();
        TableStruct[][] tbl = new TableStruct[dataList.size() + 1][listHeader.size()];
        for (i = 0; i< dataList.size() + 1; i++) {
            for (j = 0; j < listHeader.size(); j++) {
                tbl[i][j] = new TableStruct();
            }
        }

        try {
            Field field = report.getClass().getDeclaredField("ws");
            field.setAccessible(true);
            ws = (WorkSettings) field.get(report);
        } catch (NoSuchFieldException | IllegalAccessException ignore) {

        }

        i = 0;
        j = 0;
        for (TableCol col : listHeader) {
            tbl[i][j].setStyle(STYLEHEAD);
            tbl[i][j++].setName(col.getName());
        }

        i = 1;
        for (TechnicianReportItem item: dataList) {
            j = 0;

            int workTime = ws.getMinutesForShift()*item.shiftCount;
            tbl[i][j++].setName(Integer.toString(i));
            tbl[i][j++].setName(item.name);
            tbl[i][j++].setName(Integer.toString(item.shiftCount));
            tbl[i][j++].setName(Utils.timeInMinToString(workTime));
            tbl[i][j++].setName(Integer.toString(item.facilityCount));
            tbl[i][j++].setName(Integer.toString(item.emptyShiftCount));
            tbl[i][j++].setName(Integer.toString(item.mainCount));
            tbl[i][j++].setName(Utils.timeInMinToString(item.totalMainTime));
            tbl[i][j++].setName(workTime!=0 ? ""+item.totalMainTime*100/workTime : "0");
            tbl[i][j++].setName(Utils.timeInMinToString(item.mainTimeInMin));
            tbl[i][j++].setName(item.totalMainTime !=0 ? ""+item.mainTimeInMin*100/workTime : "0");
            tbl[i][j++].setName(Integer.toString(item.mainCountInPlan));
            tbl[i][j++].setName(Integer.toString(item.mainCountDone));
            tbl[i][j++].setName(Integer.toString(item.mainCountFail));
            tbl[i][j++].setName(Integer.toString(item.mainCountProc));
            tbl[i][j++].setName(Integer.toString(item.jobCount));
            tbl[i][j++].setName(Utils.timeInMinToString(item.jobTimeInMin));
            tbl[i][j++].setName(item.mainTimeInMin!=0 ? ""+item.jobTimeInMin*100/item.mainTimeInMin : "0");
            tbl[i][j++].setName(Integer.toString(item.jobCountInPlan));
            tbl[i][j++].setName(Integer.toString(item.jobCountDone));
            tbl[i][j++].setName(Integer.toString(item.jobCountFail));
            tbl[i][j++].setName(Integer.toString(item.jobCountProc));
            tbl[i][j++].setName(Integer.toString(item.voiceTotalCount));
            tbl[i][j++].setName(item.voiceTotalCount==0 ? "" : ""+Utils.timeInMinToString(item.voicePlanSum/item.voiceTotalCount/60));
            tbl[i][j++].setName(item.voiceRealCount==0 ? "" : ""+Utils.timeInMinToString(item.voiceRealSum/item.voiceRealCount/60));
            tbl[i][j++].setName(Integer.toString(item.voiceDoneCount));
            tbl[i][j++].setName(Integer.toString(item.voiceFailCount));
            tbl[i][j++].setName(Integer.toString(item.voiceCallTechnician));
            tbl[i][j++].setName(Integer.toString(item.voiceCallTechnician-item.voiceCallCount));
            tbl[i][j++].setName(item.voiceCallCount==0 ? "" : ""+Utils.timeInSecToString(item.voiceCallSum/item.voiceCallCount));
            tbl[i][j++].setName(Integer.toString(item.voiceOnlyCall));
            tbl[i][j++].setName(Integer.toString(item.wcrSendToContractor));
            tbl[i][j++].setName(Integer.toString(item.wcrOnSubScribe));
            tbl[i][j++].setName(Integer.toString(item.wcrByTechnician));
            i++;
        }

        setTblSize(tbl);
        return tbl;
    }
}
