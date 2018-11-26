package com.canvas.view.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import java.util.List;


public class kidsEventScrollerAdapter extends RecyclerView.Adapter<kidsEventScrollerAdapter.KidsItemViewHolder> {

    private RecyclerViewClickListener mListener;
    List<String> mDmEvent = null;
    Context mContext;
    Object mFilter;
    boolean mIsFullContent;

    public kidsEventScrollerAdapter(Context context, List<String> event, Object filter, boolean isFullContent, RecyclerViewClickListener listener) {
        mContext = context;
        this.mDmEvent = event;
        mListener = listener;
        mIsFullContent = isFullContent;

    }

    @Override
    public KidsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.kids_swimlane_item, null);*/

        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kids_swimlane_item,parent, false);

       // final SimpleDrawingView view = new SimpleDrawingView(mContext, SimpleDrawingView.KidsEventScrollerItemDisplayType.RECTANGLE, 200,300,10,10,20);
       // RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(MainActivity.width+(MainActivity.barderWidth*2) , MainActivity.height +(MainActivity.barderWidth*2) + MainActivity.maskWidth);
       // view.setLayoutParams(params);


        int mBarderWidth = 10;
        int mShadeWidth = 10;

        int mPosterWidth = 200;
        int mPosterHeight = 300;

        int mItemWidth = mPosterWidth + ((int)mBarderWidth * 2);
        int mItemHeight = mPosterHeight + ((int)mBarderWidth * 2) + (int)mShadeWidth;

        int mRectRadius = 20;

        SimpleDrawingView view = new SimpleDrawingView(mContext, SimpleDrawingView.KidsEventScrollerItemDisplayType.RECTANGLE, mPosterWidth, mPosterHeight, mBarderWidth, mShadeWidth, mRectRadius);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mItemWidth, mItemHeight);
        view.setLayoutParams(params);

        return new KidsItemViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(kidsEventScrollerAdapter.KidsItemViewHolder holder, int position) {

        String url = mDmEvent.get(position);
        holder.poster.setBitmapUrl(url);

    }

    @Override
    public int getItemCount() {
        return mDmEvent.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class KidsItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RecyclerViewClickListener mListener;

        private View itemView = null;
        private SimpleDrawingView poster = null;


        KidsItemViewHolder(final View View, RecyclerViewClickListener listener) {
            super(View);
            mListener = listener;
            itemView = View;
            poster = (SimpleDrawingView) View;/*(SimpleDrawingView)itemView.findViewById(R.id.kids_item_View);*/
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            mListener.onClick(view, mDmEvent.get(getAdapterPosition()));
        }

    }

    public interface RecyclerViewClickListener {

        void onClick(View view, Object eventData);
    }

}