package com.sticker.stickermakerwhatsapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sticker.stickermakerwhatsapp.data.ItemCut;
import com.sticker.stickermakerwhatsapp.R;

import java.util.ArrayList;

public class MenuCutAdapter extends RecyclerView.Adapter<MenuCutAdapter.ViewHolder> {

    private ArrayList<ItemCut> listCut;
    private int pos = 1;

    public MenuCutAdapter(ArrayList<ItemCut> listCut) {
        this.listCut = listCut;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.item_menu_cut, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        return listCut.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imMenuCut;
        int itemPos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imMenuCut = itemView.findViewById(R.id.imMenuCut);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {

            final ItemCut itemCut = listCut.get(position);
            imMenuCut.setImageResource(itemCut.getIcon());

            if (pos == position) {
                imMenuCut.setImageResource(itemCut.getIconsl());
            } else {
                imMenuCut.setImageResource(itemCut.getIcon());
            }
            itemPos = position;
        }

        @Override
        public void onClick(View v) {
            pos = itemPos;
            notifyDataSetChanged();
            onClickMenuCut.onClickItem(listCut.get(pos));
        }
    }

    public interface OnClickMenuCut {
        void onClickItem(ItemCut itemCut);
    }

    public OnClickMenuCut onClickMenuCut;

    public void setOnClickMenuCut(OnClickMenuCut onClickMenuCut) {
        this.onClickMenuCut = onClickMenuCut;
    }
}
