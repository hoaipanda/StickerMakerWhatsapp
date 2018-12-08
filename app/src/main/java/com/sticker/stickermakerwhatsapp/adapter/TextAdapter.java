package com.sticker.stickermakerwhatsapp.adapter;

import android.content.Context;
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

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.ViewHolder> {

    private ArrayList<String> listText;
    private Context context;
    private String type;

    public TextAdapter(Context context, ArrayList<String> listText, String type) {
        this.context = context;
        this.listText = listText;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.item_sticker_edit, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final String text = listText.get(i);
        Glide.with(context).load(Uri.parse("file:///android_asset/" + type + "/" + text)).into(viewHolder.imText);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickItemText != null) {
                    onClickItemText.onClickText(text);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listText.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imText = itemView.findViewById(R.id.imStickerEdit);
            if (type.equals("texts")) {
                imText.setColorFilter(context.getResources().getColor(R.color.black));
            }
        }
    }

    public interface OnClickItemText {
        void onClickText(String text);
    }

    public OnClickItemText onClickItemText;

    public void setOnClickItemText(OnClickItemText onClickItemText) {
        this.onClickItemText = onClickItemText;
    }
}
