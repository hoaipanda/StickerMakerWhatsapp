package com.sticker.stickermakerwhatsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class CustomTextRegular extends TextView{
    public CustomTextRegular(Context context) {
        super(context);
        setTypeface(context);
    }

    public CustomTextRegular(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setTypeface(context);
    }

    public CustomTextRegular(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(context);
    }

    private void setTypeface(Context context) {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/SFUFranklinGothicBook.TTF"));
    }
}
