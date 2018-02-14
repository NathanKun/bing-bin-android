package io.bingbin.bingbinandroid.models;

import io.bingbin.bingbinandroid.R;

/**
 * Enum for trash bin color
 */

public enum TrashBinColor {
    YELLOW(R.drawable.trashbin_yellow, "Jaune"),
    GREEN(R.drawable.trashbin_green, "Vert"),
    BLUE(R.drawable.trashbin_blue, "Bleu"),
    BLACK(R.drawable.trashbin_black, "Black"),
    PINK(R.drawable.trashbin_pink, "Rose"),
    OTHER(R.drawable.trashbin_black, "Autre");

    private int imageResource;
    private String frenchName;

    TrashBinColor (int id, String frenchName) {
        this.imageResource = id;
        this.frenchName = frenchName;
    }

    public int getImageResource() { return this.imageResource; }
    public String getFrenchName() { return this.frenchName; }
}
