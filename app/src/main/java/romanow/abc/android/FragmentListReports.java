package romanow.abc.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import firefighter.core.UniException;
import romanow.abc.android.service.AppData;
import romanow.abc.android.service.NetBack;
import romanow.abc.android.service.NetCall;

public class FragmentListReports extends Fragment {

    private final ButtonFactory buttonFactory = new ButtonFactory();
    private LinearLayout listButtons;
    private MainActivity parent;
    private AppData ctx;
    private String sessionToken;
    private Button technicianButtonList;
    private Button paymentButtonList;
    private Button serviceCompanyButtonList;
    private Button facilityButtonList;
    private Button payment1ButtonList;
    private Button payment2ButtonList;
    private Button payment3ButtonList;
    private Button payment4ButtonList;
    private Button dept1ButtonList;
    private Button dept2ButtonList;
    private Button dept3ButtonList;
    private Button dept4ButtonList;
    private Button dept5ButtonList;
    private Button contractorButtonList;
    private Button technicianPlanButtonList;
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
        listButtons = (LinearLayout) view.findViewById(R.id.list_button_report);
        technicianButtonList = buttonFactory.createButton(parent, listButtons, getResources().getString(R.string.reports_list_r01));
        paymentButtonList = buttonFactory.createButton(parent, listButtons, getResources().getString(R.string.reports_list_r02));
        serviceCompanyButtonList = buttonFactory.createButton(parent, listButtons, getResources().getString(R.string.reports_list_r03));
        facilityButtonList = buttonFactory.createButton(parent, listButtons, getResources().getString(R.string.reports_list_r04));
        payment1ButtonList = buttonFactory.createButton(parent, listButtons, getResources().getString(R.string.reports_list_r05));
        payment2ButtonList = buttonFactory.createButton(parent, listButtons, getResources().getString(R.string.reports_list_r06));
        payment3ButtonList = buttonFactory.createButton(parent, listButtons, getResources().getString(R.string.reports_list_r07));
        payment4ButtonList = buttonFactory.createButton(parent, listButtons, getResources().getString(R.string.reports_list_r08));
        dept1ButtonList = buttonFactory.createButton(parent, listButtons, getResources().getString(R.string.reports_list_r09));
        dept2ButtonList = buttonFactory.createButton(parent, listButtons, getResources().getString(R.string.reports_list_r10));
        dept3ButtonList = buttonFactory.createButton(parent, listButtons, getResources().getString(R.string.reports_list_r11));
        dept4ButtonList = buttonFactory.createButton(parent, listButtons, getResources().getString(R.string.reports_list_r12));
        dept5ButtonList = buttonFactory.createButton(parent, listButtons, getResources().getString(R.string.reports_list_r13));
        contractorButtonList = buttonFactory.createButton(parent, listButtons, getResources().getString(R.string.reports_list_r14));
        technicianPlanButtonList = buttonFactory.createButton(parent, listButtons, getResources().getString(R.string.reports_list_r15));

        technicianButtonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentGenerateReport = new FragmentGenerateReport(ReportType.TECHNICIANREPORT);
                createFragmentGenerateReport();
            }
        });
        paymentButtonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentGenerateReport = new FragmentGenerateReport(ReportType.PAYMENTREPORT);
                createFragmentGenerateReport();
            }
        });
        serviceCompanyButtonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentGenerateReport = new FragmentGenerateReport(ReportType.SERVICECOMPANYREPORT);
                createFragmentGenerateReport();
            }
        });
        facilityButtonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentGenerateReport = new FragmentGenerateReport(ReportType.FACILITYREPORT);
                createFragmentGenerateReport();
            }
        });
        payment1ButtonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentGenerateReport = new FragmentGenerateReport(ReportType.PAYMENT1REPORT);
                createFragmentGenerateReport();
            }
        });
        payment2ButtonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentGenerateReport = new FragmentGenerateReport(ReportType.PAYMENT2REPORT);
                createFragmentGenerateReport();
            }
        });
        payment3ButtonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentGenerateReport = new FragmentGenerateReport(ReportType.PAYMENT3REPORT);
                createFragmentGenerateReport();
            }
        });
        payment4ButtonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentGenerateReport = new FragmentGenerateReport(ReportType.PAYMENT4REPORT);
                createFragmentGenerateReport();
            }
        });
        dept1ButtonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentGenerateReport = new FragmentGenerateReport(ReportType.DEPT1REPORT);
                createFragmentGenerateReport();
            }
        });
        dept2ButtonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentGenerateReport = new FragmentGenerateReport(ReportType.DEPT2REPORT);
                createFragmentGenerateReport();
            }
        });
        dept3ButtonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentGenerateReport = new FragmentGenerateReport(ReportType.DEPT3REPORT);
                createFragmentGenerateReport();
            }
        });
        dept4ButtonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentGenerateReport = new FragmentGenerateReport(ReportType.DEPT4REPORT);
                createFragmentGenerateReport();
            }
        });
        dept5ButtonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentGenerateReport = new FragmentGenerateReport(ReportType.DEPT5REPORT);
                createFragmentGenerateReport();
            }
        });
        contractorButtonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentGenerateReport = new FragmentGenerateReport(ReportType.CONTRACTORREPORT);
                createFragmentGenerateReport();
            }
        });
        technicianPlanButtonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentGenerateReport = new FragmentGenerateReport(ReportType.TECHNICIANPLANREPORT);
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
