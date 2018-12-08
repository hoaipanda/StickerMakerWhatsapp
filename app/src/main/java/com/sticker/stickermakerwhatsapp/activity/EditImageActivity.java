package com.sticker.stickermakerwhatsapp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sticker.stickermakerwhatsapp.AppUtil;
import com.sticker.stickermakerwhatsapp.Contains;
import com.sticker.stickermakerwhatsapp.R;
import com.sticker.stickermakerwhatsapp.adapter.MenuEditAdapter;
import com.sticker.stickermakerwhatsapp.adapter.StickerEditAdapter;
import com.sticker.stickermakerwhatsapp.adapter.TextAdapter;
import com.sticker.stickermakerwhatsapp.data.MenuEdit;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.StickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class EditImageActivity extends AppCompatActivity {


    private ImageView imBack, imDone;
    private RecyclerView rvMenuEdit;
    private ArrayList<MenuEdit> listEdit = new ArrayList<>();
    private MenuEditAdapter menuEditAdapter;

    private String image;

    private RelativeLayout lyImage;
    private StickerView stickerView;
    private Bitmap bmImage;
    private RelativeLayout lySticker;
    private RecyclerView rvMenuSticker, rvText;
    private StickerEditAdapter stickerEditAdapter;
    private ArrayList<String> listMenuSticker, listText;
    private TextAdapter textAdapter;
    private Context context;
    private RelativeLayout lyEmoji;
    private RecyclerView rvEmoji;
    private TextAdapter emojiAdapter;
    private ArrayList<String> listEmoji;
    private boolean isText, isSticker, isEmoji;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private String type, mId;

    private String mJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_image);
        context = this;
        type = getIntent().getStringExtra(Contains.TYPE_EDIT);
        mId = getIntent().getStringExtra(Contains.IDENTIDIER);
        preferences = getSharedPreferences(Contains.DATA, Context.MODE_PRIVATE);
        editor = preferences.edit();
        mJson = preferences.getString(Contains.CONTENTJSON, "");
        initView();
        updateRvEdit();


        image = getIntent().getStringExtra(Contains.PATH_IMAGE);
        Log.e("hoaiii", image);

        bmImage = BitmapFactory.decodeFile(image);

        addIcon(bmImage);

        listMenuSticker = AppUtil.getListStickerEdit(EditImageActivity.this, "");
        updateRvMenuSticker();

        listText = AppUtil.getListText(context);
        updateRvText();

        listEmoji = AppUtil.getListEmoji(context);
        updateRvEmoji();
    }

    private void updateRvMenuSticker() {
        stickerEditAdapter = new StickerEditAdapter(EditImageActivity.this, listMenuSticker);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayout.HORIZONTAL, false);
        rvMenuSticker.setLayoutManager(linearLayoutManager);
        rvMenuSticker.setAdapter(stickerEditAdapter);
        stickerEditAdapter.setOnClickStickerEdit(new StickerEditAdapter.OnClickStickerEdit() {
            @Override
            public void onClickSticker(String menuSticker) {
                ArrayList<String> listSticker = AppUtil.getListStickerEdit(context, "/" + menuSticker);
            }
        });
    }

    private void updateRvText() {
        textAdapter = new TextAdapter(context, listText, "texts");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayout.HORIZONTAL, false);
        rvText.setLayoutManager(linearLayoutManager);
        rvText.setAdapter(textAdapter);
        textAdapter.setOnClickItemText(new TextAdapter.OnClickItemText() {
            @Override
            public void onClickText(String text) {
                Bitmap bm = AppUtil.getBitmapFromAsset(context, "texts/" + text);
                bm = changeBitmapColor(bm);
                addIcon(bm);
            }
        });
    }

    private void updateRvEmoji() {
        emojiAdapter = new TextAdapter(context, listEmoji, "emoji");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 5);
        rvEmoji.setLayoutManager(gridLayoutManager);
        rvEmoji.setAdapter(emojiAdapter);
        emojiAdapter.setOnClickItemText(new TextAdapter.OnClickItemText() {
            @Override
            public void onClickText(String text) {
                Bitmap bm = AppUtil.getBitmapFromAsset(context, "emoji/" + text);
                addIcon(bm);
            }
        });
    }


    private void initView() {
        imBack = findViewById(R.id.imBack);
        imDone = findViewById(R.id.imDone);
        lyEmoji = findViewById(R.id.lyEmoji);
        rvEmoji = findViewById(R.id.rvEmoji);
        rvText = findViewById(R.id.rvText);
        rvMenuEdit = findViewById(R.id.rvMenuEdit);
        lyImage = findViewById(R.id.lyImage);
        stickerView = findViewById(R.id.stickerView);
        rvMenuSticker = findViewById(R.id.rvMenuSticker);
        lySticker = findViewById(R.id.lySticker);

        imBack.setOnClickListener(lsClick);
        imDone.setOnClickListener(lsClick);
    }

    private View.OnClickListener lsClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imBack:
                    finish();
                    break;
                case R.id.imDone:
                    getBitmapFromLayout();
