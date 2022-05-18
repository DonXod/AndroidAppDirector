package naumov.abc.android;


import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import firefighter.core.constants.Values;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import firefighter.core.API.RestAPIFace;
import firefighter.core.UniException;
//import firefighter.core.constants.ValuesBase;
import firefighter.core.entity.baseentityes.JInt;

import naumov.abc.android.dialog.ListBoxDialog;
import naumov.abc.android.dialog.ProgressBarDialog;
import naumov.abc.android.fragment.EmptyFragment;
import naumov.abc.android.fragment.FragmentListReport;
import naumov.abc.android.service.AppData;
import naumov.abc.android.service.BaseActivity;
import naumov.abc.android.service.NetBack;
import naumov.abc.android.service.NetCall;

public class MainActivity extends BaseActivity {     //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private Handler event = new Handler();
    public volatile boolean shutDown = false;
    private AppData ctx;
    //-------------- Постоянные параметры snn-core ---------------------------------------
    private final int MiddleColor = 0x0000FF00;
    private final int DispColor = 0x000000FF;
    private final int GraphBackColor = 0x00A0C0C0;
    final public static int DefaultTextColor=0x00035073;
    final public static String archiveFile = "LEP500Archive.json";
    final public static double ViewProcHigh = 0.6;
    //----------------------------------------------------------------------------
    private LinearLayout log;
    private ScrollView scroll;
    private final int CHOOSE_RESULT = 100;
    private final int CHOOSE_RESULT_COPY = 101;
    public final int REQUEST_ENABLE_BT = 102;
    public final int REQUEST_ENABLE_GPS = 103;
    public final int REQUEST_ENABLE_READ = 104;
    public final int REQUEST_ENABLE_WRITE = 105;
    public final int REQUEST_ENABLE_PHONE = 106;
    public final int REQUEST_ENABLE_AUDIO = 107;
    private ImageView MenuButton;
    private ImageView NETState;
    private FragmentListReport fragmentListReport;
    private EmptyFragment emptyFragment;
    private FragmentTransaction fragmentTransaction;
    private ProgressBarDialog progressBarDialog;
    private Button headerRenderMenu;

