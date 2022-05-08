package romanow.abc.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MultiListBoxDialogGraph {
    boolean mark []=null;
    int selected = 0;
    AlertDialog myDlg=null;
    MultiListBoxListener ls=null;
    boolean second=false;
    Activity parent=null;
    public MultiListBoxDialogGraph(Activity activity, String title, ArrayList<String> src, MultiListBoxListener ff, View.OnClickListener clickListener){
        try {

            parent=activity;
            mark = new boolean[src.size()];
            for(int i=0;i<mark.length;i++)
                mark[i]=false;
            myDlg=new AlertDialog.Builder(activity).create();
            myDlg.setCancelable(true);
            myDlg.setTitle(null);
            LinearLayout lrr=(LinearLayout)activity.getLayoutInflater().inflate(R.layout.multi_listbox_graph, null);
            LinearLayout trmain=(LinearLayout)lrr.findViewById(R.id.multi_listbox_panel);
            Button button = (Button) lrr.findViewById(R.id.buttonGenerateReport);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onClick(view);
                    myDlg.cancel();
                }
            });

            trmain.setPadding(5, 5, 5, 5);
            if (title!=null){
                TextView hd=(TextView)lrr.findViewById(R.id.multi_listbox_header);
                hd.setText(title);
                hd.setPadding(25, 25, 5, 5);
                hd.setOnClickListener(new View.OnClickListener(){
                    public void onClick(final View arg0) {
                        myDlg.cancel();
                    }});
            }
            myDlg.setOnCancelListener(new DialogInterface.OnCancelListener(){
                public void onCancel(DialogInterface arg0) {
                }
            });
            ls=ff;
            for (int i=0;i<src.size();i++){
                final int ii=i;
                LinearLayout xx;
                xx=(LinearLayout)activity.getLayoutInflater().inflate(R.layout.listbox_item, null);
                xx.setPadding(5, 5, 5, 5);
                final TextView tt=(TextView)xx.findViewById(R.id.dialog_listbox_name);
                tt.setText(src.get(i));
                int bg=(mark[i] ? R.drawable.background_head_select : R.drawable.background_head);
                tt.setBackgroundResource(bg);
                tt.setOnClickListener(new View.OnClickListener(){
                    public void onClick(final View arg0) {
                        mark[ii] = !mark[ii];
                        if(mark[ii]) {
                            selected++;
                        } else {
                            selected--;
                        }
                        if (selected <=1) {
                            button.setClickable(false);
                            button.setTextColor(Color.RED);
                        } else {
                            button.setClickable(true);
                            button.setTextColor(Color.YELLOW);
                        }
                        int bg=(mark[ii] ? R.drawable.background_head_selectb : R.drawable.background_head);
                        tt.setBackgroundResource(bg);
                        ls.onSelect(mark);
                    }});
                trmain.addView(xx);
            }
            myDlg.setView(lrr);
            myDlg.show();
        } catch(Throwable ee){  }
    }
}
