package com.dh.luckywheel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by dh on 16-8-10.
 */
public class LuckyWheel extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    private SurfaceHolder mHolder;
    private Canvas mCanvas;

    private Thread thread;
    private boolean isRunning;

    /*与图片对应的Bitmap数组*/
    private Bitmap[] mImgsBitmap;
    /*奖项文字、图片、颜色*/
    private String[] mStrs = new String[6];
    //private int[] mImgs = new int[]{R.mipmap.danfan, R.mipmap.ipad,
       //     R.mipmap.f015, R.mipmap.iphone, R.mipmap.meizi, R.mipmap.f040};
    private int[] mColors = new int[]{0xFFFFC300, 0xFFF17E01, 0xFFFFC300, 0XFFF17E01, 0xFFFFC300, 0xFFF17E01};

    private int mItemCount = 6;

    /*整体盘块范围*/
    private RectF mRange = new RectF();
    //盘块直径
    private int mRadius;

    private Paint mArcPaint;
    private Paint mTextPaint;

    private double mSpeed; //旋转速度
    private volatile float mStartAngle = 0; //绘制角度 volatile保证变量在线程间的可见性

    private boolean isShouldEnd;

    private int mCenter;

    /*padding以paddingleft为准*/
    private int mPadding;

    private Bitmap mBgBitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.bg2);
    private float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,20,
            getResources().getDisplayMetrics());


    public LuckyWheel(Context context) {
        this(context, null);
    }

    public LuckyWheel(Context context, AttributeSet attrs) {
        super(context, attrs);

        mHolder = getHolder();

        mHolder.addCallback(this);

        setFocusable(true); //可获得焦点
        setFocusableInTouchMode(true);
        setKeepScreenOn(true); //设置常亮

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());

        mPadding = getPaddingLeft();
        mRadius = width - mPadding*2;

        mCenter = width/2;

        setMeasuredDimension(width, width);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setTextSize(mTextSize);

        mRange = new RectF(mPadding, mPadding, mPadding+mRadius, mPadding+mRadius);

        mImgsBitmap = new Bitmap[mItemCount];

        /*for(int i = 0; i < mItemCount; i++){
            mImgsBitmap[i] = BitmapFactory.decodeResource(getResources(),mImgs[i]);
        }*/



        isRunning = true;
        thread = new Thread(this);
        thread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning){

            long start = System.currentTimeMillis();

            draw();

            long end = System.currentTimeMillis();

            if(end - start < 50){
                try {
                    Thread.sleep(50 - (end - start));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
            if(mCanvas != null){


                drawBg();

                float tmpAngle = mStartAngle;
                float sweepAngle = 360/mItemCount;

                for(int i = 0; i < mItemCount; i++){
                    mArcPaint.setColor(mColors[i]);

                    mCanvas.drawArc(mRange,tmpAngle,sweepAngle,true,mArcPaint);
                    
                    drawText(tmpAngle, sweepAngle, mStrs[i]);

                    
                    tmpAngle += sweepAngle;
                }

                mStartAngle += mSpeed;

                if(isShouldEnd){

                    mSpeed -= 1;

                }
                if(mSpeed <= 0){
                    mSpeed = 0;
                    isShouldEnd = false;
                }

            }
        }catch (Exception e) {
        }finally {
            if(mCanvas != null){
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }

    }

    public void luckyStart(int index){

        float angle = 360 / mItemCount;

        float from = 270 - (index + 1) * angle;
        float end = from + angle;

        //停下来的范围
        float targetFrom = 4 * 360 + from;
        float targetEnd = 4 * 360 + end;

        float v1 = (float) ((-1 + Math.sqrt(1+8*targetFrom))/2);
        float v2 = (float) ((-1 + Math.sqrt(1+8*targetEnd))/2);

        mSpeed = v1 + Math.random()*(v2 - v1);

        isShouldEnd = false;
    }

    public void luckyEnd(){
        mStartAngle = 0;
        isShouldEnd = true;
    }

    public boolean isStart(){
        return mSpeed != 0;
    }

    public boolean isShouldEnd(){
        return isShouldEnd;
    }

    /**
    private void drawIcon(float tmpAngle, Bitmap bitmap) {

        //设置图片宽度为直径的1/8
        int imgWidth = mRadius / 8;

        float angle = (float) ((tmpAngle + 360 / mItemCount / 2) * Math.PI / 180);

        int x = (int) (mCenter + mRadius / 2 / 2 * Math.cos(angle));
        int y = (int) (mCenter + mRadius / 2 / 2 * Math.sin(angle));

        Rect rect = new Rect(x - imgWidth/2, y - imgWidth/2, x + imgWidth/2, y + imgWidth/2);
        mCanvas.drawBitmap(bitmap,null,rect,null);


    }*/

    private void drawText(float tmpAngle, float sweepAngle, String mStr) {

        Path path = new Path();
        path.addArc(mRange,tmpAngle,sweepAngle);


        /*水平偏移量让文字居中*/
        float textWidth = mTextPaint.measureText(mStr);
        int hOffset = (int) (mRadius*Math.PI/mItemCount/2-textWidth/2);

        int vOffset = mRadius/2/6;//垂直偏移量

        mCanvas.drawTextOnPath(mStr,path,hOffset,vOffset,mTextPaint);

    }

    private void drawBg() {

        mCanvas.drawColor(0xffffffff);
        mCanvas.drawBitmap(mBgBitmap, null, new Rect(mPadding/2,mPadding/2,
                getMeasuredWidth()-mPadding/2,getMeasuredHeight()-mPadding/2),null);

    }

    public void putItems(String[] items){
        for(int i = 0; i < items.length; i++){
            mStrs[i] = items[i];
        }
    }
}
