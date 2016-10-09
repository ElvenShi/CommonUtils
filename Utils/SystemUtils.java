package com.syz.example.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Parcelable;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create By SYZ
 * <br><br>
 * 该类中主要是一些常用的工具方法<br><br>
 * 1.获取当前的进程名称<br>
 * 2.分辨率转换(dp->px,px->dp)<br>
 * 3.MD5加密<br>
 * 4.获取manifest中配置的meta值<br>
 * 5.检测app是否具有某个权限<br>
 * 6.检测app某个权限是否被禁止<br>
 * 7.根据logourl生成对应的文件名称<br>
 * 8.根据url获取文件名称<br>
 * 9.计算视图的宽高。<br>
 * 10.获取屏幕宽度<br>
 * 11.获取屏幕密度(dpi)<br>
 * 12.获取屏幕高度<br>
 * 13.获得状态栏的高度，通过反射<br>
 * 14.网络相关，网络是否可用，wifi是否连接，获取当前网络类型<br>
 * 15.获取随机数<br>
 * 16.sd卡是否挂载并可读写<br>
 * 17.获取外部应用程序缓存路径<br>
 * 18.获取内部缓存路径<br>
 * 19.获取mac地址<br>
 * 20.获取DeviceID<br>
 * 21.获取IMEI<br>
 * 22.验证是否符合email规范<br>
 * 23.是否符合手机号码规范<br>
 * 24.是否符合价格规范($)<br>
 * 25.获取应用版本号名称<br>
 * 26.获取本机ip地址<br>
 * 27.添加输入框输入长度限制<br>
 * 28.bitmap图片转为圆形bitmap<br>
 * 29.bitmap转base64<br>
 * 30.构造sql查询条件<br>
 * 31.获取当前CPU类型<br>
 * 32.是否是主线程<br>
 */
public class SystemUtils {

    /**
     * 该类中所有需要传入context的方法，最好传入Application Context
     */