    //------------------------------------------------------------------------------------------------------
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            NETState.setImageResource(AppData.CNetRes[AppData.ctx().cState()]);
            }
        };
    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(AppData.Event_CState);
        this.registerReceiver(receiver, filter);
        }
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
        }
    //----------------------------------------------------------------------------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ENABLE_GPS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else
                errorMes("Включите разрешение геолокации");
            if (testPermission()) onAllPermissionsEnabled();
        }
        if (requestCode == REQUEST_ENABLE_READ) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else errorMes("Включите разрешение работы с памятью");
            if (testPermission()) onAllPermissionsEnabled();
        }
        if (requestCode == REQUEST_ENABLE_WRITE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else errorMes("Включите разрешение работы с памятью");
            if (testPermission()) onAllPermissionsEnabled();
        }
        if (requestCode == REQUEST_ENABLE_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else errorMes("Включите разрешение работы с микрофоном");
            if (testPermission()) onAllPermissionsEnabled();
        }
        if (requestCode == REQUEST_ENABLE_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else
                errorMes("Включите разрешение работы с телефоном");
            if (testPermission()) onAllPermissionsEnabled();
        }
    }
    //----------------------------------------------------------------------------------------------
    private boolean testPermission(){
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_ENABLE_READ);
            return false;
            }
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_ENABLE_WRITE);
            return false;
            }
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ENABLE_GPS);
            return false;
            }
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_ENABLE_AUDIO);
            return false;
            }
        return true;
    }

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = AppData.ctx();
        try {
            Values.init();                  // Статические данные
            ctx.setContext(getApplicationContext());
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setContentView(R.layout.activity_main);
            MenuButton = (ImageView) findViewById(R.id.headerMenu);
            log = (LinearLayout) findViewById(R.id.log);
            scroll = (ScrollView) findViewById(R.id.scroll);
            NETState = (ImageView) findViewById(R.id.headerNet);
            headerRenderMenu = (Button) findViewById(R.id.headerRenderMenu);
            headerRenderMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.layoutMain, emptyFragment);
                    fragmentTransaction.commit();
                }
            });
            fragmentListReport = new FragmentListReport();
            emptyFragment = new EmptyFragment();
            progressBarDialog = new ProgressBarDialog();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.layoutMain, emptyFragment);
            fragmentTransaction.commit();
            if (testPermission()){
                onAllPermissionsEnabled();
                }
            } catch (Exception ee) {
                errorMes(createFatalMessage(ee, 10));
                }
        }
    private void onAllPermissionsEnabled(){
        try{
            ctx.fileService().loadContext();
            ctx.cState(AppData.CStateGray);
            createMenuList();
            MenuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createMenuList();
                    menuDialog = new ListBoxDialog(MainActivity.this, createMenuTitles(), "Меню", new I_ListBoxListener() {
                        @Override
                        public void onSelect(int index) {
                            procMenuItem(index);
                            menuDialog = null;
                            }
                        @Override
                        public void onLongSelect(int index) {
                            menuDialog = null;
                            }
                        @Override
                        public void onCancel() {
                            menuDialog = null;
                            }
                        });
                    menuDialog.create();
                }
            });

            String fatalMessage = ctx.loginSettings().getFatalMessage();
            if (fatalMessage.length()!=0){
                addToLog(false,fatalMessage,14,0x00A00000);
                ctx.loginSettings().setFatalMessage("");
                saveContext();
                }
            String title = "Лента истории приложения";
            addToLog(false, title, 22, 0);
            addToLog(false, "Приложение директора для визуализации отчётов компании");
            } catch (Exception ee) {
                errorMes(createFatalMessage(ee, 10));
                }
        }

    public void clearLog() {
        log.removeAllViews();
        }

    public void popupAndLog(String ss) {
        addToLog(ss);
        popupInfo(ss);
        }

    public void popupAndLog(String ss,int textSize, int color) {
        addToLog(false,ss,textSize,color);
        popupInfo(ss);
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shutDown = true;
        saveContext();
        AppData.ctx().stopApplication();
        }

    public void scrollDown() {
        scroll.post(new Runnable() {
            @Override
            public void run() {
                scroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    public void errorMes(int emoCode,String text){
        addToLog(false,(emoCode==0 ? "" : (new String(Character.toChars(emoCode)))+" ")+text,14,0x00FF0000);
        }
    public void errorMes(String text){
        errorMes(EmoErr,text);
    }
    public void addToLog(String ss) {
        addToLog(false, ss, 0);
        }
    public void addToLog(boolean fullInfoMes, String ss) {
        addToLog(fullInfoMes, ss, 0);
    }
    @Override
    public void addToLog(String ss, int textSize) {
        addToLog(false, ss, textSize);
    }
    @Override
    public void addToLogHide(String ss) {
        addToLog(ss);
        }
    public void addToLog(boolean fullInfoMes, final String ss, final int textSize) {
        addToLog(fullInfoMes, ss, textSize, 0);
        }
    public void addToLog(boolean fullInfoMes, final String ss, final int textSize, final int textColor) {
        addToLog(fullInfoMes, ss, textSize, textColor, -1);
        }
    public void addToLog(boolean fullInfoMes, final String ss, final int textSize, final int textColor, final int imgRes) {
        if (fullInfoMes && !ctx.loginSettings().isFullInfo())
            return;
        guiCall(new Runnable() {
            @Override
            public void run() {
                LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.log, null);
                if (imgRes != -1) {
                    ImageView imageView = new ImageView(MainActivity.this);
                    imageView.setImageResource(imgRes);
                    layout.addView(imageView);
                }
                TextView txt = new TextView(MainActivity.this);
                txt.setText(ss);
                int tColor = textColor==0 ? DefaultTextColor : textColor;
                txt.setTextColor(tColor | 0xFF000000);
                if (textSize != 0)
                    txt.setTextSize(textSize);
                layout.addView(txt);
                log.addView(layout);
                scrollDown();
            }
        });
    }

    public LinearLayout addToLogButton(String ss, boolean jetBrain,View.OnClickListener listener, View.OnLongClickListener listenerLong) {
        LinearLayout button = (LinearLayout) getLayoutInflater().inflate(R.layout.log_item, null);
        Button bb = (Button) button.findViewById(R.id.ok_button);
        bb.setText(ss);
        bb.setTextSize(greatTextSize);
        if (jetBrain)
            bb.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.jetbrainsmonolight));
        if (listener != null)
            bb.setOnClickListener(listener);
        if (listenerLong != null)
            bb.setOnLongClickListener(listenerLong);
        log.addView(button);
        scrollDown();
        return button;
        }

    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    //------------------------------------------------------------------------
    private ArrayList<MenuItemAction> menuList = new ArrayList<>();

    private String[] createMenuTitles() {
        String out[] = new String[menuList.size()];
        for (int i = 0; i < out.length; i++)
            out[i] = menuList.get(i).title;
        return out;
    }

    public void procMenuItem(int index) {
        menuList.get(index).onSelect();
    }

    public void createMenuList() {
        menuList.clear();
        if (isAllEnabled()){
            menuList.add(new MenuItemAction("Связь с сервером") {
                @Override
                public void onSelect() {
                    new LoginSettingsMenu(MainActivity.this);
                }
                });
            }
        menuList.add(new MenuItemAction("Просмотр отчётов") {
            @Override
            public void onSelect() {
                if(!ctx.isRegisteredOnServer()){
                    popupInfo("Вы не подключены к серверу");
                    return;
                }
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.layoutMain, fragmentListReport);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        menuList.add(new MenuItemAction("Очистить ленту") {
            @Override
            public void onSelect() {
                log.removeAllViews();
                String title = "Лента истории приложения";
                addToLog(false, title, 22, 0);
                }
            });
        menuList.add(new MenuItemAction("Настройки") {
            @Override
            public void onSelect() {
                new SettingsMenu(MainActivity.this);
            }
            });

        menuList.add(new MenuItemAction("Выход") {
            @Override
            public void onSelect() {
                finish();
            }
        }   );
    }


    //-----------------------------------------------------------------------------------------------
    Retrofit retrofit = null;
    private Runnable httpKeepAlive = new Runnable() {
        @Override
        public void run() {
            if (AppData.ctx().cState()== AppData.CStateGray)
                return;
            String token = ctx.loginSettings().getSessionToken();
            System.out.println(token);
            new NetCall<JInt>().call(MainActivity.this,ctx.getService().keepalive(token), new NetBack() {
                @Override
                public void onError(int code, String mes) {
                    ctx.toLog(false,"Ошибка keep alive: "+mes+". Сервер недоступен");
                    sessionOff();
                    }
                @Override
                public void onError(UniException ee) {
                    ctx.toLog(false,"Ошибка keep alive: "+ee.toString()+". Сервер недоступен");
                    sessionOff();
                    }
                @Override
                public void onSuccess(Object val) {
                }
            });
            if (!shutDown && AppData.ctx().isApplicationOn())
                setDelay(AppData.CKeepALiveTime, httpKeepAlive);
        }
    };

    public void retrofitConnect() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(AppData.HTTPTimeOut, TimeUnit.SECONDS)
                .connectTimeout(AppData.HTTPTimeOut, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://" +ctx.loginSettings().getDataSetverIP() + ":" + ctx.loginSettings().getDataServerPort())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        ctx.service(retrofit.create(RestAPIFace.class));
        }
    public void  sessionOn(){
        httpKeepAlive.run();                // Сразу и потом по часам
        ctx.cState(AppData.CStateGreen);
        }
    public void sessionOff() {
        cancelDelay(httpKeepAlive);
        ctx.cState(AppData.CStateGray);
        }
    public void setDelay(int sec, Runnable code) {          // С возвратом в GUI
        event.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(code);
                }
            }, sec * 1000);
        }
    public void cancelDelay(Runnable code) {
        event.removeCallbacks(code);
        }
    public boolean isAllEnabled(){
        return true;
        }

    @Override
    public void notify(String s) {

    }

    @Override
    public boolean isFinish() {
        return false;
    }

    @Override
    public void onClose() {

    }

    public void showDialogProgressBar() {
        progressBarDialog.show(getSupportFragmentManager(), "progressBarDialog");
    }

    public void hideDialogProgressBar() {
        progressBarDialog.dismiss();
    }
}
