package com.sticker.stickermakerwhatsapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sticker.stickermakerwhatsapp.R;
import com.sticker.stickermakerwhatsapp.data.ItemCut;
import com.sticker.stickermakerwhatsapp.data.MenuEdit;

import java.util.ArrayList;

public class MenuEditAdapter extends RecyclerView.Adapter<MenuEditAdapter.ViewHolder> {
    ArrayList<MenuEdit> listMenu;

    int pos = -1;

    public MenuEditAdapter(ArrayList<MenuEdit> listMenu) {
        this.listMenu = listMenu;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.item_menu_edit, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        return listMenu.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imMenuEdit, imChoose;
        int itemPos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imMenuEdit = itemView.findViewById(R.id.imMenuEdit);
            imChoose = itemView.findViewById(R.id.imChoose);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {

            final MenuEdit menuEdit = listMenu.get(position);
            imMenuEdit.setImageResource(menuEdit.getIcon());

            if (pos == position) {
                imChoose.setVisibility(View.VISIBLE);
            } else {
                imChoose.setVisibility(View.GONE);
            }
            itemPos = position;
        }

        @Override
        public void onClick(View view) {
            pos = itemPos;

            notifyDataSetChanged();
            onClickMenuEdit.onClickEdit(listMenu.get(pos));
        }
    }

    public interface OnClickMenuEdit {
        void onClickEdit(MenuEdit menuEdit);
    }

    public OnClickMenuEdit onClickMenuEdit;

    public void setOnClickMenuEdit(OnClickMenuEdit onClickMenuEdit) {
        this.onClickMenuEdit = onClickMenuEdit;
    }
}
