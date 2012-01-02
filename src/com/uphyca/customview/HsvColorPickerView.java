/*
 * Copyright (C) 2011 The yanzm Custom View Project
 *      Yuki Anzai, uPhyca Inc.
 *      http://www.uphyca.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.uphyca.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class HsvColorPickerView extends View {

    public interface OnColorChangedListener {
        void onColorChanged(int color, float[] hsv);
    }

    public void setOnColorChangedListener(OnColorChangedListener l) {
        mListener = l;
    }

    /**
     * 初期カラーをセットする
     * 
     * @param color
     */
    public void setIniticalColor(int color) {
        mCurrentColor = color;
        Color.colorToHSV(mCurrentColor, mCurrentHSV);
        invalidate();
    }

    /**
     * 現在の色を返す
     * 
     * @return
     */
    public int getCurrentColor() {
        return mCurrentColor;
    }

    /**
     * プレビューを表示するかどうか
     * 
     * @param show
     */
    public void showPreview(boolean show) {
        mIsShowPreview = show;
        updateRectArea();
    }

    /**
     * 色領域の幅をセットする
     * 
     * @param width
     */
    public void setRectWidth(int width) {
        mRectWidth = width;
        updateRectArea();
    }

    /**
     * 色領域の高さをセットする
     * 
     * @param height
     */
    public void setRectHeight(int height) {
        mRectHeight = height;
        updateRectArea();
    }

    /**
     * 色領域間のギャップをセットする
     * 
     * @param gap
     */
    public void setRectGap(int gap) {
        mRectGap = gap;
        updateRectArea();
    }

    private static final int RECT_WIDTH = 400;
    private static final int RECT_HEIGHT = 80;
    private static final int RECT_GAP = 20;

    private boolean mIsShowPreview = true;

    private int mRectWidth = RECT_WIDTH;
    private int mRectHeight = RECT_HEIGHT;
    private int mRectGap = RECT_GAP;

    private OnColorChangedListener mListener;

    private int mCurrentColor = Color.RED;
    private float[] mCurrentHSV = new float[3];

    private Shader mHueShader;
    private Shader mSaturationShader;
    private Shader mValueShader;

    private Paint mHuePaint;
    private Paint mValuePaint;
    private Paint mSaturationPaint;
    private Paint mPreviewPaint;
    private Paint mLinePaint;

    private Rect mHueRect;
    private Rect mSaturationRect;
    private Rect mValueRect;
    private Rect mPreviewRect;

    int[] mHueList;

    public HsvColorPickerView(Context c) {
        super(c);
        init();
    }

    public HsvColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        Color.colorToHSV(mCurrentColor, mCurrentHSV);

        mHueList = new int[10];
        float hue = 0;
        for (int i = 0; i < 10; i++) {
            mHueList[i] = setHSVColor(hue, 255, 255);
            hue += 36;
        }

        mHuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHuePaint.setStyle(Paint.Style.FILL);
        mHuePaint.setStrokeWidth(2);

        mValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mValuePaint.setStyle(Paint.Style.FILL);
        mValuePaint.setStrokeWidth(2);

        mSaturationPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSaturationPaint.setStyle(Paint.Style.FILL);
        mSaturationPaint.setStrokeWidth(2);

        mPreviewPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPreviewPaint.setStyle(Paint.Style.FILL);
        mPreviewPaint.setStrokeWidth(5);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setStrokeWidth(5);
        mLinePaint.setColor(Color.BLACK);

        updateRectArea();
    }

    private void updateRectArea() {
        mHueShader = new LinearGradient(0, 0, mRectWidth, 0, mHueList, null, Shader.TileMode.CLAMP);
        mHuePaint.setShader(mHueShader);

        if (mIsShowPreview) {
            mPreviewRect = new Rect(0, 0, mRectWidth, mRectHeight);
            mHueRect = new Rect(0, mRectHeight + mRectGap, mRectWidth, mRectHeight * 2 + mRectGap);
            mSaturationRect = new Rect(0, mRectHeight * 2 + mRectGap * 2, mRectWidth, mRectHeight * 3 + mRectGap * 2);
            mValueRect = new Rect(0, mRectHeight * 3 + mRectGap * 3, mRectWidth, mRectHeight * 4 + mRectGap * 3);
        } else {
            mHueRect = new Rect(0, 0, mRectWidth, mRectHeight);
            mSaturationRect = new Rect(0, mRectHeight + mRectGap, mRectWidth, mRectHeight * 2 + mRectGap);
            mValueRect = new Rect(0, mRectHeight * 2 + mRectGap * 2, mRectWidth, mRectHeight * 3 + mRectGap * 2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // Hue
        canvas.drawRect(mHueRect, mHuePaint);
        canvas.drawLine(mCurrentHSV[0] * mRectWidth / 360, mHueRect.top, mCurrentHSV[0] * mRectWidth / 360,
                mHueRect.bottom, mLinePaint);

        // Saturation
        int[] mSaturationList = new int[10];
        float saturation = 0;
        for (int i = 0; i < 10; i++) {
            mSaturationList[i] = setHSVColor(mCurrentHSV[0], saturation, mCurrentHSV[2]);
            saturation += 0.1;
        }
        mSaturationShader = new LinearGradient(0, 0, mRectWidth, 0, mSaturationList, null, Shader.TileMode.CLAMP);
        mSaturationPaint.setShader(mSaturationShader);
        canvas.drawRect(mSaturationRect, mSaturationPaint);
        canvas.drawLine(mCurrentHSV[1] * mRectWidth, mSaturationRect.top, mCurrentHSV[1] * mRectWidth,
                mSaturationRect.bottom, mLinePaint);

        // Value
        int[] mValueList = new int[10];
        float value = 0;
        for (int i = 0; i < 10; i++) {
            mValueList[i] = setHSVColor(mCurrentHSV[0], mCurrentHSV[1], value);
            value += 0.1;
        }
        mValueShader = new LinearGradient(0, 0, mRectWidth, 0, mValueList, null, Shader.TileMode.CLAMP);
        mValuePaint.setShader(mValueShader);
        canvas.drawRect(mValueRect, mValuePaint);
        canvas.drawLine(mCurrentHSV[2] * mRectWidth, mValueRect.top, mCurrentHSV[2] * mRectWidth, mValueRect.bottom,
                mLinePaint);

        if (mIsShowPreview) {
            // Preview
            mPreviewPaint.setColor(mCurrentColor);
            canvas.drawRect(mPreviewRect, mPreviewPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mIsShowPreview) {
            setMeasuredDimension(mRectWidth, mRectHeight * 4 + mRectGap * 3);
        } else {
            setMeasuredDimension(mRectWidth, mRectHeight * 3 + mRectGap * 2);
        }
    }

    private int setHSVColor(float hue, float saturation, float value) {
        float[] hsv = new float[3];

        hsv[0] = Math.max(0, Math.min(359, hue));
        hsv[1] = Math.max(0, Math.min(1, saturation));
        hsv[2] = Math.max(0, Math.min(1, value));

        return Color.HSVToColor(hsv);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        boolean inHue = mHueRect.contains((int) x, (int) y);
        boolean inSaturation = mSaturationRect.contains((int) x, (int) y);
        boolean inValue = mValueRect.contains((int) x, (int) y);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (inHue) {
                    float unit = x / mRectWidth;
                    mCurrentHSV[0] = Math.max(0, Math.min(unit * 360, 359));
                    updateCurrentColor();

                } else if (inSaturation) {
                    mCurrentHSV[1] = Math.max(0, Math.min(x / mRectWidth, 1));
                    updateCurrentColor();

                } else if (inValue) {
                    mCurrentHSV[2] = Math.max(0, Math.min(x / mRectWidth, 1));
                    updateCurrentColor();
                }
                break;
        }
        return true;
    }

    private void updateCurrentColor() {
        mCurrentColor = Color.HSVToColor(mCurrentHSV);
        if (mListener != null) {
            mListener.onColorChanged(mCurrentColor, mCurrentHSV);
        }
        invalidate();
    }
}
