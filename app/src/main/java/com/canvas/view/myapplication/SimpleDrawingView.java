package com.canvas.view.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.canvas.view.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class SimpleDrawingView extends View {
    // setup initial color
    private final int paintColor = Color.WHITE;
    // defines paint and canvas
    private Paint drawPaint;
    private Context context;
    String bitmapUrl = "";
    /*private float bitmapHeight = 800;
    private float bitmapWidth = 400;*/
    private int rectHeight = 1000;
    private int rectWidth = 600;
    private float strokeWidth = 30;
    private int circularHeight = 500;
    private int circularWidth = 500;

    private boolean isRectangle = true;
    private boolean isCircle = false;
    private Bitmap downloadBitmap = null;
    private Bitmap mBitmap;

    public SimpleDrawingView(Context context) {
        super(context);
        this.context = context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();
    }

    public SimpleDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();
    }

    // Setup paint with color and stroke styles
    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(strokeWidth);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        Resources res = getResources();
      //  downloadBitmap = BitmapFactory.decodeResource(res, R.drawable.main_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new CacheClearAsyncTask().execute();
            }
        },1000);


    }

    @Override
    protected void onDraw(Canvas canvas) {

        drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        invalidate();
        if (downloadBitmap!=null)
        {
            if(isRectangle)
            {
                mBitmap = getRoundedCornerBitmap(downloadBitmap, Color.WHITE, 20,20,context);
                canvas.drawBitmap(mBitmap, 15, 15, null);
                final Paint paint1 = new Paint();
                final Rect rect1 = new Rect(25, 65, (int)MainActivity.width, (int)MainActivity.height+30);
                final RectF rectF1 = new RectF(rect1);
                paint1.setColor(Color.BLACK);
                paint1.setAlpha(10);
                paint1.setStyle(Paint.Style.STROKE);
                paint1.setStrokeWidth(strokeWidth);
                canvas.drawRoundRect(rectF1, 50, 50, paint1);


                // draw border
                final Paint paint = new Paint();
                final Rect rect = new Rect(25, 25, (int)MainActivity.width, (int)MainActivity.height);
                final RectF rectF = new RectF(rect);
                paint.setAntiAlias(true);
                paint.setDither(true);
                paint.setFilterBitmap(true);
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(strokeWidth);
                canvas.drawRoundRect(rectF, 50, 50, paint);
            }

            if(isCircle)
            {
                mBitmap = getCircleBitmap(downloadBitmap);
                final Paint paint1 = new Paint();
                final Rect rect1 = new Rect(30, 70, circularWidth, circularHeight+40);
                final RectF rectF1 = new RectF(rect1);
                paint1.setColor(Color.BLACK);
                paint1.setAlpha(10);
                paint1.setStyle(Paint.Style.STROKE);
                paint1.setStrokeWidth(strokeWidth);
                canvas.drawOval(rectF1, paint1);

                canvas.drawBitmap(mBitmap, 10, 10, null);

                // draw border
                final Paint paint = new Paint();
                final Rect rect = new Rect(30, 30, circularWidth, circularHeight);
                final RectF rectF = new RectF(rect);
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(strokeWidth);
                canvas.drawOval(rectF, paint);
            }
        }


    }


    public  Bitmap getRoundedCornerBitmap(Bitmap bitmap, int color, int cornerDips, int borderDips, Context context) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),Bitmap.Config.ARGB_8888);
        //Bitmap output    = Bitmap.createScaledBitmap(bitmap, (int)MainActivity.width-30, (int)MainActivity.height-30, true);
        Canvas canvas = new Canvas(output);

        final int borderSizePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) borderDips,
                context.getResources().getDisplayMetrics());
        final int cornerSizePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) cornerDips,
                context.getResources().getDisplayMetrics());
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, (int)MainActivity.width, (int)MainActivity.height);
        final RectF rectF = new RectF(rect);


        // prepare canvas for transfer
        paint.setAntiAlias(true);
        paint.setColor(0xFFFFFFFF);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);

        // draw bitmap
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        final int height= output.getHeight() ;
        final int width= output.getWidth();
        float h= (float) height;
        float w= (float) width;
        Matrix mat=new Matrix();
        mat.setTranslate( output.getWidth(), output.getHeight() );
        mat.setScale((MainActivity.width)/w ,(MainActivity.height)/h);
        canvas.drawBitmap(bitmap, mat, paint);
        return output;
    }

    public  Bitmap getCircleBitmap(Bitmap bm) {

        int sice = Math.min((bm.getWidth()), (bm.getHeight()));

        Bitmap bitmap = ThumbnailUtils.extractThumbnail(bm, sice, sice);

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final int color = 0xffff0000;
        final Paint paint = new Paint();
        final Rect rect = new Rect(20, 20, circularWidth, circularHeight);
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) 20);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        final int height= bitmap.getHeight() ;
        final int width= bitmap.getWidth();
        float h= (float) height;
        float w= (float) width;
        Matrix mat=new Matrix();
        mat.setTranslate( circularWidth, circularHeight );
        mat.setScale(circularWidth/w ,circularHeight/h);
        canvas.drawBitmap(bitmap, mat, paint);

        return output;
    }



    class CacheClearAsyncTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            URL imageurl = null;
            try {
                imageurl = new URL(getBitmapUrl());
                downloadBitmap = BitmapFactory.decodeStream(imageurl.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute (Void result)    {
            /*Glide.get(getActivity()).clearMemory();*/
        }
    }

    public String getBitmapUrl() {
        return bitmapUrl;
    }

    public void setBitmapUrl(String bitmapUrl) {
        this.bitmapUrl = bitmapUrl;
    }
}