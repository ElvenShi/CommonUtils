package com.syz.example.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.File;

/**
 * Created by SYZ on 16/9/21.
 * 这是一个封装了利用本地应用打开指定文件的intent工具类
 */
public class IntentUtils {

    private static final String[][] MIME_MapTable = {
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };

    /**
     * open the file
     *
     * @param file
     * @return
     */
    public static Intent createFileIntent(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = getMIMEType(file);
        intent.setDataAndType(Uri.fromFile(file), type);
        return intent;
    }

    /**
     * get MIME from file
     *
     * @param file
     */
    private static String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (TextUtils.isEmpty(end)) {
            return type;
        }
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    /**
     * 展示HTML
     * @param filename
     * @return
     */
    public static Intent getHtmlFileIntent(String filename) {
        Uri uri = Uri.parse(filename).buildUpon()
                .encodedAuthority("com.android.htmlfileprovider")
                .scheme("content").encodedPath(filename).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    /**
     * 展示图片
     */
    public static Intent getImageFileIntent(String filename) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filename));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    /**
     * 展示pdf
     *
     * @return
     * @filename filename
     */
    public static Intent getPdfFileIntent(String filename) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filename));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    /**
     * 展示普通文本
     */
    public static Intent getTextFileIntent(String filename, boolean filenameBoolean) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (filenameBoolean) {
            Uri uri1 = Uri.parse(filename);
            intent.setDataAndType(uri1, "text/plain");
        } else {
            Uri uri2 = Uri.fromFile(new File(filename));
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }

    /**
     * 音频文件
     */
    public static Intent getAudioFileIntent(String filename) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(filename));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    /**
     * 视频文件
     */
    public static Intent getVideoFileIntent(String filename) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(filename));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    /**
     * chm
     *
     * @return
     * @filename filename
     */
    public static Intent getChmFileIntent(String filename) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filename));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    /**
     * work
     *
     * @return
     * @filename filename
     */
    public static Intent getWordFileIntent(String filename) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filename));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    /**
     * excel
     *
     * @return
     * @filename filename
     */
    public static Intent getExcelFileIntent(String filename) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filename));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    /**
     * ppt
     *
     * @return
     * @filename filename
     */
    public static Intent getPptFileIntent(String filename) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filename));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;

    }

    /**
     * go to send message page.
     */
    public static Intent sendMessage(Context context, String address,
                                     String content) {
        Uri uri = Uri.parse("smsto:" + address);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", content);
        return intent;
    }

    /**
     * send multi-media message.
     *
     * @param context
     * @param address the address of receiver
     * @param content the content of message
     * @param file    the content of the attached file.
     */
    public static Intent sendMessage(Context context, String address,
                                     String content, File file) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra("address", address);
        intent.putExtra("sms_body", content);
        Uri uri = Uri.fromFile(file);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        String suffix = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        String mime = null;
        if (!"".equals(suffix))
            mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (mime == null)
            mime = "image/jpeg";
        intent.setType(mime);

        return intent;
    }

    /**
     * make call
     *
     * @param context
     * @param number
     * @return
     */
    public static Intent call(Context context, String number) {
        Intent intent = new Intent("android.intent.action.CALL",
                Uri.parse("tel:" + number));
        return intent;
    }

    /**
     * 浏览器打开网页
     * @param url
     * @return
     */
    public static Intent toBrowser(String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        return intent;
    }

    /**
     * 获取一个用于打开图片的intent
     *
     * @param filePath
     * @return
     */
    public static Intent getImageIntent(String filePath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    /**
     * Android获取一个用于打开APK文件的intent
     *
     * @param filePath
     * @return
     */
    public static Intent getApkFileIntent(String filePath) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * Android获取一个用于打开不确定类型文件的intent
     *
     * @param filePath
     * @return
     */
    public static Intent getAllIntent(String filePath) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "*/*");
        return intent;
    }

}
