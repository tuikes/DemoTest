package com.tuikes.demotest.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;


import com.tuikes.demotest.R;
import com.tuikes.demotest.app.IApplication;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {
    private final static String TAG = "CommonUtils";
    private static int m_screenDPI = -1;
    private static int m_simCardState = -1;
    private static String m_sDeviceVersion = "";
    private static String m_sIMEI = "";
    private static int m_iSdkVersion = 0;
    private static float m_iDeviceVersion = 0;
    private static String m_strPhoneStyle = "";// 手机型号
    private static DisplayMetrics m_displayMetrics;
    private static int m_screenWidth = -1;
    private static int m_screenHeight = -1;
    private static String m_phoneVersion;// 手机版本号

    /**
     * 获得进程名字
     */
    public static String getUIPName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }

    // 获取版本号
    public static String getStringDeviceVersion() {
        m_sDeviceVersion = android.os.Build.VERSION.RELEASE;

        return m_sDeviceVersion;
    }

    // 获得数字版本号
    public static float getIntDeviceVersion() {
        m_sDeviceVersion = android.os.Build.VERSION.RELEASE;
        if (m_sDeviceVersion != null && m_sDeviceVersion.length() >= 3) {
            String spiltString = m_sDeviceVersion.substring(0, 3);
            Pattern pattern = Pattern.compile("^\\d+([\\.]?\\d+)?$");
            Matcher matcher = pattern.matcher(spiltString);
            boolean result = matcher.matches();
            if (result == true) {
                m_iDeviceVersion = Float.valueOf(spiltString);
            } else {
                m_iDeviceVersion = 0;
            }
        }
        return m_iDeviceVersion;

    }

    public static String getPhoneVersion() {
        m_phoneVersion = android.os.Build.VERSION.CODENAME;
        return m_phoneVersion;
    }

    public static int getDeviceSdk() {
        m_iSdkVersion = android.os.Build.VERSION.SDK_INT;
        return m_iSdkVersion;
    }

    // 获取MacAddress
    public static String getMacAddress() {

        String macAddress = "";
        WifiManager wifi = (WifiManager) IApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifi != null) {
            WifiInfo info = wifi.getConnectionInfo();
            // 如果Wifi关闭的时候，硬件设备可能无法返回MAC ADDRESS
            if (null != info) {
                macAddress = null == info.getMacAddress() ? "" : info.getMacAddress();
            }
        }
        return macAddress;
    }

    public static String getIMEI() {
        TelephonyManager tm = (TelephonyManager) IApplication.getInstance().getSystemService(Service.TELEPHONY_SERVICE);
        if (tm.getDeviceId() != null && !tm.getDeviceId().equals("")) {
            m_sIMEI = null == tm.getDeviceId() ? "" : tm.getDeviceId();
        }
        return m_sIMEI;

    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getPhoneStyle() {
        m_strPhoneStyle = android.os.Build.MODEL;
        return m_strPhoneStyle;
    }

    /**
     * 获取国际移动用户识别码
     *
     * @param context 上下文
     * @return 手机号码，取不到时返回空字符串
     */
    public static String getIMSI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = tm.getSubscriberId();
        if (null == imsi) imsi = "";
        return imsi;
    }

    /**
     * 获取手机电话号码
     *
     * @param context 上下文
     * @return 手机号码，取不到时返回空字符串
     */
    public static String getPhoneNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = tm.getLine1Number();
        if (phoneNumber == null) phoneNumber = "";
        return phoneNumber;
    }

    /**
     * 获取手机ip地址
     *
     * @return ip地址，没有获取到时返回空字符串
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static int getScreenDPI() {
        if (m_screenDPI == -1) {
            DisplayMetrics metric = new DisplayMetrics();
            WindowManager wndMgr = (WindowManager) IApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
            wndMgr.getDefaultDisplay().getMetrics(metric);
            m_screenDPI = metric.densityDpi;
        }
        return m_screenDPI;
    }

    public static int getSIMCardState(Context context) {
        if (m_simCardState == -1) {
            TelephonyManager l_TelephonyManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            m_simCardState = l_TelephonyManager.getSimState();
        }
        return m_simCardState;
    }

    public static void showSoftInput(final Context context, final View focusView) {
        focusView.requestFocus();
        focusView.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMgr.showSoftInput(focusView, 0);
            }
        }, 200);
    }

    public static void hideSoftInput(final Activity activity) {
        if (activity.getCurrentFocus() != null) {
            if (activity.getCurrentFocus().getWindowToken() != null) {
                InputMethodManager inputMgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMgr.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public static void defaultHideSoftInput(final Activity activity) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager inputMgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMgr != null && activity != null)
                    inputMgr.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 200);
    }

    public static void hideSoftInputPlus(Context context) {
        InputMethodManager inputMgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
    }

    /**
     * 获取文字高度和行高
     *
     * @param fontSize
     * @return
     */
    public static int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

    /**
     * 检查邮箱地址是否符合规范
     *
     * @param a_strEmailAddress
     * @return
     */
    public static boolean checkEmailAddressFormat(String a_strEmailAddress) {
        if (a_strEmailAddress == null || a_strEmailAddress.trim().equals("")) return false;

        return Pattern.matches("\\w(\\.?\\w)*\\@\\w+(\\.[\\w&&[\\D]]+)+", a_strEmailAddress);

    }

    /**
     * 检查密码是否符合规范（规范为密码长度是6-20位，而且只能是字母或者是数字的组合）
     */
    public static boolean checkPasswordFormat(String a_strPassword) {
        if (a_strPassword == null || a_strPassword.trim().equals("")) return false;

        if (a_strPassword.length() < 6 || a_strPassword.length() > 20) return false;

        return Pattern.matches("[\\da-zA-Z]+", a_strPassword);

    }

    public static int getScreenWidth() {
        if (m_screenWidth == -1) {
            initDisplayMetrics();
            m_screenWidth = m_displayMetrics.widthPixels;
        }
        return m_screenWidth;
    }

    public static int getScreenHeight() {
        if (m_screenHeight == -1) {
            initDisplayMetrics();
            m_screenHeight = m_displayMetrics.heightPixels;
        }
        return m_screenHeight;
    }

    private static void initDisplayMetrics() {
        if (m_displayMetrics == null) {
            m_displayMetrics = new DisplayMetrics();
            WindowManager wndMgr = (WindowManager) IApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
            wndMgr.getDefaultDisplay().getMetrics(m_displayMetrics);
        }
    }

    /**
     * 判断是否为纯数字
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        } else {
            try {
                Long.valueOf(str);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     * @return
     */
    public static final boolean hideSoftPad(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            return ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return false;
    }

    /**
     * 将byte数组转换成十六进制字符串
     *
     * @param paramArrayOfByte 字节数组
     * @param paramInt         长度
     * @return 十六进制字符串
     */
    public static String bytesToHexString(byte[] paramArrayOfByte, int paramInt) {
        StringBuilder localStringBuilder = new StringBuilder("");
        if ((paramArrayOfByte == null) || (paramArrayOfByte.length <= 0)) return null;

        for (int i = 0; i < paramInt; ++i) {
            String str = Integer.toHexString(0xFF & paramArrayOfByte[i]);
            str = str.toUpperCase();
            if (str.length() < 2) localStringBuilder.append(0);
            localStringBuilder.append(str);
        }
        return localStringBuilder.toString();
    }

    /**
     * 十六进制字符串转换成字节数组
     *
     * @param hexString 十六进制字符串
     * @return 字节数组
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

        }
        return d;
    }

    /**
     * 将字符串转换成字节
     *
     * @param c 字符串
     * @return 字节
     */
    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 判断应用是否处于后台
     *
     * @param context 上下文
     * @return 是否处于后台，true ：后台， false：前台
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                return appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND;
            }
        }
        return false;
    }


    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isStringEmpty(String str) {
        return null == str || str.trim().equals("");
    }


    public static void showSureDialog(final Context context, String title, String msg, String positiveTitle, String negativeTitle, DialogInterface.OnClickListener surelistener) {
        new AlertDialog.Builder(context).setTitle(title).setMessage(msg).setPositiveButton(positiveTitle, surelistener).setNegativeButton(negativeTitle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }


    //是否是wife
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }


    public static boolean checkIsNumber(String value) {
        String regex = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";
        return value.matches(regex);
    }

    public static boolean checkCodeStrting(String code) {
        if (code.length() > 1) {
            String temp = code.substring(0, 1);
            return checkIsNumber(temp);
        }
        return true;
    }


    public static boolean setVisibility(View view, int visibility) {
        if (view.getVisibility() != visibility) {
            view.setVisibility(visibility);
            return true;
        }
        return false;
    }


    /**
     * 获取TextView中文字的高度
     *
     * @param textView TextView控件
     * @return 该控件中的文本宽度
     */
    public static float getTextWidth(TextView textView) {
        return textView.getPaint().measureText(textView.getText().toString());
    }

    /**
     * float转换string
     *
     * @param digits      保留几位小数
     * @param sourceFloat 源数据
     * @return floatString
     */
    public static String getFloatWithDigit(int digits, float sourceFloat) {
        String digitString = "0.";
        if (digits <= 0) {
            digitString = "";
        } else {
            for (int i = 0; i < digits; i++) {
                digitString = digitString + "0";
            }
        }
        String fString = new DecimalFormat(digitString).format(sourceFloat);
        return fString;
    }

    /**
     * double转string
     *
     * @param digits       保留几位小数
     * @param sourceDouble 源数据
     * @return doubleString
     */
    public static String getDoubleWithDigit(int digits, double sourceDouble) {
        String digitString = "0.";
        if (digits <= 0) {
            digitString = "";
        } else {
            for (int i = 0; i < digits; i++) {
                digitString = digitString + "0";
            }
        }
        String dString = new DecimalFormat(digitString).format(sourceDouble);
        return dString;
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static boolean isOpenGPS(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    /**
     * 强制帮用户打开GPS
     *
     * @param context
     */
    public static void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查当前蓝牙打开状态
     *
     * @param context     上下文（要求基于BaseActivity）
     * @param requestCode 打开蓝牙请求码
     */
    public static void checkBluetoothState(Context context, int requestCode) {
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager != null) {
            BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
            Activity activity = (Activity) context;
            if (bluetoothAdapter == null) {
                Toast.makeText(activity, R.string.permission_not_support_bluetooth, Toast.LENGTH_SHORT).show();
            } else {
                if (!bluetoothAdapter.isEnabled()) {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    activity.startActivityForResult(intent, requestCode);
                }
            }
        }
    }

    /**
     * DIP 转 PX
     */
    public static int dipToPx(float dip) {
        return dipToPx(IApplication.getInstance(), dip);
    }

    public static int dipToPx(Context context, float dip) {
        return (int) (context.getResources().getDisplayMetrics().density * dip + 0.5f);
    }

}
