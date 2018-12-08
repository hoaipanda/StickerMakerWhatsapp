package com.sticker.stickermakerwhatsapp.adapter;

import android.content.Context;
import android.net.LinkAddress;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sticker.stickermakerwhatsapp.R;

import java.util.ArrayList;

public class StickerEditAdapter extends RecyclerView.Adapter<StickerEditAdapter.ViewHolder> {
    int pos = -1;
    private ArrayList<String> listMenuSticker;
    private Context context;

    public StickerEditAdapter(Context context, ArrayList<String> listMenuSticker) {
        this.context = context;
        this.listMenuSticker = listMenuSticker;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.item_sticker_edit, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        return listMenuSticker.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imStickerEdit, imBoder;
        int itemPos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imBoder = itemView.findViewById(R.id.imBoder);
            imStickerEdit = itemView.findViewById(R.id.imStickerEdit);
        }

        public void bind(int position) {
            String menuSticker = listMenuSticker.get(position);
            Glide.with(context).load(Uri.parse("file:///android_asset/sticker/" + menuSticker + "/01.png")).into(imStickerEdit);
            if (pos == position) {
                imBoder.setVisibility(View.VISIBLE);
            } else {
                imBoder.setVisibility(View.GONE);
            }
            itemPos = position;
        }

        @Override
        public void onClick(View view) {
            pos = itemPos;
            notifyDataSetChanged();
            onClickStickerEdit.onClickSticker(listMenuSticker.get(pos));
        }
    }

    public interface OnClickStickerEdit {
        void onClickSticker(String menuSticker);
    }

    public OnClickStickerEdit onClickStickerEdit;

    public void setOnClickStickerEdit(OnClickStickerEdit onClickStickerEdit) {
        this.onClickStickerEdit = onClickStickerEdit;
    }
}
