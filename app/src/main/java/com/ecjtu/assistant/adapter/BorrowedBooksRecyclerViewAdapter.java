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
public class BorrowedBooksRecyclerViewAdapter extends BaseRecycleViewAdapter<Map<String, String>> {

    private BaseRecycleViewHolderView.MyItemClickListener myItemClickListener;

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new HolderView(LayoutInflater.from(parent.getContext()).inflate(R.layout.borrowed_book_item, parent, false), myItemClickListener);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int position, Map<String, String> data) {

        if (!(viewHolder instanceof HolderView)) {
            return;
        }

        ((HolderView) viewHolder).borrowedBookNameTextView.setText(data.get("1"));
        ((HolderView) viewHolder).borrowedBookCodeTextView.setText(data.get("0"));
        ((HolderView) viewHolder).borrowedBookAuthorTextView.setText(data.get("2"));
        ((HolderView) viewHolder).borrowedBookIndexTextView.setText(data.get("3"));
        ((HolderView) viewHolder).borrowedBookLocalTextView.setText(data.get("4"));
        ((HolderView) viewHolder).borrowedBookTypeTextView.setText(data.get("5"));
        ((HolderView) viewHolder).borrowedBookLendTimeTextView.setText(data.get("6"));
        ((HolderView) viewHolder).borrowedBookReturnTimeTextView.setText(data.get("7"));

    }

    @Override
    public void setItemClickListener(BaseRecycleViewHolderView.MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    private class HolderView extends BaseRecycleViewHolderView {

        private TextView borrowedBookNameTextView;
        private TextView borrowedBookCodeTextView;
        private TextView borrowedBookAuthorTextView;
        private TextView borrowedBookIndexTextView;
        private TextView borrowedBookLocalTextView;
        private TextView borrowedBookTypeTextView;
        private TextView borrowedBookLendTimeTextView;
        private TextView borrowedBookReturnTimeTextView;

        HolderView(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView, myItemClickListener);

            borrowedBookNameTextView = (TextView) itemView.findViewById(R.id.borrowed_book_name_textView);
            borrowedBookCodeTextView = (TextView) itemView.findViewById(R.id.book_code_textView);
            borrowedBookAuthorTextView = (TextView) itemView.findViewById(R.id.book_author_textView);
            borrowedBookIndexTextView = (TextView) itemView.findViewById(R.id.book_index_textView);
            borrowedBookLocalTextView = (TextView) itemView.findViewById(R.id.book_local_textView);
            borrowedBookTypeTextView = (TextView) itemView.findViewById(R.id.book_type_textView);
            borrowedBookLendTimeTextView = (TextView) itemView.findViewById(R.id.book_lend_time_textView);
            borrowedBookReturnTimeTextView = (TextView) itemView.findViewById(R.id.book_return_time_textView);

        }
    }
}
