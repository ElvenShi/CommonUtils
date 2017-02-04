package com.gfound.viface.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import com.gfound.viface.R;

import org.videolan.vlc.util.LogUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PictureUtils {
    private static final String TAG = "PictureUtils";
    public static final int NONE = 0;
    public static final String IMAGE_UNSPECIFIED = "image/*";//任意图片类型
    public static final int PHOTOGRAPH = 0x1001;// 拍照
    public static final int SELECT_PICTURE_FROM_ALBUM = 0x1002; // 相册选取
    public static final int PHOTORESOULT = 0x1003;// 结果
    public static final int PICTURE_HEIGHT = 500;
    public static final int PICTURE_WIDTH = 500;
    public static String imageName;

    /**================================相册选取，拍照，剪切=======================================**/

    /**
     * 从系统相册中选取照片,没有剪切步骤，直接返回
     *
     * @param fragment
     */
    public static void albumSelection(Fragment fragment) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType(IMAGE_UNSPECIFIED);
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        fragment.startActivityForResult(Intent.createChooser(intent, fragment.getString(R.string.choose_picture)),
                SELECT_PICTURE_FROM_ALBUM);
    }

    /**
     * 从系统相册中选取照片,没有剪切步骤，直接返回
     * @param activity
     */
    public static void albumSelection(Activity activity){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(IMAGE_UNSPECIFIED);
        activity.startActivityForResult(Intent.createChooser(intent, activity.getString(R.string.choose_picture)),
                SELECT_PICTURE_FROM_ALBUM);
    }

    /**
     * 从系统相册中选取照片，带有剪切步骤的
     *
     * @param activity
     */
    public static void selectPictureFromAlbum(Activity activity) {
        // 调用系统的相册
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_UNSPECIFIED);

        // 调用剪切功能
        activity.startActivityForResult(intent, SELECT_PICTURE_FROM_ALBUM);
    }

    /**
     * 从系统相册中选取照片上传，带有剪切步骤的
     *
     * @param fragment
     */
    public static void selectPictureFromAlbum(Fragment fragment) {
        // 调用系统的相册
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_UNSPECIFIED);

        // 调用剪切功能
        fragment.startActivityForResult(intent, SELECT_PICTURE_FROM_ALBUM);
    }

    /**
     * 返回图片路径
     * @param activity
     * @return
     */
    public static String getPicturePath(Activity activity){
        String path;
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().getPath()+File.separator+activity.getPackageName();
        } else {
            path = activity.getFilesDir().getPath()+File.separator+activity.getPackageName();
        }
        return path;
    }

    /**
     * 拍照
     *
     * @param activity
     */
    public static void photograph(Activity activity) {
        imageName = File.separator + getStringToday() + ".jpg";
        String path = getPicturePath(activity);
        File file = new File(path);
        if (!file.exists()){
            file.mkdirs();
        }
        // 调用系统的拍照功能
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                file, imageName)));
        activity.startActivityForResult(intent, PHOTOGRAPH);
    }

    /**
     * 拍照
     *
     * @param fragment
     */
    public static void photograph(Fragment fragment) {
        imageName = File.separator + getStringToday() + ".jpg";
        String path = getPicturePath(fragment.getActivity());
        File file = new File(path);
        if (!file.exists()){
            file.mkdirs();
        }
        // 调用系统的拍照功能
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                file, imageName)));
        fragment.startActivityForResult(intent, PHOTOGRAPH);
    }

    /**
     * 图片裁剪
     *
     * @param activity
     * @param uri
     * @param height
     * @param width
     */
    public static void startPhotoZoom(Activity activity, Uri uri, int height, int width) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", height);
        intent.putExtra("outputY", width);
        intent.putExtra("noFaceDetection", true); //关闭人脸检测
        intent.putExtra("return-data", true);//如果设为true则返回bitmap
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);//输出文件
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        activity.startActivityForResult(intent, PHOTORESOULT);
    }

    /**
     * 图片裁剪
     *
     * @param activity
     * @param uri      原图的地址
     * @param height   指定的剪辑图片的高
     * @param width    指定的剪辑图片的宽
     * @param destUri  剪辑后的图片存放地址
     */
    public static void startPhotoZoom(Activity activity, Uri uri, int height, int width, Uri destUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", height);
        intent.putExtra("outputY", width);
        intent.putExtra("noFaceDetection", true); //关闭人脸检测
        intent.putExtra("return-data", false);//如果设为true则返回bitmap
        intent.putExtra(MediaStore.EXTRA_OUTPUT, destUri);//输出文件
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        activity.startActivityForResult(intent, PHOTORESOULT);
    }

    /**
     * 图片裁剪
     *
     * @param fragment
     * @param uri
     * @param height
     * @param width
     */
    public static void startPhotoZoom(Fragment fragment, Uri uri, int height, int width) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", height);
        intent.putExtra("outputY", width);
        intent.putExtra("return-data", true);
        fragment.startActivityForResult(intent, PHOTORESOULT);
    }

    /**
     * 获取当前系统时间并格式化
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getStringToday() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 按正方形裁切图片
     */
    public static Bitmap ImageCrop(Bitmap bitmap) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        LogUtils.print(TAG,"原图宽："+w+"\t 原图高："+h);

        int wh = w > h ? h : w;// 裁切后所取的正方形区域边长

        int retX = w > h ? (w - h) / 2 : 0;//基于原图，取正方形左上角x坐标
        int retY = w > h ? 0 : (h - w) / 2;

        LogUtils.print(TAG,"x坐标："+retX+"y坐标："+retY);

        //下面这句是关键
        return Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null, false);
    }

    /**
     * 根据x,y坐标和width，height切出给定图片中的部分图像
     * @param bitmap 图源
     * @param retX 图片的x坐标
     * @param retY 图片的y坐标
     * @param width  图片宽
     * @param height 图片高
     * @return
     */
    public static Bitmap cropImage(Bitmap bitmap,int retX,int retY,int width,int height){
        LogUtils.print(TAG,"x坐标："+retX+"y坐标："+retY);
        //下面这句是关键
        return Bitmap.createBitmap(bitmap, retX, retY, width, height, null, false);
    }


    /**
     * 返回需要的图片bitmap
     * @param path
     * @return
     */
    public static Bitmap decodeBitmapFromResource(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        /**
         * 当inJustDecodeBounds为true时，执行decodeXXX方法时，
         * BitmapFactory只会解析图片的原始宽高信息，并不会真正的加载图片到内存
         */
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path,options);
        options.inSampleSize = calculateSampleSize(options,500,500);
        options.inJustDecodeBounds =false;
        return  BitmapFactory.decodeFile(path,options);
    }

    /**
     * 计算合适的采样率(当然这里还可以自己定义计算规则)，reqWidth为期望的图片大小，单位是px
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
        LogUtils.print(TAG,"calculateSampleSize reqWidth:"+reqWidth+",reqHeight:"+reqHeight);
        int width = options.outWidth;
        int height =options.outHeight;
        LogUtils.print(TAG,"calculateSampleSize width:"+width+",height:"+height);
        int inSampleSize = 1;
        int halfWidth = width/2;
        int halfHeight = height/2;
        while((halfWidth/inSampleSize)>=reqWidth&& (halfHeight/inSampleSize)>=reqHeight){
            inSampleSize*=2;
            LogUtils.print(TAG,"calculateSampleSize inSampleSize:"+inSampleSize);
        }
        return inSampleSize;
    }

    /**
     * 解决小米手机上获取图片路径为null的情况
     * @param intent
     * @param context
     * @return
     */
    public static Uri getPictureUri(android.content.Intent intent, Context context) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[] { MediaStore.Images.ImageColumns._ID },
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }
}