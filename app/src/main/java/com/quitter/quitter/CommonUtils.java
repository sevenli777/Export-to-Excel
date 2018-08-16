package com.quitter.quitter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.view.ViewCompat;
import android.text.InputType;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@SuppressLint("SimpleDateFormat")
public class CommonUtils {
    private static long lastClickTime = 0;
    private static int lastButtonId = -1;
    private static long DIFF = 1000;    //时间间隔

    public static String getSignature(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signatures = info.signatures;
            StringBuilder sb = new StringBuilder();
            for (Signature sign : signatures) {
                sb.append(sign.toCharsString());
            }
            return sb.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 获取屏幕尺寸与密度.
     *
     * @param context the context
     * @return mDisplayMetrics
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        Resources mResources;
        if (context == null) {
            mResources = Resources.getSystem();

        } else {
            mResources = context.getResources();
        }
        //DisplayMetrics{density=1.5, width=480, height=854, scaledDensity=1.5, xdpi=160.421, ydpi=159.497}
        //DisplayMetrics{density=2.0, width=720, height=1280, scaledDensity=2.0, xdpi=160.42105, ydpi=160.15764}
        DisplayMetrics mDisplayMetrics = mResources.getDisplayMetrics();
        return mDisplayMetrics;
    }

    public static String getSeqForNoId(String seriaNo) {
        int sum = 0;
        for (int i = 0; i < seriaNo.length(); i++) {
            sum = sum + Integer.parseInt(String.valueOf(seriaNo.charAt(i)));
        }
        String checkNo = String.valueOf(sum % 10);//校验位
        return seriaNo + checkNo;
    }


    public static void log() {
        StackTraceElement[] eles = Thread.currentThread().getStackTrace();
        String methodName = eles[3].getMethodName();//在这里获取 调用 A.log()方法的方法名
       // LogUtils.e(methodName);
    }

    /**
     * comparing two string digits.
     *
     * @param input
     * @param base
     * @return true if input < base
     */
    public static boolean compareDigit(String input, String base) {
        boolean flag = false;
        try {
            if (Integer.valueOf(input) < Integer.valueOf(base)) {
                flag = true;
            }
        } catch (NumberFormatException e) {
            flag = false;
        }
        return flag;
    }

