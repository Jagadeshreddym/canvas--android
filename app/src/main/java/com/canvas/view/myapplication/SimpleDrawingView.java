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

import static android.graphics.Color.GRAY;

public class SimpleDrawingView  extends View {
    // setup initial color
    private final int paintColor = Color.WHITE;
    private Paint drawPaint;
    private Context context;
    String bitmapUrl = "";
    private int mStrokeWidth;
    private int mShadeStrokeWidth;
    private int mWidth;
    private int mHeight;
    private Bitmap downloadBitmap = null;
    private Bitmap mBitmap;
    private KidsEventScrollerItemDisplayType mDisplayType;
    private int mRectRadius;

    public enum KidsEventScrollerItemDisplayType {
        RECTANGLE, CIRCLE
    }

    public SimpleDrawingView(Context context, KidsEventScrollerItemDisplayType displayType, int width, int height, int strokeWidth, int shadeStrokeWidth, int rectRadius) {
        super(context);
        this.context = context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();
        mDisplayType = displayType;

        mStrokeWidth = strokeWidth;
        mShadeStrokeWidth = shadeStrokeWidth;

        mWidth = width;
        mHeight = height;
        mRectRadius = rectRadius;

    }


    // Setup paint with color and stroke styles
    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(mStrokeWidth);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));


        switch (mDisplayType) {
            case RECTANGLE: {
                // draw poster image
                mBitmap = getRoundedCornerBitmap(downloadBitmap, Color.WHITE, mRectRadius, mRectRadius, context);
                canvas.drawBitmap(mBitmap, mStrokeWidth, mStrokeWidth, null);
                // draw Shader
                final Paint paint1 = new Paint();
                final Rect rect1 = new Rect(mShadeStrokeWidth, (mShadeStrokeWidth * 2), mWidth + mShadeStrokeWidth, mHeight + mShadeStrokeWidth * 2);
                final RectF rectF1 = new RectF(rect1);
                paint1.setColor(Color.BLACK);
                paint1.setAlpha(90);
                paint1.setStyle(Paint.Style.STROKE);
                paint1.setStrokeWidth(mShadeStrokeWidth);
                canvas.drawRoundRect(rectF1, 50, 50, paint1);


                // draw border
                final Paint paint = new Paint();
                final Rect rect = new Rect(mStrokeWidth, mStrokeWidth, mWidth + mStrokeWidth, mHeight + mStrokeWidth);
                final RectF rectF = new RectF(rect);
                paint.setAntiAlias(true);
                paint.setDither(true);
                paint.setFilterBitmap(true);
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(mStrokeWidth);
                canvas.drawRoundRect(rectF, 50, 50, paint);

                break;
            }
            case CIRCLE:

            {

                mBitmap = getCircleBitmap(downloadBitmap);
                final Paint paint1 = new Paint();
                final Rect rect1 = new Rect(mShadeStrokeWidth, (mShadeStrokeWidth * 2), mWidth + mShadeStrokeWidth, mHeight + (mShadeStrokeWidth * 2));
                final RectF rectF1 = new RectF(rect1);
                paint1.setColor(Color.BLACK);
                paint1.setAlpha(90);
                paint1.setStyle(Paint.Style.STROKE);
                paint1.setStrokeWidth(mStrokeWidth);
                canvas.drawOval(rectF1, paint1);
                canvas.drawBitmap(mBitmap, mStrokeWidth, mStrokeWidth, null);

                // draw border
                final Paint paint = new Paint();
                final Rect rect = new Rect(mStrokeWidth, mStrokeWidth, (int) mWidth + mStrokeWidth, (int) mHeight + mStrokeWidth);
                final RectF rectF = new RectF(rect);
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(mStrokeWidth);
                canvas.drawOval(rectF, paint);


                break;
            }
        }

    }


    public Bitmap getRoundedCornerBitmap(Bitmap bitmap, int color, int cornerDips, int borderDips, Context context) {
        Bitmap output =null;
        try{
            if (bitmap != null) {
                output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            } else {
                output = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
            }
        }catch (OutOfMemoryError e){e.printStackTrace();}


        Canvas canvas = new Canvas(output);

        final int borderSizePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) borderDips,
                context.getResources().getDisplayMetrics());
        final int cornerSizePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) cornerDips,
                context.getResources().getDisplayMetrics());
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, (int) mWidth, (int) mHeight);
        final RectF rectF = new RectF(rect);


        // prepare canvas for transfer
        paint.setAntiAlias(true);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);

        // draw bitmap
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        final int height = output.getHeight();
        final int width = output.getWidth();
        float h = (float) height;
        float w = (float) width;
        Matrix mat = new Matrix();
        mat.setTranslate(output.getWidth(), output.getHeight());
        mat.setScale((mWidth) / w, (mHeight) / h);
        if (bitmap == null) {
            canvas.drawBitmap(output, mat, paint);
            invalidate();
        } else {
            canvas.drawBitmap(bitmap, mat, paint);
            bitmap.recycle();
        }

        return output;
    }

    public Bitmap getCircleBitmap(Bitmap bm) {
        Bitmap bitmap;
        Bitmap output;
        if (bm != null) {
            int sice = Math.min((bm.getWidth()), (bm.getHeight()));

            bitmap = ThumbnailUtils.extractThumbnail(bm, sice, sice);

            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        } else {
            output = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);

            int sice = Math.min((output.getWidth()), (output.getHeight()));

            bitmap = ThumbnailUtils.extractThumbnail(output, sice, sice);

        }
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, mWidth, mHeight);
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawOval(rectF, paint);


        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        final int height = bitmap.getHeight();
        final int width = bitmap.getWidth();
        float h = (float) height;
        float w = (float) width;
        Matrix mat = new Matrix();
        mat.setTranslate(output.getWidth(), output.getHeight());
        mat.setScale(mWidth / w, mHeight / h);
        canvas.drawBitmap(bitmap, mat, paint);

        if (bm == null) {
            canvas.drawBitmap(bitmap, mat, paint);
            invalidate();
        } else {
            canvas.drawBitmap(bitmap, mat, paint);
        }

        return output;
    }


    class CacheClearAsyncTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            URL imageurl = null;
            try {
                imageurl = new URL(getBitmapUrl());
                if(downloadBitmap!=null)
                {
                    downloadBitmap = null;
                }
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                downloadBitmap = BitmapFactory.decodeStream(imageurl.openConnection().getInputStream(),null, options);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

    public String getBitmapUrl() {
        return bitmapUrl;
    }

    public void setBitmapUrl(String bitmapUrl) {
        this.bitmapUrl = bitmapUrl;

      //  ImageLoader.getSharedInstance().loadImageFromUrlAsync(this, bitmapUrl, 0, 0, mBitmapImageLoaderListener);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new CacheClearAsyncTask().execute();
            }
        }, 0);
    }

  //  private final IImageLoaderListener mBitmapImageLoaderListener;

  /*  {
        mBitmapImageLoaderListener = new IImageLoaderListener() {
            @Override
            public void onImageLoaderComplete(final Object tag, final String url, final Bitmap bitmap) {
                downloadBitmap = bitmap;
                *//*if(downloadBitmap != null){
                    downloadBitmap.recycle();
                }*//*
            }

            @Override
            public void onImageLoaderFailed(final Object tag, final String url, final Exception error) {
                if (error != null) {
                    Logger.printStackTrace(error);
                }

            }

        };
    }*/
}