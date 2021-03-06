package naumov.abc.android;

import android.widget.LinearLayout;

import naumov.abc.android.service.AppData;

public class SettingsMenu extends SettingsMenuBase {
    public SettingsMenu(MainActivity base0){
        super(base0);
        }
    @Override
    public void settingsSave() {
        base.saveContext();
        }

    @Override
    public void createDialog(LinearLayout trmain){
        try {
            LoginSettings set = AppData.ctx().loginSettings();
            LinearLayout layout = createItem("Mail ", ""+set.getMailToSend(), true,true,new I_EventListener(){
                @Override
                public void onEvent(String ss) {
                    set.setMailToSend(ss);
                    settingsChanged();
                    }
                });
            trmain.addView(layout);
            layout = createItem("Отладка", set.isFullInfo() ? "1" : "0" , new I_EventListener(){
                @Override
                public void onEvent(String ss) {
                    try {
                        set.setFullInfo(Integer.parseInt(ss)!=0);
                        settingsChanged();
                    } catch (Exception ee){
                        base.popupInfo("Формат числа");}
                        }
                });
            trmain.addView(layout);

            //---------------------------------------------------------------------------------------
        } catch(Exception ee){
            int a=1;
            }
        catch(Error ee){
            int u=0;
        }
    }
}

