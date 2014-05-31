package com.grimnirdesigns.passdoodle_unsecure.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/*
This View acts as the drawing surface for PassDoodle. It features a visibly divided, square surface
(the default division is a 4x4 grid), that allows a user to draw a single-stroke doodle. Once a
single stroke has been drawn, the View must be cleared before another line can be drawn. A drawn
line can be extracted from the View in the form of an android.graphics.Path object.
 */
public class DrawingBoardView extends View {

    private Bitmap mCanvasBitmap;
    private Path mDrawPath;
    private Canvas mBoardCanvas;
    private Paint mDrawPaint, mCanvasPaint;
    private boolean mDrawingEnabled; //Allows or disallows drawing. Used to force one-stroke designs

    private final int GRID_SIZE = 4;

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

        mDrawingEnabled = true;
    }

    public void clearDrawing() {
        mCanvasBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mBoardCanvas = new Canvas(mCanvasBitmap);
        addGrid(mBoardCanvas);
        invalidate();
    }

    /*
    Add grid lines to a canvas to make replicating designs easier
    @param canvas       The Canvas the grid lines are to be added to
     */
    private void addGrid(Canvas canvas) {
        Paint gridPaint = new Paint();

        gridPaint.setColor(Color.BLACK);
        gridPaint.setStrokeWidth(1);

        int width = canvas.getWidth();

        int gridSquareEdge = width / GRID_SIZE;

        for (int i=1; i < GRID_SIZE; i++) {
            canvas.drawLine((float)gridSquareEdge*i, 0, (float)gridSquareEdge*i,(float)getHeight(), gridPaint);
            canvas.drawLine(0, (float)gridSquareEdge*i,(float)getWidth(), (float)gridSquareEdge*i, gridPaint);
        }

        float[] edgeLinePoints = {0, 1, getWidth()-1, 1,  //Top line
                                  getWidth()-1, 0, getWidth()-1, getHeight(), //Right line
                                  0, getHeight()-1, getWidth(), getHeight()-1, //Bottom line
                                  1, 0, 1, getHeight()-1}; //Left line

        canvas.drawLines(edgeLinePoints, gridPaint);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mCanvasBitmap, 0, 0, mCanvasPaint);
        canvas.drawPath(mDrawPath, mDrawPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mBoardCanvas = new Canvas(mCanvasBitmap);
        addGrid(mBoardCanvas);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        if (mDrawingEnabled) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDrawPath.moveTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mDrawPath.lineTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_UP:
                    mDrawingEnabled = false;
                    break;
                default:
                    return false;

            }
            invalidate();
        }
        return true;
    }

    /*
    Get the Path stored in mDrawPath.
    @return     The Path that stores the drawn shape
     */
    public Path getDrawPath() {
        return mDrawPath;}

    public void resetDrawPath() {
        mDrawPath.reset();
        mDrawingEnabled = true;
    }

    public boolean isDrawingEnabled() {
        return mDrawingEnabled;
    }
}
