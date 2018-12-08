package com.sticker.stickermakerwhatsapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sticker.stickermakerwhatsapp.Contains;
import com.sticker.stickermakerwhatsapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailPackActivity extends AppCompatActivity {
    private RelativeLayout lyTrayIcon;
    private ImageView imTrayIcon, imAdd;
    static final int REQUEST_TAKE_PHOTO = 211, REWQUEST_CHOSE_PHOTO = 111;
    private BottomSheetDialog dialogPick;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String json = "";
    private String mIdentifier;
    private JSONObject mJsonObject;
    private String trayIcon;
    private ArrayList<String> listSticker = new ArrayList<>();
    private Context context;
    private String type;
    private String nameSticker, publisher;
    private TextView tvNamePack, tvPublisher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pack);
        context = this;
        initView();
        initListen();
        preferences = getSharedPreferences(Contains.DATA, Context.MODE_PRIVATE);
        editor = preferences.edit();
        mIdentifier = getIntent().getStringExtra(Contains.IDENTIDIER);
        Log.e("hoaiii", "mIdentifier" + mIdentifier);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIdentifier.length() > 0) {
            json = preferences.getString(Contains.CONTENTJSON, "");
            new LoadJson().execute(json);
        }
    }

    private class LoadJson extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            if (json.length() > 0) {
                try {
                    JSONObject pack = new JSONObject(json);
                    JSONArray jsonPack = pack.getJSONArray("sticker_packs");
                    if (jsonPack.length() > 0)
                        for (int i = 0; i < jsonPack.length(); i++) {
                            JSONObject jsonObject = jsonPack.getJSONObject(i);
                            String identifier = jsonObject.getString(Contains.IDENTIDIER);
                            if (identifier.equals(mIdentifier)) {
                                mJsonObject = jsonObject;
                                nameSticker = mJsonObject.getString("name");
                                publisher = mJsonObject.getString("publisher");
                                trayIcon = mJsonObject.getString(Contains.TRAY_ICON);
                                JSONArray arrSticker = mJsonObject.getJSONArray("stickers");
                                if (arrSticker.length() > 0)
                                    for (int j = 0; j < arrSticker.length(); j++) {
                                        JSONObject jsonSticker = arrSticker.getJSONObject(i);
                                        listSticker.add(jsonSticker.getString("image_file"));
                                    }
                            }
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (trayIcon.length() > 0) {
                Glide.with(context).load(getCacheDir().getAbsolutePath() + "/" + trayIcon).into(imTrayIcon);
                imAdd.setVisibility(View.GONE);
            }

            tvNamePack.setText(nameSticker);
            tvPublisher.setText(publisher);
        }
    }

    private void initView() {
        lyTrayIcon = findViewById(R.id.lyTrayIcon);
        imTrayIcon = findViewById(R.id.imTrayIcon);
        tvNamePack = findViewById(R.id.tvNamePack);
        tvPublisher = findViewById(R.id.tvPublisher);
        imAdd = findViewById(R.id.imAdd);
    }

    private void initListen() {
        lyTrayIcon.setOnClickListener(lsClick);
    }


    private View.OnClickListener lsClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.lyTrayIcon:
                    dialogPick = new BottomSheetDialog(DetailPackActivity.this);
                    dialogPick.setContentView(R.layout.dialog_pick_image);
                    LinearLayout lyCamera = dialogPick.findViewById(R.id.lyCamera);
                    LinearLayout lyGallery = dialogPick.findViewById(R.id.lyGallery);
                    lyCamera.setOnClickListener(lsClickDialog);
                    lyGallery.setOnClickListener(lsClickDialog);
                    type = Contains.TYPE_TRAY_ICON;
                    dialogPick.show();
                    break;
            }
        }
    };

    private View.OnClickListener lsClickDialog = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.lyCamera:
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                    break;
                case R.id.lyGallery:
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, REWQUEST_CHOSE_PHOTO);
                    break;

            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case REWQUEST_CHOSE_PHOTO:
                    if (data != null) {
                        final Uri imageUri = data.getData();
                        String image = String.valueOf(imageUri);
                        transActivity(image);
                    }

                    break;
                case REQUEST_TAKE_PHOTO:
                    if (data != null) {
                        final Uri imageUri = data.getData();
                        String image = String.valueOf(imageUri);
                        transActivity(image);
                    }
                    break;

            }
        }

    }

    private void transActivity(String image) {
        Intent intent = new Intent(DetailPackActivity.this, CutImageActivity.class);
        intent.putExtra(Contains.PATH_IMAGE, image);
        intent.putExtra(Contains.TYPE_EDIT, type);
        intent.putExtra(Contains.IDENTIDIER, mIdentifier);
        startActivity(intent);
    }
}
