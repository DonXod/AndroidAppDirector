package romanow.abc.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import firefighter.core.UniException;
import romanow.abc.android.service.AppData;
import romanow.abc.android.service.NetBack;
import romanow.abc.android.service.NetCall;

public class FragmentListReports extends Fragment {

    private MainActivity parent;
    private AppData ctx;
    private String sessionToken;
    private Button techniciansButtonList;
    private FragmentTransaction fragmentTransaction;
    private FragmentGenerateReport fragmentGenerateReport;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_reports, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parent = (MainActivity) this.getActivity();
        ctx = AppData.ctx();
        sessionToken = ctx.loginSettings().getSessionToken();
        techniciansButtonList = (Button) view.findViewById(R.id.techniciansButtonList);
        techniciansButtonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentGenerateReport = new FragmentGenerateReport(ReportType.TECHNICIANREPORT);
                createFragmentGenerateReport();
            }
        });
    }

    private void createFragmentGenerateReport() {
        fragmentTransaction = parent.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.layoutMain, fragmentGenerateReport);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
