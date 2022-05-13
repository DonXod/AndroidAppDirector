package naumov.abc.android.mapper;

import naumov.abc.android.TableStruct;
import naumov.abc.android.service.AppData;

public interface MapperToTable {

    int STYLEHEAD = 42;
    int STYLEHEADGIST = 43;
    int STYLEHEADPIE = 44;
    int sizeHeightLine = 90;
    int sizeWidthSymbolLine = 26;
    int maxInLine = 15;
    int sizeAddHeight = 60;

    TableStruct[][] toTable(Object object, AppData ctx);

    default TableStruct[][] setTblSize(TableStruct[][] tbl) {
        int maxWidthString[] = new int[tbl[0].length];
        int maxHeightString[] = new int[tbl.length];

        for (int i = 0; i < tbl[0].length; i++) {
            for (TableStruct[] tableStructs : tbl) {
                if(maxWidthString[i] < tableStructs[i].getName().length()) {
                    maxWidthString[i] = tableStructs[i].getName().length();
                }
            }
            for (TableStruct[] tableStructs : tbl) {
                if(maxWidthString[i] > maxInLine) {
                    tableStructs[i].setWidth(sizeWidthSymbolLine * maxInLine);
                } else {
                    tableStructs[i].setWidth(sizeWidthSymbolLine * maxWidthString[i]);
                }
            }
        }

        for (int i = 0; i < tbl.length; i++) {
            for (TableStruct tableStruct : tbl[i]) {
                if(maxHeightString[i] < (tableStruct.getName().length() / maxInLine + 1)) {
                    maxHeightString[i] = tableStruct.getName().length() / maxInLine + 1;
                }
            }

            for (TableStruct tableStruct : tbl[i]) {
                if(maxHeightString[i] == 1) {
                    tableStruct.setHeight(sizeHeightLine * maxHeightString[i]);
                } else {
                    tableStruct.setHeight(sizeHeightLine + (maxHeightString[i] - 1) * sizeAddHeight);
                }

            }
        }
        return tbl;
    }
}
