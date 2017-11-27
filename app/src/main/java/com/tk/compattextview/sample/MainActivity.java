package com.tk.compattextview.sample;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;

import com.tk.compattextview.CompatTextView;
import com.tk.compattextview.TintUtils;

public class MainActivity extends AppCompatActivity {
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Drawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
            }
        });
        final CompatTextView textView = (CompatTextView) findViewById(R.id.btn_drawable_config);

        drawable = TintUtils.tint(ContextCompat.getDrawable(this, R.drawable.vector_github), Color.WHITE);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable[] drawables = textView.getCompoundDrawables();
                if (null == drawables[2]) {
                    textView.setCompoundDrawables(drawables[0],
                            drawables[1],
                            drawable,
                            drawables[3]);
                } else {
                    textView.setCompoundDrawables(drawables[0],
                            drawables[1],
                            null,
                            drawables[3]);
                }
            }
        });
    }
}
