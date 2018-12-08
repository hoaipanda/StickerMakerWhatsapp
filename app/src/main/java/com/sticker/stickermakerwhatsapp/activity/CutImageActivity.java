package com.sticker.stickermakerwhatsapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sticker.stickermakerwhatsapp.AppUtil;
import com.sticker.stickermakerwhatsapp.Contains;
import com.sticker.stickermakerwhatsapp.cutphoto.CutPhotoCircleView;
import com.sticker.stickermakerwhatsapp.cutphoto.CutPhotoSquareView;
import com.sticker.stickermakerwhatsapp.data.ItemCut;
import com.sticker.stickermakerwhatsapp.R;
import com.sticker.stickermakerwhatsapp.adapter.MenuCutAdapter;
import com.sticker.stickermakerwhatsapp.cutphoto.CutPhotoView;
import com.sticker.stickermakerwhatsapp.cutphoto.DataSingleton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class CutImageActivity extends AppCompatActivity implements CutPhotoView.OnActionCut, CutPhotoSquareView.OnActionCutSquare, CutPhotoCircleView.OnActionCutCircle {
    private String image;
    private CutPhotoView cutView;
    private CutPhotoSquareView cutSquareView;
    private CutPhotoCircleView cutCircle;
    private RecyclerView rvMenuCut;
    private RelativeLayout lyCut;
    private int width, height;
    private RelativeLayout.LayoutParams params;
    private ArrayList<ItemCut> listCut = new ArrayList<>();
    private MenuCutAdapter menuCutAdapter;
    private ImageView imBack;

    private String type, mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cut_image);
        image = getIntent().getStringExtra(Contains.PATH_IMAGE);
        initView();
        loadImage(Uri.parse(image), lyCut);
        type = getIntent().getStringExtra(Contains.TYPE_EDIT);
        mId = getIntent().getStringExtra(Contains.IDENTIDIER);
        listCut = AppUtil.getListItemCut();

        updateRvMenuCut();

    }

    private void updateRvMenuCut() {
        menuCutAdapter = new MenuCutAdapter(listCut);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        rvMenuCut.setLayoutManager(linearLayoutManager);
        rvMenuCut.setAdapter(menuCutAdapter);

        menuCutAdapter.setOnClickMenuCut(new MenuCutAdapter.OnClickMenuCut() {
            @Override
            public void onClickItem(ItemCut itemCut) {
                switch (itemCut.getName()) {
                    case "Select all":
                        Intent intent = new Intent(CutImageActivity.this, EditImageActivity.class);
                        intent.putExtra(Contains.PATH_IMAGE, image);
                        startActivity(intent);
                        break;
                    case "Freehand":
                        cutSquareView.setVisibility(View.GONE);
                        cutCircle.setVisibility(View.GONE);
                        cutView.setVisibility(View.VISIBLE);
                        break;
                    case "Cut square":
                        cutSquareView.setVisibility(View.VISIBLE);
                        cutView.setVisibility(View.GONE);
                        cutCircle.setVisibility(View.GONE);
                        break;
                    case "Cut circle":
                        cutView.setVisibility(View.GONE);
                        cutCircle.setVisibility(View.VISIBLE);
                        cutSquareView.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    private void loadImage(final Uri data, final RelativeLayout relativeLayout) {
        relativeLayout.post(new Runnable() {
            @Override
            public void run() {
                width = relativeLayout.getWidth();
                height = relativeLayout.getHeight();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setParamslayout(bitmap, relativeLayout);
                cutView.setImageBitmap(bitmap);
                cutSquareView.setImageBitmap(bitmap);
                cutCircle.setImageBitmap(bitmap);
            }
        });

    }

    private void initView() {
        cutSquareView = findViewById(R.id.cutSquare);
        cutSquareView.setOnActionCutSquare(this);
        cutCircle = findViewById(R.id.cutCircle);
        cutCircle.setOnActionCutCircle(this);
        imBack = findViewById(R.id.imBack);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cutView = findViewById(R.id.cutView);
        cutView.setOnActionCut(this);
        rvMenuCut = findViewById(R.id.rvMenuCut);
        lyCut = findViewById(R.id.lyCut);
        params = (RelativeLayout.LayoutParams) lyCut.getLayoutParams();
    }

    public void setParamslayout(Bitmap resource, RelativeLayout relativeLayout) {
        if (ratio(resource)) {
            params.width = width;
            params.height = width * resource.getHeight() / resource.getWidth();
            relativeLayout.requestLayout();
        } else {
            params.height = height;
            params.width = height * resource.getWidth() / resource.getHeight();
            relativeLayout.requestLayout();
        }
    }

    private boolean ratio(Bitmap bitmap) {
        if (bitmap != null) {
            float w = (float) bitmap.getWidth() / width;
            float h = (float) bitmap.getHeight() / height;
            if (w > h) {
            }
        }
        return true;
    }

    @Override
    public void onActionDraw(Boolean b) {
        if (b) {
            rvMenuCut.setVisibility(View.GONE);
        } else {
            rvMenuCut.setVisibility(View.VISIBLE);
            DataSingleton.getInstance().crop = true;
            Bitmap bitmap1 = cutView.crop();
            if (bitmap1 != null) {
                try {
                    String path = saveImage(bitmap1);
                    Toast.makeText(CutImageActivity.this, "Cut done", Toast.LENGTH_SHORT).show();
                    cutView.resetView();
                    transActivity(path);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(CutImageActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CutImageActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String saveImage(Bitmap bitmap) throws IOException {
        File root = File.createTempFile("image", ".png");
        root.deleteOnExit();
        if (!root.exists()) {
            root.mkdirs();
        } else {
            Log.e("path Dir", root.getAbsolutePath());
        }
        OutputStream outputStream = new FileOutputStream(root);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        outputStream.flush();
        outputStream.close();
        return root.getAbsolutePath();
    }

    @Override
    public void onActionDrawSquare(Boolean b) {
        if (b) {
            rvMenuCut.setVisibility(View.GONE);
        } else {
            rvMenuCut.setVisibility(View.VISIBLE);
            DataSingleton.getInstance().crop = true;
            Bitmap bitmap1 = cutSquareView.crop();
            if (bitmap1 != null) {
                try {
                    String path = saveImage(bitmap1);
                    Toast.makeText(CutImageActivity.this, "Cut done", Toast.LENGTH_SHORT).show();
                    cutSquareView.resetView();
                    transActivity(path);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(CutImageActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            } else {
            }
        }
    }

    @Override
    public void onActionDrawCircle(Boolean b) {
        if (b) {
            rvMenuCut.setVisibility(View.GONE);
        } else {
            rvMenuCut.setVisibility(View.VISIBLE);
            DataSingleton.getInstance().crop = true;
            Bitmap bitmap1 = cutCircle.crop();
            if (bitmap1 != null) {
                try {
                    String path = saveImage(bitmap1);
                    Toast.makeText(CutImageActivity.this, "Cut done", Toast.LENGTH_SHORT).show();
                    cutCircle.resetView();
                    transActivity(path);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(CutImageActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            } else {
            }
        }
    }

    private void transActivity(String path) {
        Intent intent = new Intent(CutImageActivity.this, EditImageActivity.class);
        intent.putExtra(Contains.PATH_IMAGE, path);
        intent.putExtra(Contains.TYPE_EDIT, type);
        intent.putExtra(Contains.IDENTIDIER, mId);
        startActivity(intent);
    }
}
