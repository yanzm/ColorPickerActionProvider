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
import android.graphics.Color;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;

public class HsvColorActionProvider extends ActionProvider {

    public interface OnColorChangedListener {
        public void onColorChanged(int color);

        public void onColorSelected(int color);

        public void onCanceled();
    }

    private OnColorChangedListener mListener;

    private Context mContext;

    private LayoutInflater mLayoutInflater;

    private PopupWindow mPopupWindow;

    private HsvColorPickerView mColorPicker;

    private int mInitialColor;

    public HsvColorActionProvider(Context context) {
        super(context);
        mContext = context;

        mInitialColor = Color.HSVToColor(new float[] { 180, 0.7f, 0.8f });

        mLayoutInflater = LayoutInflater.from(context);

        View v = mLayoutInflater.inflate(R.layout.color_popup, null, false);

        mColorPicker = (HsvColorPickerView) v.findViewById(R.id.color_picker);
        mColorPicker.showPreview(true);
        mColorPicker.setIniticalColor(mInitialColor);
        mColorPicker.setOnColorChangedListener(new HsvColorPickerView.OnColorChangedListener() {

            @Override
            public void onColorChanged(int color, float[] hsv) {
                if (mListener != null) {
                    mListener.onColorChanged(color);
                }
            }
        });

        v.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                if (mListener != null) {
                    mListener.onCanceled();
                }
            }
        });

        v.findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mInitialColor = mColorPicker.getCurrentColor();
                if (mListener != null) {
                    mListener.onColorSelected(mInitialColor);
                }
                mPopupWindow.dismiss();
            }
        });

        mPopupWindow = new PopupWindow(v);
        mPopupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.panel_bg));
        mPopupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void setOnColorChangedListener(OnColorChangedListener l) {
        mListener = l;
    }

    @Override
    public View onCreateActionView() {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        final View actionItem = layoutInflater.inflate(R.layout.color_action_provider, null);

        ImageButton button = (ImageButton) actionItem.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mPopupWindow.isShowing()) {
                    mColorPicker.setIniticalColor(mInitialColor);
                    mPopupWindow.showAsDropDown(actionItem);
                } else {
                    mPopupWindow.dismiss();
                }
            }
        });

        return actionItem;
    }
}
