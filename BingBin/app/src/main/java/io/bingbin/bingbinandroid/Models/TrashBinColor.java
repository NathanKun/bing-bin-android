package io.bingbin.bingbinandroid.Models;

import io.bingbin.bingbinandroid.R;

/**
 * Enum for trash bin color
 */

public enum TrashBinColor {
    YELLOW(R.drawable.trashbin_yellow),
    GREEN(R.drawable.trashbin_green),
    BLUE(R.drawable.trashbin_blue),
    BLACK(R.drawable.trashbin_black),
    PINK(R.drawable.trashbin_pink),
    OTHER(R.drawable.trashbin_black);

    private int imageResource;

    TrashBinColor (int id) {
        this.imageResource = id;
    }

    public int getImageResource() { return this.imageResource; }
}
