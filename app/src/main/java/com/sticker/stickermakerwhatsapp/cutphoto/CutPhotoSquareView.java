package com.sticker.stickermakerwhatsapp.cutphoto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.sticker.stickermakerwhatsapp.R;

import java.util.ArrayList;
import java.util.List;

public class CutPhotoSquareView extends android.support.v7.widget.AppCompatImageView implements View.OnTouchListener {
    private Paint paint;
    public static List<Point> points;
    boolean flgPathDraw = true;
    Point mfirstpoint = null;
    boolean bfirstpoint = false;
    Point mlastpoint = null;
    private Matrix mmatrix;
    Context mContext;
    Bitmap bitmap;
    private float MAX_SCALE = 1.2f;
    private double halfDiagonalLength;
    private float oringinWidth = 0;
    private int mScreenwidth, mScreenHeight;
    private DisplayMetrics dm;
    private float MIN_SCALE = 0.5f;
    private PointF zoomPos;
    private boolean zooming = false;
    Bitmap result;
    private int left = 0, top = 0, right = 0, bot;

    public CutPhotoSquareView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public CutPhotoSquareView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{20, 20}, 0));
        paint.setStrokeWidth(10);
        paint.setColor(mContext.getResources().getColor(R.color.green));
        this.setOnTouchListener(this);
        points = new ArrayList<Point>();
        bfirstpoint = false;
        mmatrix = new Matrix();
        dm = getResources().getDisplayMetrics();
        mScreenwidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        zoomPos = new PointF(0, 0);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        setBitmap(bm);

    }

    private int leftDraw, topDraw, rightDraw, botDraw;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null) {
            if (top != 0 || left != 0 || right != 0 || bot != 0) {

                if (right <= left) {
                    leftDraw = right;
                    rightDraw = left;
                } else {
                    leftDraw = left;
                    rightDraw = right;
                }
                if (bot <= top) {
                    botDraw = top;
                    topDraw = bot;
                } else {
                    botDraw = bot;
                    topDraw = top;
                }

                canvas.drawRect(leftDraw, topDraw, rightDraw, botDraw, paint);
            }
        }
    }


    Point point;

    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                left = (int) event.getX();
                top = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (onActionCutSquare != null) {
                    onActionCutSquare.onActionDrawSquare(true);
                }
                point = new Point();
                point.x = (int) event.getX();
                point.y = (int) event.getY();

                right = (int) event.getX();
                bot = (int) event.getY();


                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (onActionCutSquare != null) {
                        onActionCutSquare.onActionDrawSquare(false);
                    }
                }
                break;
        }


        return true;
    }


    public Bitmap crop() {
//        Bitmap bitmap = Bitmap.createBitmap(rightDraw - leftDraw, botDraw - topDraw, Bitmap.Config.ARGB_8888);

        int left = leftDraw * bitmap.getWidth() / this.getWidth();
        int top = topDraw * bitmap.getHeight() / this.getHeight();
        int right = rightDraw * bitmap.getWidth() / this.getWidth();
        int bot = botDraw * bitmap.getHeight() / this.getHeight();

        Bitmap result = Bitmap.createBitmap(bitmap, left, top, right - left, bot - top);
        return result;
    }


    public void setBitmap(Bitmap bm) {

        mmatrix.reset();
        bitmap = bm;
        setDiagonalLength();
        initBitmaps();
        int w = bm.getWidth();
        int h = bm.getHeight();
        oringinWidth = w;
        float initScale = (MIN_SCALE + MAX_SCALE) / 2;
        mmatrix.postScale(initScale, initScale, w / 2, h / 2);
        mmatrix.postTranslate(mScreenwidth / 2 - w / 2, (mScreenwidth) / 2 - h / 2);
        invalidate();

        left = 0;
        top = 0;
        right = bm.getWidth();
        bot = bm.getHeight();
    }

    private void initBitmaps() {
        if (bitmap.getWidth() >= bitmap.getHeight()) {
            float minWidth = mScreenwidth / 8;
            if (bitmap.getWidth() < minWidth) {
                MIN_SCALE = 1f;
            } else {
                MIN_SCALE = 1.0f * minWidth / bitmap.getWidth();
            }

            if (bitmap.getWidth() > mScreenwidth) {
                MAX_SCALE = 1;
            } else {
                MAX_SCALE = 1.0f * mScreenwidth / bitmap.getWidth();
            }
        } else {
            float minHeight = mScreenwidth / 8;
            if (bitmap.getHeight() < minHeight) {
                MIN_SCALE = 1f;
            } else {
                MIN_SCALE = 1.0f * minHeight / bitmap.getHeight();
            }

            if (bitmap.getHeight() > mScreenwidth) {
                MAX_SCALE = 1;
            } else {
                MAX_SCALE = 1.0f * mScreenwidth / bitmap.getHeight();
            }
        }
    }

    private void setDiagonalLength() {
        halfDiagonalLength = Math.hypot(bitmap.getWidth(), bitmap.getHeight()) / 2;
    }


    public void resetView() {
        // paths.clear();
        bfirstpoint = false;
        points.clear();
        paint.setColor(mContext.getResources().getColor(R.color.green));
        paint.setStyle(Paint.Style.STROKE);
        flgPathDraw = true;
        invalidate();
    }


    public interface OnActionCutSquare {
        void onActionDrawSquare(Boolean b);
    }

    public OnActionCutSquare onActionCutSquare;

    public void setOnActionCutSquare(OnActionCutSquare onActionCutSquare) {
        this.onActionCutSquare = onActionCutSquare;
    }


}