    /**
     * 根据指定的pid，获得对应的进程名称
     * @return null may be returned if the specified process not found
     */
    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    private static String toHexString(byte[] b) {
        // String to byte
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    /** MD5加密 */
    private static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            return toHexString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取manifest中配置的meta值
     *
     * @param context
     * @param name
     *            key的名称
     * @return
     */
    public static String getMetaDataValue(Context context, String name) {
        if (context == null) {
            return null;
        }
        PackageManager packageManager = context.getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                Object value = applicationInfo.metaData.get(name);
                if (value != null) {
                    return value.toString();
                } else {
                    return null;
                }
            }
        } catch (NameNotFoundException e) {
//            Log.e(TAG, "manifest中没有配置" + name);
        }
        return null;
    }

    /**
     * 检测app是否具有某个权限
     *
     * @param context
     * @param permissionName
     *            权限名称， 如 android.permission.RECORD_AUDIO
     * @return
     */
    public static boolean hasPermission(Context context, String permissionName) {
        PackageManager pm = context.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED == pm
                .checkPermission(permissionName, context.getPackageName()));
        return permission;
    }

    /**
     * 检测app某个权限是否被禁止
     *
     * @param context
     * @param permissionName
     *            权限名称， 如 android.permission.RECORD_AUDIO
     * @return
     */
    public static boolean isPermissionDenied(Context context,
                                             String permissionName) {
        PackageManager pm = context.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_DENIED == pm
                .checkPermission(permissionName, context.getPackageName()));
        return permission;
    }

    /**
     * 根据logourl生成对应的文件名称
     * @param logoUrl
     * @return
     */
    public static String getNameFromLogoUrl(String logoUrl) {
        if (TextUtils.isEmpty(logoUrl)) {
            return null;
        }

        return md5(URLEncoder.encode(logoUrl));
    }

    /**
     * 根据url获取文件名称
     * @param url
     * @return
     */
    public static String getFileNameFronUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public static boolean checkExternalStorageAvailable() {
        boolean bAvailable = Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
        if (bAvailable) {
            StatFs statfs = new StatFs(Environment
                    .getExternalStorageDirectory().getPath());
            if ((long) statfs.getAvailableBlocks()
                    * (long) statfs.getBlockSize() < 1048576) {
                // ToastManager.show(sApplication, R.string.prompt_sdcard_full);
                bAvailable = false;
            }
        } else {
            // ToastManager.show(sApplication,
            // R.string.prompt_sdcard_unavailable);
        }
        return bAvailable;
    }

    private static float sDensity = 0;

    private static int WIDTH = 0;
    private static int HEIGHT = 0;
    private static int DPI = 0;

    /**
     * 计算视图的宽高。
     *
     * @param view
     */
    public static void measureView(View view) {
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int nWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int nHeight = p.height;
        int nHeightSpec;

        if (nHeight > 0) {
            nHeightSpec = MeasureSpec.makeMeasureSpec(nHeight,
                    MeasureSpec.EXACTLY);
        } else {
            nHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        view.measure(nWidthSpec, nHeightSpec);
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth(Context context) {
        if (WIDTH == 0) {
            WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            DPI = dm.densityDpi;
            WIDTH = dm.widthPixels;
            HEIGHT = dm.heightPixels;
        }
        return WIDTH;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight(Context context) {
        if (HEIGHT == 0) {
            getScreenWidth(context);
        }
        return HEIGHT;
    }

    /***
     * 获取屏幕密度
     *
     * @return
     */
    public static int getScreenDpi(Context context) {
        if (DPI == 0) {
            getScreenWidth(context);
        }
        return DPI;
    }

    /**
     * 获得状态栏的高度，通过反射
     *
     * @param context
     * @return 如果为－1则获取失败
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return statusHeight;
    }

    /***
     * 是否连接的wifi
     *
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /***
     * 当前网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {
            return true;
        }
        return false;
    }


    /**
     * 当前网络是否可用
     *
     * @param intent
     * @return
     */
    public static boolean isNetworkAvailable(Intent intent) {
        Parcelable p = intent
                .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
        if (p != null) {
            NetworkInfo ni = (NetworkInfo) p;
            if (ni.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    /**
     * @author 获取当前的网络状态 -1：没有网络 1：WIFI网络 2：wap网络 3：net网络
     * @param context
     * @return
     */
    public static int getNetworkType(Context context) {
        int netType = -1;
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            if (networkInfo.getExtraInfo().toLowerCase(Locale.CHINA)
                    .equals("cmnet")) {
                netType = 3;
            } else {
                netType = 2;
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 1;
        }
        return netType;
    }

    /**
     * 获取随机数
     *
     * @param nStart
     *            最小数值
     * @param nEnd
     *            最大数值
     * @return
     */
    public static int randomRange(int nStart, int nEnd) {
        if (nStart >= nEnd) {
            return nEnd;
        }
        return nStart + (int) (Math.random() * (nEnd - nStart));
    }

    /**
     * sd卡是否挂载并可读写
     *
     * @return
     */
    public static boolean isExternalStorageMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    /**
     * 获取外部应用程序缓存路径
     *
     * @param context
     * @return
     */
    public static String getExternalCachePath(Context context) {
        String path = Environment.getExternalStorageDirectory().getPath()
                + "/Android/data/" + context.getPackageName() + "/cache";
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return path;
    }

    /**
     * 获取内部缓存路径
     *
     * @param context
     * @return
     */
    public static String getCachePath(Context context) {
        String path = context.getApplicationContext().getCacheDir().getPath();
        return path;
    }

    /**
     * dip转px
     *
     * @param context
     * @param nDip
     * @return
     */
    public static int dipToPixel(Context context, int nDip) {
        if (sDensity == 0) {
            final WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            sDensity = dm.density;
        }
        return (int) (sDensity * nDip);
    }

    /**
     * 获取系统DeviceID
     *
     * @param context
     * @return
     */
    public static String getDeviceUUID(Context context) {
        final TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String strIMEI = tm.getDeviceId();
        if (TextUtils.isEmpty(strIMEI)) {
            strIMEI = "1";
        }

        String strMacAddress = getMacAddress(context);
        if (TextUtils.isEmpty(strMacAddress)) {
            strMacAddress = "1";
        }

        return strIMEI + strMacAddress;
    }

    /**
     * 获取IMEI
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        final TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String strIMEI = tm.getDeviceId();
        if (TextUtils.isEmpty(strIMEI)) {
            strIMEI = "1";
        }
        return strIMEI;
    }

    /**
     * 获取mac地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        final WifiManager wm = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        return wm.getConnectionInfo().getMacAddress();
    }

    public static void copyToClipBoard(Context context, String strText) {
        final ClipboardManager manager = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        manager.setText(strText);
    }

    /**
     * 验证是否符合email规范
     *
     * @param strEmail
     * @return
     */
    public static boolean isEmail(String strEmail) {
        Pattern pattern = Pattern
                .compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(strEmail);
        return matcher.matches();
    }

    /**
     * 是否符合手机号码规范
     *
     * @param str
     * @return
     */
    public static boolean isMobile(String str) {
        Pattern p = Pattern
                .compile("^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$"); // 验证手机号
        Matcher m = p.matcher(str);
        return m.matches();
    }


    /**
     * 是否符合价格规范
     *
     * @param price
     * @return
     */
    public static boolean isPrice(String price) {
        String aaString = "^(?!0\\d)(?!\\.)[0-9]+(\\.[0-9]{1,2})?$";
        Pattern p = Pattern.compile(aaString);
        Matcher m = p.matcher(price);
        return m.matches();
    }

    /**
     * 获取应用版本号名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取本机ip地址
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 添加输入框输入长度限制
     *
     * @param editText
     * @param nLengthLimit
     */
    public static void addEditTextLengthFilter(EditText editText,
                                               int nLengthLimit) {
        InputFilter filters[] = editText.getFilters();
        if (filters == null) {
            editText.getEditableText().setFilters(
                    new InputFilter[] { new InputFilter.LengthFilter(
                            nLengthLimit) });
        } else {
            final int nSize = filters.length + 1;
            InputFilter newFilters[] = new InputFilter[nSize];
            int nIndex = 0;
            for (InputFilter filter : filters) {
                newFilters[nIndex++] = filter;
            }
            newFilters[nIndex] = new InputFilter.LengthFilter(nLengthLimit);
            editText.getEditableText().setFilters(newFilters);
        }
    }

    /**
     * 图片转为圆形
     *
     * @param bitmap
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    /**
     * bitmap转base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            baos.flush();
            baos.close();

            byte[] bitmapBytes = baos.toByteArray();
            result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 构造sql查询条件 input = {"userId"}, output = ("userId = ?") input = {"userId" ,
     * "role"}, output = ("userId = ? and role = ?");
     *
     * @param columns
     *            列名
     * @return 构造后的查询条件字符串
     */
    public static String getSelection(String[] columns) {
        if (columns == null || columns.length == 0) {
            return null;
        }
        String selection = "";
        for (int i = 0; i < columns.length - 1; i++) {
            selection = selection + columns[i] + " = ? and ";
        }
        selection = selection + columns[columns.length - 1] + " = ?";
        return selection;
    }

    /**
     * 验证当前CPU类型是否是armV7
     * @return
     */
    public static boolean isArmV7() {
        return getCpuType().contains("armv7");
    }

    /**
     * 获取当前CPU类型
     * @return
     */
    public static String getCpuType() {
        String strInfo = getCpuString();
        String strType = null;

        if (strInfo.contains("ARMv5")) {
            strType = "armv5";
        } else if (strInfo.contains("ARMv6")) {
            strType = "armv6";
        } else if (strInfo.contains("ARMv7")) {
            strType = "armv7";
        } else if (strInfo.contains("x86")) {
            strType = "x86";
        } else {
            strType = "unknown";
            return strType;
        }

        if (strInfo.contains("neon")) {
            strType += "_neon";
        } else if (strInfo.contains("vfpv3")) {
            strType += "_vfpv3";
        } else if (strInfo.contains(" vfp")) {
            strType += "_vfp";
        } else {
            strType += "_none";
        }
        return strType;
    }

    static public String getCpuString() {
        if (Build.CPU_ABI.equalsIgnoreCase("x86")) {
            return "x86";
        }

        String strInfo = "";
        RandomAccessFile reader = null;
        try {
            byte[] bs = new byte[1024];
            reader = new RandomAccessFile("/proc/cpuinfo", "r");
            reader.read(bs);
            String ret = new String(bs);
            int index = ret.indexOf(0);
            if (index != -1) {
                strInfo = ret.substring(0, index);
            } else {
                strInfo = ret;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }

        return strInfo;
    }

    /**
     * 是否是主线程
     * @return
     */
    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

}
