package com.grimnirdesigns.passdoodle_unsecure.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;


/**
 * TODO: document your custom view class.
 */
public class DrawingBoardView extends View {

    Bitmap mCanvasBitmap;
    Path mDrawPath;
    Canvas mBoardCanvas;
    Paint mDrawPaint, mCanvasPaint;

    public DrawingBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mDrawPath = new Path();
        mDrawPaint = new Paint();

        mDrawPaint.setColor(Color.BLUE);

        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setStrokeWidth(20);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);

        mCanvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    public void clearDrawing() {
        mCanvasBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mBoardCanvas = new Canvas(mCanvasBitmap);
        addGrid(mBoardCanvas);
        invalidate();
    }

    private void addGrid(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        //TODO
    }

    @Override
    protected void onDraw(Canvas canvas) {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mBoardCanvas = new Canvas(mCanvasBitmap);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heightWithoutPadding = height - getPaddingTop() - getPaddingBottom();

        if (widthWithoutPadding > heightWithoutPadding) {
            width = heightWithoutPadding;
        } else {
            height = widthWithoutPadding;
        }

        setMeasuredDimension(width, height);
    }

}
