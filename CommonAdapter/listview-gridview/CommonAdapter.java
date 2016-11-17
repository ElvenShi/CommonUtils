package com.syz.example.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.syz.example.App;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SYZ on 16/10/27.
 * 该类是单类型通用adapter。如果需要支持多类型，需要扩展BaseAdapter的getItemViewType(int position)和
 * getViewTypeCount()两个方法。
 * <br>
 * <br>
 * 示例：http://blog.csdn.net/loveyaozu/article/details/52947808
 */

public abstract class CommonAdapter<T> extends BaseAdapter {

    private Context mContext = App.getContext();
    private List<T> data;

    public CommonAdapter(List<T> data){
        this.data = data != null?data:new ArrayList<T>();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position >= data.size()?null:data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    /**
     * 返回所有数据
     * @return
     */
    public List<T> getData(){
        return data;
    }

    /**
     * 在原有数据的基础上添加新的数据
     * @param data
     */
    public void addNewData(List<T> data){
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 替换原有的数据
     * @param data
     */
    public void replaceAll(List<T> data){
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 移除某个元素
     * @param element
     */
    public void removeElement(T element){
        this.data.remove(element);
        notifyDataSetChanged();
    }

    /**
     * 根据角标移除列表中的某个元素
     * @param indext
     */
    public void removeEByIndex(int indext){
        this.data.remove(indext);
        notifyDataSetChanged();
    }

    /**
     * 返回item布局的resId
     * 在子类中实现该方法，在方法中传入item的布局id
     * @return
     */
    public abstract int getItemViewResId();

    /**
     * 该方法主要是用来替换BaseAdapter的getView方法。
     * 所以，该方法需要在子类中实现。
     * 该方法主要是拿到ItemView布局文件中的每个控件的实例
     * @param position
     * @param convertView
     * @param viewHolder
     * @return
     */
    public abstract View getItemView(int position,View convertView,ViewHolder viewHolder);

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = View.inflate(mContext,getItemViewResId(),null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return getItemView(position,convertView,viewHolder);
    }
}
