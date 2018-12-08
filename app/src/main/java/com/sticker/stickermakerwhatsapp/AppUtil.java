package com.sticker.stickermakerwhatsapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sticker.stickermakerwhatsapp.data.ItemCut;
import com.sticker.stickermakerwhatsapp.data.MenuEdit;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class AppUtil {
    public static ArrayList<ItemCut> getListItemCut() {
        ArrayList<ItemCut> list = new ArrayList<>();
        list.add(new ItemCut("Select all", R.drawable.selectall, R.drawable.selectall_sl));
        list.add(new ItemCut("Freehand", R.drawable.free_hand, R.drawable.free_hand_sl));
        list.add(new ItemCut("Cut square", R.drawable.cutsquare, R.drawable.cutsquare_sl));
        list.add(new ItemCut("Cut circle", R.drawable.cutcircle, R.drawable.cutcircle_sl));
        return list;
    }

    public static ArrayList<MenuEdit> getListMenuEdit() {
        ArrayList<MenuEdit> list = new ArrayList<>();
        list.add(new MenuEdit("Sticker", R.drawable.sticker));
        list.add(new MenuEdit("Emoji", R.drawable.emoji));
        list.add(new MenuEdit("Text", R.drawable.text));
        return list;
    }

    public static ArrayList<String> getListStickerEdit(Context context, String file) {
        ArrayList<String> listRain = new ArrayList<>();
        String[] images = new String[0];
        try {
            images = context.getAssets().list("sticker" + file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(images));
        for (String st : list) {
            listRain.add(st);
        }
        return listRain;
    }

    public static ArrayList<String> getListText(Context context) {
        ArrayList<String> listRain = new ArrayList<>();
        String[] images = new String[0];
        try {
            images = context.getAssets().list("texts");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(images));
        for (String st : list) {
            listRain.add(st);
        }
        return listRain;
    }

    public static ArrayList<String> getListEmoji(Context context) {
        ArrayList<String> listRain = new ArrayList<>();
        String[] images = new String[0];
        try {
            images = context.getAssets().list("emoji");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(images));
        for (String st : list) {
            listRain.add(st);
        }
        return listRain;
    }


    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
        }

        return bitmap;
    }
}