    public static boolean isTopActivity(Activity activity) {
        String packageName = activity.getPackageName();
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            if (packageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static int getScreenOrientation() {
        Configuration config = MyApplication.getAppContext().getResources().getConfiguration();
        return config.orientation;
    }

    /**
     * format time yyyy-MM-dd HH:mm:ss.SSS-->MM-dd HH:mm
     *
     * @param input time with pattern "yyyy-MM-dd HH:mm:ss.SSS"
     * @return
     */
    public static String formatTime(String input) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
        Date d = null;
        try {
            d = sdf.parse(input);
        } catch (ParseException e) {
            return "";
        }
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(d);
    }

    /**
     * format time yyyy-MM-dd HH:mm:ss-->yyyy年MM月dd日 HH:mm
     *
     * @param input
     * @return
     */
    public static String formatTime1(String input, String format) {
        String res = null;
        try {
            res = convertStrToSpecifiedDateTime(input.trim(),
                    new SimpleDateFormat("yyyy-MM-dd"),
                    new SimpleDateFormat(format));

        } catch (Exception e) {
            res = "";
        }
        return res;
    }

    /**
     * yyyy-MM-dd HH:mm:ss  >>自定义样式
     *
     * @param input
     * @param format
     * @return
     */
    public static String getTimeField(String input, String format) {
        if (TextUtils.isEmpty(input)) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = null;
        try {
            d = sdf.parse(input);
        } catch (ParseException e) {
            return "";
        }
        sdf = new SimpleDateFormat(format);
        return sdf.format(d);
    }

    /**
     * "yyyy-MM-dd HH:mm:ss"-->"HH:mm"
     *
     * @param input
     * @return
     */
    public static String formatTime3(String input) {
        String res = null;
        try {
            res = convertStrToSpecifiedDateTime(input, new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss"), new SimpleDateFormat("HH:mm"));
        } catch (Exception e) {
            res = "";
        }
        return res;
    }

    public static String formatTime4(String input) {
        String res = null;
        try {
            res = convertStrToSpecifiedDateTime(input, new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss"), new SimpleDateFormat("dd日 HH:mm"));
        } catch (Exception e) {
            res = "";
        }
        return res;
    }

    /**
     * long --> date string
     *
     * @param time
     * @return
     */
    public static String getTimeLong2String(long time, String field) {
        SimpleDateFormat sdfDateFormat = new SimpleDateFormat(field);
        String name = sdfDateFormat.format(time);
        return name;
    }

    public static String formatTimeLong2String(long time) {
        SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String name = sdfDateFormat.format(time);
        return name;
    }

    /**
     * 时间戳字符串转换成yyyy-MM-dd HH:mm
     *
     * @param time
     * @return
     */

    public static String getTimeStamp2String(String time, String format) {
        SimpleDateFormat sdfDateFormat = new SimpleDateFormat(format);
        String name = sdfDateFormat.format(new Date(Long.parseLong(time)));
        return name;
    }

    public static String getTimeStamp2String2(String time) {
        SimpleDateFormat sdfDateFormat = new SimpleDateFormat("dd日 HH:mm");
        String name = sdfDateFormat.format(new Date(Long.parseLong(time)));
        return name;
    }

    public static Date getDateTimeFromSring(String s) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date mdate = null;
        mdate = sdf.parse(s);
        calendar.setTime(mdate);
        return calendar.getTime();
    }

    /**
     * 判断查询日期是否为当天
     *
     * @param verifyDate 格式： yyyy-MM-dd
     * @return
     */
    public static boolean isCurrentDay(String verifyDate) {
        SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = Calendar.getInstance().getTime();
        String frmDate = sdfDateFormat.format(date);
        return frmDate.equals(verifyDate);
    }

    public static String dayForWeek(String pTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dayForWeek = "";
        int w = c.get(Calendar.DAY_OF_WEEK) - 1;
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = weekDays[0];
        } else {
            dayForWeek = weekDays[w];
        }
        return dayForWeek;
    }

    public static int dip2px(float dipValue) {
        final float scale = MyApplication.getAppContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(float pxValue) {
        final float scale = MyApplication.getAppContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     * DisplayMetrics类中属性scaledDensity）
     *
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * <p/>
     * DisplayMetrics类中属性scaledDensity）
     *
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int getScreenWidth() {
        return MyApplication.getAppContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return MyApplication.getAppContext().getResources().getDisplayMetrics().heightPixels;
    }

    public static void readScreen(Context context) {
        String TAG = "hello";
        // 获取屏幕密度（方法2）
        DisplayMetrics dm = context.getResources().getDisplayMetrics();

        float density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
        int densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;

        Log.e(TAG + "  DisplayMetrics", "xdpi=" + xdpi + "; ydpi=" + ydpi);
        Log.e(TAG + "  DisplayMetrics", "density=" + density + "; densityDPI=" + densityDPI);

        int screenWidth = dm.widthPixels; // 屏幕宽（像素，如：480px）
        int screenHeight = dm.heightPixels; // 屏幕高（像素，如：800px）

        Log.e(TAG, "screenWidth=" + screenWidth + "; screenHeight=" + screenHeight);
    }

    public static int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = MyApplication.getAppContext().getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    public static int getStatusBarHeight2() {
        Resources resources = MyApplication.getAppContext().getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("dbw", "Status height:" + height);
        return height;
    }

    public static int getNavigationBarHeight() {
        Resources resources = MyApplication.getAppContext().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("dbw", "Navi height:" + height);
        return height;
    }

    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //                Window window = activity.getWindow();
                //                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //                window.setStatusBarColor(activity.getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));

                Window window = activity.getWindow();
                //取消状态栏透明
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //添加Flag把状态栏设为可绘制模式
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //设置状态栏颜色
                window.setStatusBarColor(colorResId);
                //设置系统状态栏处于可见状态
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                //让view不根据系统窗口来调整自己的布局
                ViewGroup mContentView = window.findViewById(Window.ID_ANDROID_CONTENT);
                View mChildView = mContentView.getChildAt(0);
                if (mChildView != null) {
                    ViewCompat.setFitsSystemWindows(mChildView, false);
                    ViewCompat.requestApplyInsets(mChildView);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setWindowStatusBarColor(Dialog dialog, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = dialog.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(dialog.getContext().getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Gzip compress
     */
    public static byte[] compress(byte[] input) {
        if (input == null || input.length == 0) {
            return input;
        }
        byte[] output;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(input);
            gzip.close();
            output = out.toByteArray();
        } catch (IOException e) {
            output = new byte[0];
        } finally {
            try {
                gzip.close();
                out.close();
            } catch (IOException e) {
            }
        }
        return output;
    }

    /**
     * Gzip uncompress
     */
    public static byte[] uncompress(byte[] input) {
        if (input == null || input.length == 0) {
            return input;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        GZIPInputStream gunzip = null;

        byte[] output;
        try {
            gunzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            output = out.toByteArray();
        } catch (IOException e) {
            output = new byte[0];
        } finally {
            try {
                gunzip.close();
                in.close();
                out.close();
            } catch (IOException e) {
            }
        }
        return output;
    }

    public static <T> T cloneObject(T obj) {
        return toObject(toByteArray(obj));
    }

    /**
     * 将对象序列化成byte数组
     *
     * @param obj
     * @return
     */
    public static byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        if (null == obj)
            return bytes;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 将byte数组反序列化
     *
     * @param bytes
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T toObject(byte[] bytes) {
        if (null == bytes)
            return null;
        if (bytes.length < 1)
            return null;

        T obj;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = (T) ois.readObject();

            ois.close();
            bis.close();
        } catch (Exception e) {
            return null;
        }
        return obj;
    }

    /**
     * 将字符串的日期时间转化成指定的日期时间字符串
     */
    public static String convertStrToSpecifiedDateTime(String s, SimpleDateFormat oldSdf, SimpleDateFormat sdf)
            throws ParseException {
        if ("".equals(s))
            return "";
        Date date = oldSdf.parse(s);
        return sdf.format(date);
    }

    public static <T> boolean isListEmpty(List<T> inputs) {
        return null == inputs || inputs.isEmpty();
    }


    public static int str2Int(String input) {
        if (TextUtils.isEmpty(input)) {
            return 0;
        }
        int output = 0;
        try {
            output = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            output = 0;
        }
        return output;
    }

    /**
     * 遗留问题，这里只能改内容了，函数名字不变
     *
     * @param input
     * @return double
     */
    public static double str2Float(String input) {
        if (TextUtils.isEmpty(input)) {
            return 0;
        }
        double output = 0;
        try {
            output = Double.valueOf(input);
        } catch (NumberFormatException e) {
            output = 0;
        }
        return output;
    }

    public static long str2Long(String input) {
        if (TextUtils.isEmpty(input)) {
            return 0;
        }
        long output = 0;
        try {
            output = Long.valueOf(input);
        } catch (NumberFormatException e) {
            output = 0;
        }
        return output;
    }

    public static String int2Str(int input) {
        String output = "";
        try {
            output = String.valueOf(input);
        } catch (NumberFormatException e) {
            output = "";
        }
        return output;
    }

    /**
     * 计算字符串的长度
     *
     * @param text 要计算的字符串
     * @param Size 字体大小
     * @return
     */
    public static float getTextWidth(String text, float Size) {
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(Size);
        return textPaint.measureText(text);
    }


    /**
     * 日期加一天
     *
     * @param date
     * @param dayAddNum
     * @return
     */
    public static String getTomorrowDate(String date, int dayAddNum) {
        if (TextUtils.isEmpty(date)) {
            return "";
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        Date nowDate = null;
        Calendar cal = Calendar.getInstance();
        try {
            nowDate = sf.parse(date);
            cal.setTime(nowDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        cal.add(Calendar.DAY_OF_MONTH, dayAddNum);
        String nextDate_1 = sf.format(cal.getTime());
        return nextDate_1;
    }

    public static String getTomorrowDate(String pattern, String date, int dayAddNum) {
        if (TextUtils.isEmpty(date)) {
            return "";
        }
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        Date nowDate = null;
        Calendar cal = Calendar.getInstance();
        try {
            nowDate = sf.parse(date);
            cal.setTime(nowDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        cal.add(Calendar.DAY_OF_MONTH, dayAddNum);
        String nextDate_1 = sf.format(cal.getTime());
        return nextDate_1;
    }


    public static long getTimeStampFormat(String time, String format) {
        long realTime = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date date = sdf.parse(time);
            realTime = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return realTime;
    }

    /**
     * 时间戳 >>时间字符串
     *
     * @param time
     * @return
     */
    public static String getStrTime(long time, String format) {
        String realTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        realTime = sdf.format(new Date(time));
        return realTime;
    }

    public static Date getCurrentDateTime() {
        return new Date();
    }

    public static String getSexByIdcard(String argss) {
        String sex = null;
        if (TextUtils.isEmpty(argss)) {
            return "MAN";
        }
        if (argss.length() == 15) {
            sex = argss.substring(argss.length() - 1, argss.length());
        } else if (argss.length() == 18) {
            sex = argss.substring(argss.length() - 2, argss.length() - 1);
        }
        int xing = str2Int(sex) % 2;
        if (xing == 0) {
            return "WOMAN";
        } else {
            return "MAN";
        }
    }

    /**
     * 隐藏实名名字
     *
     * @param name
     * @return
     */
    public static String hideRealName(String name) {
        String realName = "", endStr = "";
        if (TextUtils.isEmpty(name)) {
            return "";
        }
        if (name.substring(1, name.length()).length() == 1) {
            endStr = name.substring(1, name.length()).replace(name.substring(1, name.length()), "*");
        } else if (name.substring(1, name.length()).length() >= 2) {
            endStr = name.substring(1, name.length()).replace(name.substring(1, name.length()), "**");
        }
        realName = name.substring(0, 1) + endStr;
        return realName;
    }

    /**
     * 隐藏身份证号
     *
     * @param idcard
     * @return
     */
    public static String hideIdCard(String idcard) {
        String strIdCard = "";
        if (TextUtils.isEmpty(idcard)) {
            return "";
        }
        strIdCard = idcard.substring(0, 3) + "********" + idcard.substring(idcard.length() - 4, idcard.length());
        return strIdCard;
    }

    public static String getPkgType(String string) {
        String type = "";
        if ("FOR_NORMAL".equals(string)) {
            type = "普通红包";
        } else if ("FOR_LUCKY".equals(string)) {
            type = "拼手气红包";
        } else {
            type = "普通红包";
        }
        return type;
    }

    /**
     * 保存小数点后两位
     *
     * @param str
     * @return
     */
    public static String fen2yuan(String str) {
        double temp = str2Float(str);
        if (temp == 0) {
            return "0.00";
        }
        double realNum = temp / 100f;
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(realNum);
    }


    /**
     * @param str      金额
     * @param multiple 倍数
     * @return
     */
    public static String getAllMoney(String str, int multiple) {
        double temp = (double) multiple * yuan2FenLong(str);
        if (temp == 0) {
            return "0.00";
        }
        BigDecimal decimal = new BigDecimal(temp);
        return String.valueOf(decimal.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
    }


    public static String getTradeListMoney(long str) {
        float realNum = str / 100f;
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(realNum);
    }

    /**
     * 元转为分
     */
    public static String yuan2FenStr(String string) {
        return String.valueOf(yuan2FenLong(string));
    }

    /**
     * 方法说明：金额元转为分<br>
     *
     * @param amount
     * @return
     */
    public static long yuan2FenLong(String amount) {
        if (TextUtils.isEmpty(amount)) {
            return 0;
        }
        if (amount.indexOf(",") != -1) {
            amount = amount.replace(",", "");
        }
        BigDecimal decimal = new BigDecimal(amount);
        return decimal.multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_EVEN).longValue();
    }


    /**
     * 分转成元
     *
     * @param str
     * @return
     */
    public static String fen2yuan(long str) {
        if (str == 0) {
            return "0.00";
        }
        BigDecimal decimal = new BigDecimal(str);
        return String.valueOf(decimal.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
    }

    /**
     * 保留两位小数
     */
    public static String formatMoney2Bit(String str) {
        if (TextUtils.isEmpty(str)) {
            return "0.00";
        }
        DecimalFormat df = new DecimalFormat("0.00");
        double number = 0.00;
        try {
            number = Double.parseDouble(str);
        } catch (Exception e) {
            number = 0.00;
        }
        return df.format(number);
    }

    public static void toggleSoftInput(Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void hiddenSoftInput(IBinder windowToken, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(windowToken, InputMethodManager.RESULT_HIDDEN);
    }

    public static void hiddenSoftInputWithView(View view, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void disableShowInput(Context mContext, EditText editText) {
        if (Build.VERSION.SDK_INT <= 10) {
            editText.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {//TODO: handle exception
            }
        }
    }

    public static void openKeyboard(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


    public static void showSoftInput(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }

    public static void hideSoftInput(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0); //强制隐藏键盘
    }

    public static boolean isSoftInputShowing(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();
    }


    public static String educationChineseToEnglish(String text) {
        String result = "";
        if ("专科".equals(text)) {
            result = "specialized_subject";
        }
        if ("本科".equals(text)) {
            result = "undergraduate";
        }
        if ("硕士研究生".equals(text)) {
            result = "master_degree";
        }
        if ("博士研究生".equals(text)) {
            result = "doctoral_candidate";
        }
        return result;
    }

    public static String StudyChineseToEnglish(String text) {
        String result = "";
        if ("全日制".equals(text)) {
            result = "FULLTIME";
        }
        if ("非全日制".equals(text)) {
            result = "PARTTIME";
        }

        return result;
    }

    public static boolean isValidDate(String str, String pattern) {
        try {
            // 指定日期格式
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2004/02/29会被接受，并转换成2004/03/01
            dateFormat.setLenient(false);
            dateFormat.parse(str);
            return true;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }
    }

    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 判断两次点击的间隔，如果小于1s，则认为是多次无效点击（同一个view，固定时长1s）
     *
     * @return
     */
    public static boolean isFastDoubleClick(int buttonId) {
        return isFastDoubleClick(buttonId, DIFF);
    }

    /**
     * 判断两次点击的间隔，如果小于diff，则认为是多次无效点击（同一按钮，自定义间隔时长）
     *
     * @param diff
     * @return
     */
    public static boolean isFastDoubleClick(int buttonId, long diff) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (lastButtonId == buttonId && lastClickTime > 0 && timeD < diff) {
            Log.d("isFastDoubleClick", "短时间内view被多次点击");
            return true;
        }
        lastClickTime = time;
        lastButtonId = buttonId;
        return false;
    }

}
