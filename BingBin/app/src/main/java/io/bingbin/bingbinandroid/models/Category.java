package io.bingbin.bingbinandroid.models;

/**
 * Trash category for BingBin.
 *
 * @author Junyang HE
 */

public enum Category {
    PAPER("paper", TrashBinColor.YELLOW),
    METAL("metal", TrashBinColor.OTHER),
    CARDBOARD("cardboard", TrashBinColor.YELLOW),
    GLASS("glass", TrashBinColor.GREEN),
    PLASTIC("plastic", TrashBinColor.YELLOW),
    OTHER("other", TrashBinColor.OTHER);

    private String lowerCaseName;
    private TrashBinColor color;

    Category(String friendlyName, TrashBinColor color) {
        this.lowerCaseName = friendlyName;
        this.color = color;
    }

    @Override
    public String toString() {
        return lowerCaseName;
    }

    public static boolean contains(String name) {
        //所有的枚举值
        Category[] ctgs = values();
        //遍历查找
        for (Category c : ctgs) {
            if (c.name().equals(name.toUpperCase())) {
                return true;
            }
        }

        return false;
    }

    public static Category saveValueOf(String name) {
        if (Category.contains(name.toLowerCase())) {
            return Category.valueOf(name.toUpperCase());
        } else {
            return Category.OTHER;
        }
    }

    public TrashBinColor getColor() { return color; }
}
