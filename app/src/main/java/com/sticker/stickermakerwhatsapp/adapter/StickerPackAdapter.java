package com.sticker.stickermakerwhatsapp.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sticker.stickermakerwhatsapp.Contains;
import com.sticker.stickermakerwhatsapp.R;
import com.sticker.stickermakerwhatsapp.StickerPack;
import com.sticker.stickermakerwhatsapp.WhitelistCheck;

import java.util.ArrayList;

public class StickerPackAdapter extends RecyclerView.Adapter<StickerPackAdapter.ViewHolder> {
    private ArrayList<StickerPack> listPack;
    private Context context;

    public StickerPackAdapter(Context context, ArrayList<StickerPack> listPack) {
        this.context = context;
        this.listPack = listPack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.item_pack, viewGroup, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final StickerPack pack = listPack.get(i);
        viewHolder.tvNamePack.setText(pack.getName());
        viewHolder.tvPublisher.setText(pack.getPublisher());
        if (WhitelistCheck.isWhitelisted(context, pack.getIdentifier())) {
            viewHolder.imAdd.setImageResource(R.drawable.tick);
        } else {
            viewHolder.imAdd.setImageResource(R.drawable.add);
        }

    }

    @Override
    public int getItemCount() {
        return listPack.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamePack, tvPublisher;
        LinearLayout lyImage;
        ImageView imAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamePack = itemView.findViewById(R.id.tvNamePack);
            tvPublisher = itemView.findViewById(R.id.tvPublisher);
            lyImage = itemView.findViewById(R.id.lyImage);
            imAdd = itemView.findViewById(R.id.imAdd);
        }
    }
}
