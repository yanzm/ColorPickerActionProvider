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