package com.app.court.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.app.court.interfaces.OnViewHolderClick;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RecyclerViewListAdapter<T>
        extends RecyclerView.Adapter<RecyclerViewListAdapter.RecyclerviewViewHolder> {
    private List<T> mList = Collections.emptyList();
    private Context mContext;
    private OnViewHolderClick mListener;

    public static class RecyclerviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Map<Integer, View> mMapView;
        private OnViewHolderClick mListener;

        public RecyclerviewViewHolder(View view, OnViewHolderClick listener) {
            super(view);
            mMapView = new HashMap<>();
            mMapView.put(0, view);
            mListener = listener;

            if (mListener != null)
                view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null)
                mListener.onItemClick(view, getAdapterPosition());
        }

        public void initViewList(int[] idList) {
            for (int id : idList)
                initViewById(id);
        }

        public void initViewById(int id) {
            View view = (getView() != null ? getView().findViewById(id) : null);

            if (view != null)
                mMapView.put(id, view);
        }

        public View getView() {
            return getView(0);
        }

        public View getView(int id) {
            if (mMapView.containsKey(id))
                return mMapView.get(id);
            else
                initViewById(id);

            return mMapView.get(id);
        }
    }

    protected abstract View createView(Context context, ViewGroup viewGroup, int viewType);

    protected abstract void bindView(T item, RecyclerviewViewHolder viewHolder);

    protected abstract int bindItemViewType(int position);

    protected abstract int bindItemId(int position);

    public void clearList() {
        mList.clear();
        notifyDataSetChanged();
    }

    public RecyclerViewListAdapter(Context context) {
        this(context, null);
    }

    public RecyclerViewListAdapter(Context context, OnViewHolderClick listener) {
        super();
        mContext = context;
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return bindItemViewType(position);
    }

    @Override
    public RecyclerviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new RecyclerviewViewHolder(createView(mContext, viewGroup, viewType), mListener);
    }

    @Override
    public void onBindViewHolder(RecyclerviewViewHolder viewHolder, int position) {
        bindView(getItem(position), viewHolder);
    }

    @Override
    public long getItemId(int position) {
        return bindItemId(position);
    }

    @Override
    public int getItemCount() {
        if (mList == null) return 0;
        return mList.size();
    }

    public T getItem(int index) {
        if (mList==null || mList.size()<1) return null;
        return ((index < mList.size()) ? mList.get(index) : null);
    }

    public Context getContext() {
        return mContext;
    }

    public void addAll(List<T> list) {
        if (list == null) return;
        mList = list;
        notifyDataSetChanged();
    }

    public void updateItem(int position, T entity) {
        mList.set(position, entity);
        notifyItemChanged(position);
    }

    public void removeItem(int index) {
        mList.remove(index);
        notifyItemRemoved(index);
    }

    public void removeItem(T entity) {
        mList.remove(entity);
        notifyDataSetChanged();
    }

    public List<T> getList() {
        return mList;
    }

    public void setClickListener(OnViewHolderClick listener) {
        mListener = listener;
    }
}