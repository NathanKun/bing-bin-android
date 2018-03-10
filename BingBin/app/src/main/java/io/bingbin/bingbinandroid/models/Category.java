package io.bingbin.bingbinandroid.models;

/**
 * Trash category for BingBin.
 *
 * @author Junyang HE
 */

public enum Category {
    PLASTIC("Plastique", TrashBin.YELLOW, 1, "1000 ans pour décomposer  une bouteille en plastique"),
    METAL("Metal", TrashBin.YELLOW, 2, "200 ans pour décomposer une cannette"),
    CARDBOARD("Carton", TrashBin.YELLOW, 3, "On utilise 222 222 t de cartons par jour"),
    PAPER("Papier", TrashBin.BLUE, 4, "35 000 d'arbres sont utilisé pour faire des papiers chaque jour"),
    GLASS("Verre", TrashBin.GREEN, 5, "35 kg de verre sont recyclé chaque seconde"),
    FOOD("Déchet menager", TrashBin.COMPOST, 6, "Met le dans le compost et récupère des engrais plus tard"),
    LIGHTBULB("Ampoule", TrashBin.SUPERMARKET, 7, "Les supermarchés récuppèrent de s ampoules"),
    CUMBERSOME("Encombrant", TrashBin.LEBONCOIN, 8, "Vent ton produit ou appel SMDO 0. 44 38 29 00"),
    ELECTRONIC("Equipement électronique", TrashBin.LEBONCOIN, 9, "Vent ton produit ou appel SMDO 0. 44 38 29 00"),
    BATTERY("Pile", TrashBin.SUPERMARKET, 10, "5000 ans pour décomposer une pile"),
    CLOTHE("Vêtements usés", TrashBin.CLOTHE, 11, "11 kg de textiles achetés par habitant par an"),
    MEDICINE("Médicament", TrashBin.PHARMACY, 12, "Les médicaments non utilisés présentent des risques d'intoxications par ingestion accidentelle"),
    OTHER("Autre", TrashBin.OTHER, 99, "Il faut que Bing Bin apprenne plus pour le valoriser");

    private final String frenchName;
    private final TrashBin trashbin;
    private final int categoryId;
    private final String text;

    Category(String frenchName, TrashBin trashbin, int categoryId, String text) {
        this.frenchName = frenchName;
        this.trashbin = trashbin;
        this.categoryId = categoryId;
        this.text = text;
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

    public String getText() { return text; }

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
