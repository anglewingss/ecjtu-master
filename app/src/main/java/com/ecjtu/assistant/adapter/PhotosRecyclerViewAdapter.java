package com.ecjtu.assistant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ecjtu.assistant.R;
import com.ecjtu.assistant.db.BannerDb;
import com.ecjtu.assistant.db.RecordDb;
import com.ecjtu.assistant.utils.StringUtils;

/**
 */
public class PhotosRecyclerViewAdapter extends BaseRecycleViewAdapter<BannerDb.Record> {

    private BaseRecycleViewHolderView.MyItemClickListener myItemClickListener;
    private Context context;
    private String TAG = "fdm";

    public PhotosRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new HolderView(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_photos, parent, false), myItemClickListener);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int position, BannerDb.Record data) {

        if (!(viewHolder instanceof HolderView)) {
            return;
        }
        ((HolderView) viewHolder).tv_title.setText(data.title);

        if (StringUtils.isFine(data.imgLink)) {
            ((HolderView) viewHolder).iv_photo.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(data.imgLink)
                    .thumbnail(0.1f)
                    .centerCrop()
                    .into(((HolderView) viewHolder).iv_photo);
        } else {
            ((HolderView) viewHolder).iv_photo.setVisibility(View.GONE);
        }
    }

    @Override
    public void setItemClickListener(BaseRecycleViewHolderView.MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    private class HolderView extends BaseRecycleViewHolderView {

        private ImageView iv_photo;
        private TextView tv_title;

        HolderView(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView, myItemClickListener);

            iv_photo = (ImageView) itemView.findViewById(R.id.iv_pic);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);

        }
    }
}
