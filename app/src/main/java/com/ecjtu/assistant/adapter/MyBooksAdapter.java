package com.ecjtu.assistant.adapter;

import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecjtu.assistant.R;
import com.ecjtu.assistant.activity.MyBookActivity;
import com.ecjtu.assistant.db.MyBookDb;
import com.ecjtu.assistant.utils.DialogUtil;
import com.ecjtu.assistant.utils.TimeUtils;

/**
 */
public class MyBooksAdapter extends BaseRecycleViewAdapter<MyBookDb.Record> {


    private BaseRecycleViewHolderView.MyItemClickListener myItemClickListener;
    private MyBookActivity myBookActivity;

    public MyBooksAdapter(MyBookActivity myBookActivity) {
        this.myBookActivity = myBookActivity;
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new HolderView(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_book_item, parent, false), myItemClickListener);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int position, final MyBookDb.Record data) {

        if (!(viewHolder instanceof HolderView)) {
            return;
        }

        ((HolderView) viewHolder).title.setText(data.title);
        ((HolderView) viewHolder).address.setText("馆藏地点：" + data.address);

        int day = TimeUtils.getGapCount(data.backtime);
        ((HolderView) viewHolder).day.setText(day + "天");
        ((HolderView) viewHolder).num.setText(data.num + " 续借");
        ((HolderView) viewHolder).backtime.setText(data.backtime);
        ((HolderView) viewHolder).borrowtime.setText(data.borrowtime);
        if (day < 0) {
            ((HolderView) viewHolder).xujie.setVisibility(View.GONE);
        } else {
            ((HolderView) viewHolder).xujie.setVisibility(View.VISIBLE);
        }
        ((HolderView) viewHolder).xujie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogUtil.show2Btn(myBookActivity, "提示", "确认续借30天？"
                        , "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    data.backtime = TimeUtils.add30Days(data.backtime);
                                    int n = Integer.parseInt(data.num);
                                    data.num = (n + 1) + "";
                                    myBookActivity.xujie(data);//续借
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        , "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            }
        });

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
        private TextView title;
        private TextView address;
        private TextView xujie;

        HolderView(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView, myItemClickListener);

            title = (TextView) itemView.findViewById(R.id.title);
            xujie = (TextView) itemView.findViewById(R.id.xujie);
            address = (TextView) itemView.findViewById(R.id.address);
            day = (TextView) itemView.findViewById(R.id.day);
            num = (TextView) itemView.findViewById(R.id.num);
            backtime = (TextView) itemView.findViewById(R.id.backtime);
            borrowtime = (TextView) itemView.findViewById(R.id.borrowtime);

        }
    }
}
