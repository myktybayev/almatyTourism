package kz.informatics.okulik.nalog_app.home.module;

public class PopularPlace {
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
}
