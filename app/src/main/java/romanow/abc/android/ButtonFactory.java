package romanow.abc.android;

import android.app.Activity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ButtonFactory {
    public Button createButton (Activity parent, LinearLayout trmain, String text) {
        LinearLayout xx=(LinearLayout)parent.getLayoutInflater().inflate(R.layout.button_list_report, null);
        Button button = (Button) xx.findViewById(R.id.button_report);
        button.setText(text);
        trmain.addView(xx);
        return button;
    }
}
