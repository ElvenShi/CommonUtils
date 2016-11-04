package com.syz.example.adapter;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by SYZ on 16/10/27.
 * 通用的ViewHolder。不需要继承，不关心ItemView布局中又哪些控件
 */

public class ViewHolder {

    private SparseArray<View> viewArray = new SparseArray<View>();
    private View convertView;

    /**
     * 传入convertView
     * @param convertView
     */
    public ViewHolder(View convertView) {
        this.convertView = convertView;
    }

    /**
     * 从convertView中取出对应resId的view
     * @param resId
     * @param <T>
     * @return
     */
    public < T extends View> T getView(int resId){
        View view = viewArray.get(resId);
        if (null == view){
            view = convertView.findViewById(resId);
            viewArray.put(resId,view);
        }
        return (T) view;
    }
}
