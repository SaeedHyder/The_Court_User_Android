package com.app.court.ui.viewbinders.abstracts;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public abstract class ExpandableListViewBinder<T,E> {

    int mLayoutGroupId;
    int mLayoutChildId;

    public ExpandableListViewBinder() {

    }

    public ExpandableListViewBinder(int layoutGroupId,int layoutChildId) {
        mLayoutGroupId = layoutGroupId;
        mLayoutChildId = layoutChildId;
    }

    public View createGroupView(Activity activity) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(mLayoutGroupId, null);
        view.setTag(createGroupViewHolder(view));
        return view;

    }

    public View createChildView(Activity activity) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(mLayoutChildId, null);
        view.setTag(createChildViewHolder(view));
        return view;

    }

    public abstract BaseGroupViewHolder createGroupViewHolder(View view);
    public abstract BaseChildViewHolder createChildViewHolder(View view);

    /**
     * @param entity
     * @param position
     * @param grpPosition In cases applicable, for e.g in expandable listview
     * @param view
     * @param activity
     */
    public abstract void bindGroupView(T entity, int position, int grpPosition, View view, Activity activity);
    public abstract void bindChildView(E entity, int position, int grpPosition, View view, Activity activity);

    protected static class BaseGroupViewHolder {

    }

    protected static class BaseChildViewHolder {

    }
}

