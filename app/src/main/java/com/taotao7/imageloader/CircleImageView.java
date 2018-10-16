package com.taotao7.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 一个简洁和高效的圆形图片
 */

public class CircleImageView extends ImageView {

    //用户设置的图片
    private Bitmap mBitmap;
    private Paint mPaint;
    private float width;
    private float height;
    private float radius;
    private Matrix matrix;


    public CircleImageView(Context context) {
        this(context,null);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    private void initData() {
        mPaint = new Paint();
        //设置抗锯齿
        mPaint.setAntiAlias(true);
        //初始化缩放矩阵
        matrix = new Matrix();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        radius = Math.min(width,height)/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setShader(initBitmapShader());
        canvas.drawCircle(width/2,height/2,radius,mPaint);
    }

    private Shader initBitmapShader() {
        mBitmap = ((BitmapDrawable)getDrawable()).getBitmap();
        BitmapShader bitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = Math.max(width/mBitmap.getWidth(),height/mBitmap.getHeight());
        matrix.setScale(scale,scale);
        bitmapShader.setLocalMatrix(matrix);
        return bitmapShader;
    }
}
