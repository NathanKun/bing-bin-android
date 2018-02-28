package io.bingbin.bingbinandroid.models;

/**
 * Trash category for BingBin.
 *
 * @author Junyang HE
 */

public enum Category {
    PLASTIC("plastique", TrashBin.YELLOW, 1),
    METAL("metal", TrashBin.YELLOW, 2),
    CARDBOARD("carton", TrashBin.YELLOW, 3),
    PAPER("papier", TrashBin.BLUE, 4),
    GLASS("verre", TrashBin.GREEN, 5),
    FOOD("dechet menager", TrashBin.COMPOST, 6),
    LIGHTBULB("ampoule", TrashBin.CALLNUMBER, 7),
    CUMBERSOME("encombrant", TrashBin.LEBONCOIN, 8),
    ELECTRONIC("équipement électronique", TrashBin.LEBONCOIN, 9),
    BATTERY("pile ou ampoule", TrashBin.SUPERMARKET, 10),
    CLOTHE("vêtements usés", TrashBin.CLOTHES, 11),
    MEDICINE("medicament", TrashBin.PHARMACY, 12),
    OTHER("autre", TrashBin.OTHER, 99);

    private String frenchName;
    private TrashBin trashbin;
    private int categoryId;

    Category(String frenchName, TrashBin trashbin, int categoryId) {
        this.frenchName = frenchName;
        this.trashbin = trashbin;
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return frenchName;
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

    public TrashBin getTrashbin() { return trashbin; }

    public String getFrenchName() { return frenchName; }

    public int getCategoryId() { return categoryId; }

    public static Category fromName(String name) {
        if (Category.contains(name)) {
            return Category.valueOf(name.toUpperCase());
        } else {
            return Category.OTHER;
        }
    }

    public static Category fromFrenchName(String frenchName) {
        Category[] ctgs = values();

        for (Category c : ctgs) {
            if (c.getFrenchName().toLowerCase().equals(frenchName.toLowerCase())) {
                return c;
            }
        }
        return null;
    }

    public static String getFrenchNameById(int categoryId) {
        Category[] ctgs = values();

        for (Category c : ctgs) {
            if (c.getCategoryId() == categoryId) {
                return c.getFrenchName();
            }
        }
        return "";
    }

    public static String getFrenchNameByName(String name) {
        return fromName(name).getFrenchName();
    }
}
