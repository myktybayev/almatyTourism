package kz.informatics.okulik.nalog_app.home.module;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PopularPlace {
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_SUBTITLE = "subtitle";
    private static final String KEY_ABOUT = "about";
    private static final String KEY_RATING = "rating";
    private static final String KEY_IMAGE_RES = "imageRes";
    private static final String KEY_TAGS = "tags";
    private static final String KEY_GALLERY = "listOfGalleryPhotos";
    private static final String KEY_CATEGORIES = "categories";
    private static final String KEY_LOCATION = "location";
    public String id;
    public String title;
    public String subtitle;
    public String about;
    public float rating;
    public int imageRes;
    public String[] tags;
    public int[] listOfGalleryPhotos;
    /** Categories for filtering (e.g. "all", "citylife", "nature", "parks", "spiritual") */
    public String[] categories;
    /** Geo coordinates for map: "lat,lng" e.g. "43.238949,76.889709" */
    public String location;

    public PopularPlace(String title, String subtitle, float rating, int imageRes, String[] tags) {
        this.id = title;
        this.title = title;
        this.subtitle = subtitle;
        this.rating = rating;
        this.imageRes = imageRes;
        this.tags = tags;
        this.listOfGalleryPhotos = new int[0];
        this.categories = new String[0];
        this.location = "";
    }

    public PopularPlace(String id, String title, String subtitle, float rating, int imageRes, String[] tags) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.rating = rating;
        this.imageRes = imageRes;
        this.tags = tags;
        this.listOfGalleryPhotos = new int[0];
        this.categories = new String[0];
        this.location = "";
    }

    public PopularPlace(String id, String title, String subtitle, String about, float rating, int imageRes, String[] tags, int[] listOfGalleryPhotos) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.about = about;
        this.rating = rating;
        this.imageRes = imageRes;
        this.tags = tags;
        this.listOfGalleryPhotos = listOfGalleryPhotos != null ? listOfGalleryPhotos : new int[0];
        this.categories = new String[0];
        this.location = "";
    }

    public PopularPlace(String id, String title, String subtitle, String about, float rating, int imageRes, String[] tags, int[] listOfGalleryPhotos, String[] categories) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.about = about;
        this.rating = rating;
        this.imageRes = imageRes;
        this.tags = tags;
        this.listOfGalleryPhotos = listOfGalleryPhotos != null ? listOfGalleryPhotos : new int[0];
        this.categories = categories != null ? categories : new String[0];
        this.location = "";
    }

    public PopularPlace(String id, String title, String subtitle, String about, float rating, int imageRes, String[] tags, int[] listOfGalleryPhotos, String[] categories, String location) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.about = about;
        this.rating = rating;
        this.imageRes = imageRes;
        this.tags = tags;
        this.listOfGalleryPhotos = listOfGalleryPhotos != null ? listOfGalleryPhotos : new int[0];
        this.categories = categories != null ? categories : new String[0];
        this.location = location != null ? location : "";
    }

    public boolean hasCategory(String category) {
        if (category == null || categories == null) return false;
        for (String c : categories) {
            if (category.equals(c)) return true;
        }
        return false;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject o = new JSONObject();
        o.put(KEY_ID, id != null ? id : "");
        o.put(KEY_TITLE, title != null ? title : "");
        o.put(KEY_SUBTITLE, subtitle != null ? subtitle : "");
        o.put(KEY_ABOUT, about != null ? about : "");
        o.put(KEY_RATING, (double) rating);
        o.put(KEY_IMAGE_RES, imageRes);
        o.put(KEY_LOCATION, location != null ? location : "");
        JSONArray tagsArr = new JSONArray();
        if (tags != null) for (String t : tags) tagsArr.put(t);
        o.put(KEY_TAGS, tagsArr);
        JSONArray galleryArr = new JSONArray();
        if (listOfGalleryPhotos != null) for (int i : listOfGalleryPhotos) galleryArr.put(i);
        o.put(KEY_GALLERY, galleryArr);
        JSONArray catArr = new JSONArray();
        if (categories != null) for (String c : categories) catArr.put(c);
        o.put(KEY_CATEGORIES, catArr);
        return o;
    }

    public static PopularPlace fromJson(JSONObject o) throws JSONException {
        String id = o.optString(KEY_ID, "");
        String title = o.optString(KEY_TITLE, "");
        String subtitle = o.optString(KEY_SUBTITLE, "");
        String about = o.optString(KEY_ABOUT, "");
        float rating = (float) o.optDouble(KEY_RATING, 0);
        int imageRes = o.optInt(KEY_IMAGE_RES, 0);
        String location = o.optString(KEY_LOCATION, "");
        String[] tags = jsonArrayToStringArray(o.optJSONArray(KEY_TAGS));
        int[] gallery = jsonArrayToIntArray(o.optJSONArray(KEY_GALLERY));
        String[] categories = jsonArrayToStringArray(o.optJSONArray(KEY_CATEGORIES));
        return new PopularPlace(id, title, subtitle, about, rating, imageRes, tags, gallery, categories, location);
    }

    private static String[] jsonArrayToStringArray(JSONArray arr) {
        if (arr == null) return new String[0];
        String[] a = new String[arr.length()];
        for (int i = 0; i < arr.length(); i++) a[i] = arr.optString(i, "");
        return a;
    }

    private static int[] jsonArrayToIntArray(JSONArray arr) {
        if (arr == null) return new int[0];
        int[] a = new int[arr.length()];
        for (int i = 0; i < arr.length(); i++) a[i] = arr.optInt(i, 0);
        return a;
    }
}
