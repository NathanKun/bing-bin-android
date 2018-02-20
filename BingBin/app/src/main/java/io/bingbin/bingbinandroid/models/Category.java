package io.bingbin.bingbinandroid.models;

/**
 * Trash category for BingBin.
 *
 * @author Junyang HE
 */

public enum Category {
    PAPER("paper", TrashBinColor.YELLOW, 4),
    METAL("metal", TrashBinColor.OTHER, 2),
    CARDBOARD("cardboard", TrashBinColor.YELLOW, 3),
    GLASS("glass", TrashBinColor.GREEN, 5),
    PLASTIC("plastic", TrashBinColor.YELLOW, 1),
    OTHER("other", TrashBinColor.OTHER, 99);

    private String lowerCaseName;
    private TrashBinColor color;
    private int categoryId;

    Category(String friendlyName, TrashBinColor color, int categoryId) {
        this.lowerCaseName = friendlyName;
        this.color = color;
        this.categoryId = categoryId;
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

    public String getLowerCaseName() { return lowerCaseName; }

    public int getCategoryId() { return categoryId; }
}
