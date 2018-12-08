package com.sticker.stickermakerwhatsapp.data;

public class MenuEdit {
    private String name;
    private int icon;

    public MenuEdit() {
    }

    public MenuEdit(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
