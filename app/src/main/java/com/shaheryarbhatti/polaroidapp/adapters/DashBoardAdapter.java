package com.shaheryarbhatti.polaroidapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shaheryarbhatti.polaroidapp.R;

/**
 * Created by Shahzore on 12/16/2017.
 */


public class DashBoardAdapter extends RecyclerView.Adapter<DashBoardAdapter.DashBoardAdapterViewHolder> {


    private final Context mContext;


    public DashBoardAdapter(Context context) {
        this.mContext = context;

    }

    @Override
    public DashBoardAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dashboard, parent, false);
        return new DashBoardAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DashBoardAdapter.DashBoardAdapterViewHolder holder, int position) {
        holder.bind();

    }


    @Override
    public int getItemCount() {
        return 0;
    }


    class DashBoardAdapterViewHolder extends RecyclerView.ViewHolder {

        /* Views */
        private RelativeLayout layout_root;


        DashBoardAdapterViewHolder(View itemView) {
            super(itemView);

            layout_root = (RelativeLayout) itemView.findViewById(R.id.root_layout);


        }

        void bind() {


            layout_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });


        }
    }
}
