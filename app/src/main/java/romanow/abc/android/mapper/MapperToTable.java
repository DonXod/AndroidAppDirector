package romanow.abc.android.mapper;

import romanow.abc.android.TableStruct;
import romanow.abc.android.service.AppData;

public interface MapperToTable {

    TableStruct[][] toTable(Object object, AppData ctx);
}
