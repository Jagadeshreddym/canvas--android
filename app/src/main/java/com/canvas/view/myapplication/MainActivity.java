package com.canvas.view.myapplication;

import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    SimpleDrawingView canvasView;
    public static int width = 500;
    public static int height = 500;
    public static int barderWidth = 10;
    public static int maskWidth = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        /*canvasView = new SimpleDrawingView(this);
        canvasView.setBitmapUrl("http://cdn-poster-bgllabs.vsscloud.in/VOD/2_3/shrek.jpg");*/

        LinearLayout layout = new LinearLayout(this);
        //  layout.addView(canvasView);

        RelativeLayout.LayoutParams params = null;
        RecyclerView swimLaneRecyclerView = new RecyclerView(this);
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        swimLaneRecyclerView.setLayoutParams(params);
        LinearLayoutManager mScrollerManager = new LinearLayoutManager(this);
        mScrollerManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        swimLaneRecyclerView.setLayoutManager(mScrollerManager);


        List<String> urlList = new ArrayList<String>();
        for (int i = 100; i > 1; i--) {
            urlList.add("http://cdn-poster-bgllabs.vsscloud.in/VOD/2_3/shrek.jpg");
            urlList.add("http://cdn-poster-bgllabs.vsscloud.in/VOD/16_9/Epic.jpg");
            urlList.add("http://cdn-poster-bgllabs.vsscloud.in/VOD/2_3/Epic.jpg");
            urlList.add("http://cdn-poster-bgllabs.vsscloud.in/VOD/2_3/shrek.jpg");
            urlList.add("http://cdn-poster-bgllabs.vsscloud.in/VOD/2_3/shrek.jpg");
            urlList.add("http://cdn-poster-bgllabs.vsscloud.in/VOD/2_3/shrek.jpg");
            urlList.add("http://cdn-poster-bgllabs.vsscloud.in/VOD/2_3/shrek.jpg");
            urlList.add("http://cdn-poster-bgllabs.vsscloud.in/VOD/2_3/shrek.jpg");
        }
        kidsEventScrollerAdapter adapter = new kidsEventScrollerAdapter(this, urlList, false, true, null);
        swimLaneRecyclerView.addItemDecoration(new RecyclerViewItemDecorator(70));
        swimLaneRecyclerView.setAdapter(adapter);

        layout.addView(swimLaneRecyclerView);
        layout.setBackgroundColor(Color.BLUE);
        setContentView(layout);
    }

    public class RecyclerViewItemDecorator extends RecyclerView.ItemDecoration {
        private int space;

        public RecyclerViewItemDecorator(int rightSpace) {
            this.space = rightSpace;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.right = space;

        }
    }
}
