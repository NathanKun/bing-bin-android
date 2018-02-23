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
    COMPOST(R.drawable.rabbit_1, "Compost"),
    SUPERMARKET(R.drawable.rabbit_2, "Supermarché"),
    CLOTHES(R.drawable.rabbit_1, "Point relais (Boîte blanche)"),
    PHARMACY(R.drawable.rabbit_1, "Pharmacie"),
    CALLNUMBER(R.drawable.rabbit_1, "Appel "),
    LEBONCOIN(R.drawable.rabbit_1, "Leboncoin"),
    OTHER(R.drawable.rabbit_1, "Autre");

    private int imageResource;
    private String frenchName;

    TrashBin(int id, String frenchName) {
        this.imageResource = id;
        this.frenchName = frenchName;
    }

    public int getImageResource() { return this.imageResource; }
    public String getFrenchName() { return this.frenchName; }
}
