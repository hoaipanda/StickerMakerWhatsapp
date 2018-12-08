package com.sticker.stickermakerwhatsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class CustomTextBold extends TextView {
    public CustomTextBold(Context context) {
        super(context);
        setTypeface(context);
    }

    public CustomTextBold(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setTypeface(context);
    }

    public CustomTextBold(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(context);
    }

    private void setTypeface(Context context) {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/SFUFranklinGothicHeavy.TTF"));
    }
}
