package com.ecjtu.assistant.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecjtu.assistant.R;

import java.util.Map;

/**
 */
public class LibraryRecyclerViewAdapter extends BaseRecycleViewAdapter<Map<String, String>> {

    private BaseRecycleViewHolderView.MyItemClickListener myItemClickListener;

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new HolderView(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_library_hot_item, parent, false), myItemClickListener);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, final int position, final Map<String, String> data) {

        if (!(viewHolder instanceof HolderView)) {
            return;
        }
        ((HolderView) viewHolder).bookNameTextView.setText(data.get("bookName"));

    }

    @Override
    public void setItemClickListener(BaseRecycleViewHolderView.MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public class HolderView extends BaseRecycleViewHolderView {

        private TextView bookNameTextView;

        public HolderView(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView, myItemClickListener);

            bookNameTextView = (TextView) itemView.findViewById(R.id.bookName_textView);
        }
    }
}
