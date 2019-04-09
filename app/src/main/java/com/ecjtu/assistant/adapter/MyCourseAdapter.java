package com.ecjtu.assistant.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecjtu.assistant.R;
import com.ecjtu.assistant.activity.MyCourseActivity;
import com.ecjtu.assistant.db.CourseDb;


/**
 */
public class MyCourseAdapter extends BaseRecycleViewAdapter<CourseDb.Record> {


    private BaseRecycleViewHolderView.MyItemClickListener myItemClickListener;
    private MyCourseActivity courseActivity;

    public MyCourseAdapter(MyCourseActivity courseActivity) {
        this.courseActivity = courseActivity;
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new HolderView(LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false), myItemClickListener);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int position, final CourseDb.Record data) {

        if (!(viewHolder instanceof HolderView)) {
            return;
        }

        ((HolderView) viewHolder).name.setText(data.courseName);
        ((HolderView) viewHolder).type.setText( data.courseType);
        ((HolderView) viewHolder).num.setText(data.courseScore );
        ((HolderView) viewHolder).time.setText(data. courseTime);
        ((HolderView) viewHolder).way.setText(data.courseTestWay);
        ((HolderView) viewHolder).select.setText("退选");

        ((HolderView) viewHolder).select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                courseActivity.unSelect(data);//

            }
        });

    }

    @Override
    public void setItemClickListener(BaseRecycleViewHolderView.MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    private class HolderView extends BaseRecycleViewHolderView {

        private TextView num;
        private TextView  time;
        private TextView type;
        private TextView name;
        private TextView way;
        private TextView select;

        HolderView(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView, myItemClickListener);

            time = (TextView) itemView.findViewById(R.id.time);
            num = (TextView) itemView.findViewById(R.id.num);
            type = (TextView) itemView.findViewById(R.id.type);
            name = (TextView) itemView.findViewById(R.id.name);
            way = (TextView) itemView.findViewById(R.id.way);
            select = (TextView) itemView.findViewById(R.id.select);

        }
    }
}
