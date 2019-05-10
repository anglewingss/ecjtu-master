package com.ecjtu.assistant.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseRecycleViewAdapter
 */
abstract class BaseRecycleViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public String TAG = "fdm";
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private List<T> mDatas = new ArrayList<>();

    private View mHeaderView;

    /**
     * 设置 RecyclerView 的 HeaderView
     *
     * @param headerView HeaderView
     */
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public void updateDatas(List<T> list) {

        mDatas = new ArrayList<>();
        addAdapterRangeItem(0, list.size(), list);
    }

    public void removeDatas() {
        removeAdapterRangeItem(0, mDatas.size());
    }

    /**
     * 该方法用于对 Adapter 的赋值操作
     *
     * @param datas datas
     */
    public void addDatas(List<T> datas) {
//        mDatas.addAll(datas);
        mDatas = datas;
        notifyDataSetChanged();
    }
    /**
     * 该方法用于对 Adapter 的赋值操作
     *
     */
    public void addData( T data) {
        mDatas.add(data);
        notifyDataSetChanged();
    }

    /**
     * 获取Adapter 的数据
     *
     * @return 数据
     */
    public List<T> getDatas() {
        return mDatas;
    }

    /**
     * 移除指定Item
     *
     * @param position position
     */
    public void removeAdapterItem(int position) {

        if (position >= mDatas.size()) {
            position = mDatas.size() - 1;
        }

        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 插入一条指定位置数据
     *
     * @param data       插入的数据
     * @param isLocation 是否指定插入的位置
     * @param position   location = true position 为指定插入的角标位置；location = false position 随意
     */
    public void addAdapterItem(T data, boolean isLocation, int position) {

        if (isLocation) {
            // position 插入的位置大于 size 将position赋予最大size
            if (position > mDatas.size()) {
                position = mDatas.size();
            }

            mDatas.add(position, data);
            notifyItemInserted(position);
        } else {
            mDatas.add(0, data);
            notifyItemInserted(0);
        }
    }

    /**
     * 插入一定范围数据 (随机)
     *
     * @param positions 插入位置的角标 数组
     * @param lists     插入的数据
     */
    public void addAdapterRandomRangeItem(int[] positions, List<T> lists) {

        for (int i = 0; i < positions.length; i++) {
            addAdapterItem(lists.get(i), true, positions[i]);
        }
    }

    /**
     * 删除一定范围的数据 (随机)
     *
     * @param positions 删除位置的角标 数据
     */
    public void removeAdapterRandomRangeItem(int[] positions) {

        for (int position : positions) {
            removeAdapterItem(position);
        }
    }

    /**
     * 插入一定范围数据 (顺序) 必须保证 count = data.size 否则插入数据以data.size为主
     *
     * @param startPosition 插入起始位置
     * @param count         插入个数
     * @param data          插入数据
     */
    public void addAdapterRangeItem(int startPosition, int count, List<T> data) {

        for (int i = 0; i < data.size(); i++) {
            mDatas.add(startPosition + i, data.get(i));
        }

        notifyItemRangeInserted(startPosition, data.size());
    }

    /**
     * 删除一定范围数据（顺序） 必须保证 删除起始位置 加上 删除个数 小于 data.size 否则删除个数为起始位置到data的末尾位置
     *
     * @param startPosition 删除起始位置
     * @param count         删除个数
     */
    public void removeAdapterRangeItem(int startPosition, int count) {

        if ((mDatas.size() - startPosition) < count) {
            count = mDatas.size() - startPosition;
        }

        for (int i = 0; i < count; i++) {
            mDatas.remove(startPosition);
        }

        notifyItemRangeRemoved(startPosition, count);
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) return TYPE_NORMAL;
        if (position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) return new Holder(mHeaderView);
        return onCreate(parent, viewType);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) return;

        final int pos = getRealPosition(viewHolder);
        final T data = mDatas.get(pos);
        onBind(viewHolder, pos, data);
    }

    /**
     * 重写该方法 让GridLayoutManager 添加的HeaderView 占据整行Item
     *
     * @param recyclerView RecyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEADER
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    /**
     * 重写改方法 让 StaggeredGridLayoutManager 添加 HeaderView 占据整行Item
     *
     * @param holder Holder
     */
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {

        super.onViewAttachedToWindow(holder);

        if (mHeaderView == null) {
            return;
        }

        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams && holder.getLayoutPosition() == 0) {

            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        } else {

            if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {

                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(false);
            }
        }
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();

        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mDatas.size() : mDatas.size() + 1;
    }

    /**
     * 创建Item的 Layout布局 并创建HolderView
     *
     * @param parent   ViewGroup
     * @param viewType viewType
     * @return ViewHolder
     */
    public abstract RecyclerView.ViewHolder onCreate(ViewGroup parent, final int viewType);

    /**
     * 对Item中的控件 进行复制操作
     *
     * @param viewHolder ViewHolder
     * @param position   position
     * @param data       data
     */
    public abstract void onBind(RecyclerView.ViewHolder viewHolder, int position, T data);

    /**
     * 让Adapter 重写该方法 并赋值 显示Item的点击事件
     *
     * @param myItemClickListener Listener
     */
    public abstract void setItemClickListener(BaseRecycleViewHolderView.MyItemClickListener myItemClickListener);

    public class Holder extends RecyclerView.ViewHolder {
        public Holder(View itemView) {
            super(itemView);
        }
    }

}
