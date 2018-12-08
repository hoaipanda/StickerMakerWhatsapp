package com.sticker.stickermakerwhatsapp.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sticker.stickermakerwhatsapp.Contains;
import com.sticker.stickermakerwhatsapp.R;
import com.sticker.stickermakerwhatsapp.StickerPack;
import com.sticker.stickermakerwhatsapp.StickerPackLoader;
import com.sticker.stickermakerwhatsapp.adapter.StickerPackAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class MainActivity extends AddStickerPackActivity {
    private RecyclerView rvStickerPack;
    private RelativeLayout lyCreate;
    private Dialog dialogCreate;
    private EditText edPackName, edPackAuthor;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ArrayList<StickerPack> listPack = new ArrayList<>();
    private String mJson;
    private StickerPackAdapter stickerPackAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences(Contains.DATA, Context.MODE_PRIVATE);
        editor = preferences.edit();
        mJson = preferences.getString(Contains.CONTENTJSON, "");

        initView();

        new LoadStickerPack().execute();


    }

    private class LoadStickerPack extends AsyncTask<Void, Void, Pair<String, ArrayList<StickerPack>>> {

        @Override
        protected Pair<String, ArrayList<StickerPack>> doInBackground(Void... voids) {
            ArrayList<StickerPack> stickerPackList;
            try {
                stickerPackList = StickerPackLoader.fetchStickerPacks(MainActivity.this);
                if (stickerPackList.size() == 0) {
                    return new Pair<>("could not find any packs", null);
                } else {
                    return new Pair<>("", stickerPackList);
                }

            } catch (Exception e) {
                Log.e("EntryActivity", "error fetching sticker packs", e);
                return new Pair<>(e.getMessage(), null);
            }
        }

        @Override
        protected void onPostExecute(Pair<String, ArrayList<StickerPack>> stringArrayListPair) {
            super.onPostExecute(stringArrayListPair);
            listPack = stringArrayListPair.second;
            updateRvStickerPack();
        }
    }

    private void updateRvStickerPack() {
        stickerPackAdapter = new StickerPackAdapter(MainActivity.this, listPack);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 1);
        rvStickerPack.setLayoutManager(gridLayoutManager);
        rvStickerPack.setAdapter(stickerPackAdapter);
    }

    private void initView() {
        rvStickerPack = findViewById(R.id.rvStickerPack);
        lyCreate = findViewById(R.id.lyCreate);
        lyCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCreate = new Dialog(MainActivity.this);
                dialogCreate.requestWindowFeature(Window.FEATURE_NO_TITLE);
                Window v = ((Dialog) dialogCreate).getWindow();
                v.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogCreate.setContentView(R.layout.dialog_create);
                TextView tvCancel = dialogCreate.findViewById(R.id.tvCancel);
                TextView tvCreate = dialogCreate.findViewById(R.id.tvCreate);
                edPackAuthor = dialogCreate.findViewById(R.id.edPackAuthor);
                edPackName = dialogCreate.findViewById(R.id.edPackname);
                tvCancel.setOnClickListener(lsClickDialog);
                tvCreate.setOnClickListener(lsClickDialog);
                dialogCreate.show();
            }
        });
    }

    private View.OnClickListener lsClickDialog = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvCancel:
                    dialogCreate.dismiss();
                    break;
                case R.id.tvCreate:
                    String id = "";
                    JSONObject jsonPack = null;
                    JSONArray jsonArray = null;
                    if (mJson.length() == 0) {
                        id = "1";
                        try {
                            jsonPack = new JSONObject();
                            jsonPack.put("android_play_store_link", "https://play.google.com/store/apps/details?id=" + getPackageName());
                            jsonPack.put("ios_app_store_link", "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        jsonArray = new JSONArray();

                    } else {
                        try {
                            jsonPack = new JSONObject(mJson);
                            jsonArray = jsonPack.getJSONArray("sticker_packs");
                            int size = jsonArray.length();
                            id = (size + 1) + "";
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("identifier", id);
                        jsonObject.put("name", edPackName.getText().toString());
                        jsonObject.put("publisher", edPackAuthor.getText().toString());
                        jsonObject.put("tray_image_file", "");
                        jsonObject.put("publisher_email", "");
                        jsonObject.put("publisher_website", "");
                        jsonObject.put("privacy_policy_website", "");
                        jsonObject.put("license_agreement_website", "");
                        JSONArray arrSticker = new JSONArray();
                        jsonObject.put("stickers", arrSticker);
                        jsonArray.put(jsonObject);
                        jsonPack.put("sticker_packs", jsonArray);
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(MainActivity.this, DetailPackActivity.class);
                    intent.putExtra(Contains.IDENTIDIER, id);
                    startActivity(intent);
                    dialogCreate.dismiss();
                    break;
            }
        }
    };
}
