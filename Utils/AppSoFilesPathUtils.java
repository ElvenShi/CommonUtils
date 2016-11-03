package com.syz.example.utils;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by SYZ on 16/11/3.
 * <br>
 * 获取so文件安装目录
 */

public class AppSoFilesPathUtils {
    /**
     * 查找APP中so文件的安装目录
     * Android OS >= 2.3
     * @param context
     * @param libName
     * @return
     */
    public static String findLibrary1(Context context, String libName) {
        String result = null;
        ClassLoader classLoader = (context.getClassLoader());
        if (classLoader != null) {
            try {
                Method findLibraryMethod = classLoader.getClass().getMethod("findLibrary", new Class<?>[] { String.class });
                if (findLibraryMethod != null) {
                    Object objPath = findLibraryMethod.invoke(classLoader, new Object[] { libName });
                    if (objPath != null && objPath instanceof String) {
                        result = (String) objPath;
                    }
                }
            } catch (NoSuchMethodException e) {
                Log.e("findLibrary1", e.toString());
            } catch (IllegalAccessException e) {
                Log.e("findLibrary1", e.toString());
            } catch (IllegalArgumentException e) {
                Log.e("findLibrary1", e.toString());
            } catch (InvocationTargetException e) {
                Log.e("findLibrary1", e.toString());
            } catch (Exception e) {
                Log.e("findLibrary1", e.toString());
            }
        }

        return result;
    }

    /**
     * The function use to find so path for compatible android system
     * Android OS <= 2.2
     */
    public static String findLibrary2(Context context, String libName) {
        String result = null;
        ClassLoader classLoader = (context.getClassLoader());
        if (classLoader != null) {
            try {
                Method findLibraryMethod = classLoader.getClass().getDeclaredMethod("findLibrary", new Class<?>[] { String.class });
                if (findLibraryMethod != null) {
                    if (!findLibraryMethod.isAccessible()) {
                        findLibraryMethod.setAccessible(true);
                    }
                    Object objPath = findLibraryMethod.invoke(classLoader, new Object[] { libName });
                    if (objPath != null && objPath instanceof String) {
                        result = (String) objPath;
                    }
                }
            } catch (NoSuchMethodException e) {
                Log.e("findLibrary2", e.toString());
            } catch (IllegalAccessException e) {
                Log.e("findLibrary2", e.toString());
            } catch (IllegalArgumentException e) {
                Log.e("findLibrary2", e.toString());
            } catch (InvocationTargetException e) {
                Log.e("findLibrary2", e.toString());
            } catch (Exception e) {
                Log.e("findLibrary2", e.toString());
            }
        }

        return result;
    }
}
