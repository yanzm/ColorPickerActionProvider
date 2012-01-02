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

import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

    private int mSelectedColor = Color.BLACK;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem item = menu.findItem(R.id.menu_color_picker);

        HsvColorActionProvider actionProvider = (HsvColorActionProvider) item.getActionProvider();

        actionProvider.setOnColorChangedListener(new HsvColorActionProvider.OnColorChangedListener() {

            @Override
            public void onColorChanged(int color) {
                View v = findViewById(android.R.id.content);
                v.setBackgroundColor(color);
            }

            @Override
            public void onColorSelected(int color) {
                mSelectedColor = color;
                View v = findViewById(android.R.id.content);
                v.setBackgroundColor(color);
            }

            @Override
            public void onCanceled() {
                View v = findViewById(android.R.id.content);
                v.setBackgroundColor(mSelectedColor);
            }
        });

        return true;
    }
}