//                    addIcon(BitmapFactory.decodeFile(strImage));
                    JSONObject jsonPack = null;
                    try {
                        jsonPack = new JSONObject(mJson);
                        JSONArray arrPack = jsonPack.getJSONArray("sticker_packs");
                        for (int i = 0; i < arrPack.length(); i++) {
                            JSONObject oject = arrPack.getJSONObject(i);
                            if (oject.getString(Contains.IDENTIDIER).equals(mId)) {
                                switch (type) {
                                    case Contains.TYPE_TRAY_ICON:
                                        oject.put(Contains.TRAY_ICON, strImage);
                                        break;
                                    case Contains.TYPE_STICKER:
                                        JSONArray arrSticker = oject.getJSONArray("stickers");
                                        JSONObject jsonSticker = new JSONObject();
                                        jsonSticker.put("image_file", "1.webp");
                                        JSONArray arrEmoji = new JSONArray();
                                        arrEmoji.put("🎉");
                                        jsonSticker.put("emojis", arrEmoji);
                                        arrSticker.put(jsonSticker);
                                        break;
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    editor.putString(Contains.CONTENTJSON, jsonPack.toString());
                    editor.commit();

                    FileOutputStream outputStream;

                    try {
                        outputStream = openFileOutput("content.json", Context.MODE_PRIVATE);
                        outputStream.write(jsonPack.toString().getBytes());
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finish();
                    break;
            }
        }
    };

    private void updateRvEdit() {
        listEdit = AppUtil.getListMenuEdit();
        menuEditAdapter = new MenuEditAdapter(listEdit);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 3);
        rvMenuEdit.setLayoutManager(linearLayoutManager);
        rvMenuEdit.setAdapter(menuEditAdapter);
        menuEditAdapter.setOnClickMenuEdit(new MenuEditAdapter.OnClickMenuEdit() {
            @Override
            public void onClickEdit(MenuEdit menuEdit) {
                switch (menuEdit.getName()) {
                    case "Sticker":
                        isSticker = !isSticker;
                        isEmoji = false;
                        isText = false;
                        if (isSticker) {
                            lySticker.setVisibility(View.VISIBLE);
                            rvMenuSticker.setVisibility(View.VISIBLE);
                            rvText.setVisibility(View.GONE);
                            lyEmoji.setVisibility(View.GONE);
                        } else {
                            lySticker.setVisibility(View.GONE);
                        }

                        break;
                    case "Emoji":
                        isEmoji = !isEmoji;
                        isText = false;
                        isSticker = false;
                        if (isEmoji) {
                            lyEmoji.setVisibility(View.VISIBLE);
                            lySticker.setVisibility(View.GONE);
                        } else {
                            lyEmoji.setVisibility(View.GONE);
                        }

                        break;
                    case "Text":
                        isText = !isText;
                        isEmoji = false;
                        isSticker = false;
                        if (isText) {
                            lySticker.setVisibility(View.VISIBLE);
                            rvMenuSticker.setVisibility(View.GONE);
                            rvText.setVisibility(View.VISIBLE);
                            lyEmoji.setVisibility(View.GONE);
                        } else {
                            lySticker.setVisibility(View.GONE);
                        }

                        break;

                }
            }
        });
    }

    private void addIcon(Bitmap bitmap) {
        float ratio = bitmap.getWidth() / (float) bitmap.getHeight();
        int with = 720;
        int height = Math.round(with / ratio);
        bitmap = Bitmap.createScaledBitmap(
                bitmap, with, height, false);
        Drawable drawable = new BitmapDrawable(bitmap);
        stickerView.addSticker(
                new DrawableSticker(drawable));
    }

    @Override
    public void onBackPressed() {
        if (isEmoji) {
            isEmoji = false;
            lyEmoji.setVisibility(View.GONE);
        } else if (isSticker || isText) {
            isText = false;
            isSticker = false;
            lySticker.setVisibility(View.GONE);
        } else {
            finish();
        }

    }

    private void getBitmapFromLayout() {
        stickerView.setLocked(true);
        lyImage.setDrawingCacheEnabled(true);
        lyImage.buildDrawingCache();
        Bitmap bm = lyImage.getDrawingCache();
        stickerView.setLocked(false);

        strImage = saveImagePNG(bm);

    }


    public String saveImagePNG(Bitmap bitmap) {
        String root = getCacheDir().getAbsolutePath();
        File myDir = new File(root);
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        String fname = Calendar.getInstance().getTimeInMillis() + "";
        File file = new File(myDir, fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fname;
    }

    public String saveImageWebp(Bitmap bitmap) {
        String root = getCacheDir().getAbsolutePath();
        File myDir = new File(root);
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        String fname = Calendar.getInstance().getTimeInMillis() + "";
        File file = new File(myDir, fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.WEBP, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fname;
    }


    private String strImage;

    private Bitmap changeBitmapColor(Bitmap sourceBitmap) {
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0,
                sourceBitmap.getWidth() - 1, sourceBitmap.getHeight() - 1);
        Paint p = new Paint();
        ColorFilter filter = new LightingColorFilter(context.getResources().getColor(R.color.black), 1);
        p.setColorFilter(filter);

        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, p);
        return resultBitmap;
    }

}
