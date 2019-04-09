package com.ecjtu.assistant.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecjtu.assistant.R;
import com.ecjtu.assistant.db.MyBookDb;
import com.ecjtu.assistant.utils.TimeUtils;

/**
 */
public class OverTimeBooksAdapter extends BaseRecycleViewAdapter<MyBookDb.Record> {

    private BaseRecycleViewHolderView.MyItemClickListener myItemClickListener;

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new HolderView(LayoutInflater.from(parent.getContext()).inflate(R.layout.overtime_book_item, parent, false), myItemClickListener);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int position, MyBookDb.Record data) {

        if (!(viewHolder instanceof HolderView)) {
            return;
        }

        ((HolderView) viewHolder).title.setText(data.title );
        ((HolderView) viewHolder).address.setText("馆藏地点："+data. address);
        int day=(-TimeUtils.getGapCount(data. backtime) );
        (( HolderView) viewHolder).day.setText(day +"天");
        ((HolderView) viewHolder).num.setText(data. num+" 续借");
        ((HolderView) viewHolder).backtime.setText(data. backtime);
        ((HolderView) viewHolder).borrowtime.setText(data. borrowtime);
        ((HolderView) viewHolder).price.setText((day*0.5)+"元");

    }

    @Override
    public void setItemClickListener(BaseRecycleViewHolderView.MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    private class HolderView extends BaseRecycleViewHolderView {

        private TextView day;
        private TextView num;
        private TextView backtime;
        private TextView borrowtime;
        private TextView price;
        private TextView title;
        private TextView address;

        HolderView(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView, myItemClickListener);

            title = (TextView) itemView.findViewById(R.id.title);
            address = (TextView) itemView.findViewById(R.id.address);
            day = (TextView) itemView.findViewById(R.id.day);
            num = (TextView) itemView.findViewById(R.id.num);
            backtime = (TextView) itemView.findViewById(R.id.backtime);
            borrowtime = (TextView) itemView.findViewById(R.id.borrowtime);
            price = (TextView) itemView.findViewById(R.id.price);

        }
    }
}
