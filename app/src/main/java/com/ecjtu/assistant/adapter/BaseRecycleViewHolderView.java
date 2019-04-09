package com.ecjtu.assistant.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * BaseRecycleViewHolderView.
 */

public class BaseRecycleViewHolderView extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public String TAG = "fdm";
    private MyItemClickListener myItemClickListener;
    private MyItemLongClickListener myItemLongClickListener;

    public BaseRecycleViewHolderView(View itemView, MyItemClickListener myItemClickListener) {
        super(itemView);

        this.myItemClickListener = myItemClickListener;
        itemView.setOnClickListener(this);
    }

    public BaseRecycleViewHolderView(View itemView, MyItemClickListener myItemClickListener, MyItemLongClickListener myItemLongClickListener) {
        super(itemView);

        this.myItemClickListener = myItemClickListener;
        this.myItemLongClickListener = myItemLongClickListener;

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (myItemClickListener != null) {
            myItemClickListener.onItemClick(v, getLayoutPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {

        if (myItemLongClickListener != null) {
            myItemLongClickListener.onItemLongClick(v, getLayoutPosition());
        }
        return true;
    }

    public interface MyItemClickListener {
        public void onItemClick(View view, int position);
    }

    public interface MyItemLongClickListener {
        public void onItemLongClick(View view, int position);
    }
}
