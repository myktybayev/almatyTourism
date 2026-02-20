package kz.informatics.okulik.nalog_app.home.module;

/**
 * Place definition with resource IDs. Resolved to PopularPlace via DestinationsRepository.
 */
public class PlaceDefinition {
    public final String id;
    public final int titleResId;
    public final int subtitleResId;
    public final int aboutResId;
    public final float rating;
    public final int imageRes;
    public final int[] tagResIds;
    public final int[] galleryResIds;
    public final String[] categories;
    /** Sub-filters for chips: "museums", "parks", "shopping" */
    public final String[] subcategories;

    public PlaceDefinition(String id, int titleResId, int subtitleResId, int aboutResId,
                           float rating, int imageRes, int[] tagResIds, int[] galleryResIds, String[] categories) {
        this(id, titleResId, subtitleResId, aboutResId, rating, imageRes, tagResIds, galleryResIds, categories, null);
    }

    public PlaceDefinition(String id, int titleResId, int subtitleResId, int aboutResId,
                           float rating, int imageRes, int[] tagResIds, int[] galleryResIds,
                           String[] categories, String[] subcategories) {
        this.id = id;
        this.titleResId = titleResId;
        this.subtitleResId = subtitleResId;
        this.aboutResId = aboutResId;
        this.rating = rating;
        this.imageRes = imageRes;
        this.tagResIds = tagResIds != null ? tagResIds : new int[0];
        this.galleryResIds = galleryResIds != null ? galleryResIds : new int[0];
        this.categories = categories != null ? categories : new String[0];
        this.subcategories = subcategories != null ? subcategories : new String[0];
    }

    public boolean hasCategory(String category) {
        if (category == null) return false;
        for (String c : categories) {
            if (category.equals(c)) return true;
        }
        return false;
    }

    public boolean hasSubcategory(String sub) {
        if (sub == null || "all".equals(sub)) return true;
        for (String s : subcategories) {
            if (sub.equals(s)) return true;
        }
        return false;
    }
}
