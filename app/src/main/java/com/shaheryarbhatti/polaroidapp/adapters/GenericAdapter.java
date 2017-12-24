package com.shaheryarbhatti.polaroidapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by shery on 9/16/2016.
 */
public abstract class GenericAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private static final String TAG = GenericAdapter.class.getSimpleName();
    protected Context context;
    private ArrayList<T> items;
    private int viewPositon;
    protected int selectedPosition = -1;


    public abstract RecyclerView.ViewHolder setViewHolder(ViewGroup parent);

    public abstract void onBindData(RecyclerView.ViewHolder holder, T val, int position);

    public abstract void onItemClick(View view);

    public GenericAdapter(Context context, ArrayList<T> items) {
        this.context = context;
        this.items = items;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder = setViewHolder(parent);

        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBindData(holder, getItem(position), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onClick(View view) {

        onItemClick(view);
    }

    public void addItems(ArrayList<T> items) {

        this.items = items;
        notifyDataSetChanged();
    }

    public T getItem(int position) {

        return items.get(position);
    }

    public ArrayList<T> getItemList() {
        return items;
    }


}
