package com.sticker.stickermakerwhatsapp.data;

public class ItemCut {
    private String name;
    private int icon;
    private int iconsl;

    public ItemCut() {
    }

    public ItemCut(String name, int icon, int iconsl) {
        this.name = name;
        this.iconsl = iconsl;
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

    public int getIconsl() {
        return iconsl;
    }

    public void setIconsl(int iconsl) {
        this.iconsl = iconsl;
    }
}
