package io.bingbin.bingbinandroid.models;

import io.bingbin.bingbinandroid.R;

/**
 * Enum for trash bin color
 */

public enum TrashBin {
    YELLOW(R.drawable.trashbin_yellow, "Poubelle jaune"),
    GREEN(R.drawable.trashbin_green, "Bac vert"),
    BLUE(R.drawable.trashbin_blue, "Poubelle bleu"),
    BLACK(R.drawable.trashbin_black, "Poubelle black"),
    PINK(R.drawable.trashbin_pink, "Rose"),
    COMPOST(R.drawable.trashbin_compost, "Compost"),
    SUPERMARKET(R.drawable.trashbin_store, "Supermarché"),
    PHARMACY(R.drawable.trashbin_pharmacy, "Pharmacie"),
    LEBONCOIN(R.drawable.trashbin_leboncoin, "Leboncoin"),
    CLOTHE(R.drawable.trashbin_clothes, "Le relais"),
    CIGARETTE(R.drawable.trashbin_cigarette, "?????????"),
    HUMAN(R.drawable.trashbin_human, "Un humain, \njette le !"),
    BBCERCLE(R.drawable.bingbin_icon_no_bg, "BB CERCLE"),
    OTHER(R.drawable.trashbin_black, "Autre");

    private final int imageResource;
    private final String frenchName;

    TrashBin(int id, String frenchName) {
        this.imageResource = id;
        this.frenchName = frenchName;
    }

    public int getImageResource() { return this.imageResource; }
    public String getFrenchName() { return this.frenchName; }
}
